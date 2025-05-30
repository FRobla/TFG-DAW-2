package com.proyecto3d.backend.apirest.model.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto3d.backend.apirest.model.dao.AnuncioDao;
import com.proyecto3d.backend.apirest.model.dao.CarritoDAO;
import com.proyecto3d.backend.apirest.model.dao.UsuarioDao;
import com.proyecto3d.backend.apirest.model.dto.CarritoDTO;
import com.proyecto3d.backend.apirest.model.entity.Anuncio;
import com.proyecto3d.backend.apirest.model.entity.Carrito;
import com.proyecto3d.backend.apirest.model.entity.Usuario;

/**
 * Implementación del servicio para la gestión del carrito
 */
@Service
@Transactional
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private CarritoDAO carritoDAO;

    @Autowired
    private AnuncioDao anuncioDao;

    @Autowired
    private UsuarioDao usuarioDao;

    @Override
    public CarritoDTO anadirAlCarrito(Long usuarioId, Long anuncioId, Integer cantidad, 
                                    String materialSeleccionado, String colorSeleccionado,
                                    Boolean acabadoPremium, Boolean urgente, Boolean envioGratis) {

        System.out.println("=== DEBUG: Añadiendo al carrito ===");
        System.out.println("usuarioId: " + usuarioId);
        System.out.println("anuncioId: " + anuncioId);
        System.out.println("cantidad: " + cantidad);
        System.out.println("materialSeleccionado: " + materialSeleccionado);

        // Validar que el usuario existe
        Usuario usuario = usuarioDao.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        System.out.println("Usuario encontrado: " + usuario.getNombre());

        // Validar que el anuncio existe
        Anuncio anuncio = anuncioDao.findById(anuncioId)
            .orElseThrow(() -> new RuntimeException("Anuncio no encontrado con ID: " + anuncioId));

        System.out.println("Anuncio encontrado: " + anuncio.getTitulo());
        System.out.println("Precio base del anuncio: " + anuncio.getPrecioBase());

        // Verificar si ya existe un elemento con el mismo material en el carrito
        Optional<Carrito> carritoExistente = carritoDAO.findByUsuarioIdAndAnuncioIdAndMaterial(
            usuarioId, anuncioId, materialSeleccionado);

        if (carritoExistente.isPresent()) {
            System.out.println("Actualizando elemento existente en el carrito");
            // Si existe, actualizar la cantidad y servicios adicionales
            Carrito carrito = carritoExistente.get();
            carrito.setCantidad(carrito.getCantidad() + cantidad);
            carrito.setColorSeleccionado(colorSeleccionado);
            carrito.setAcabadoPremium(acabadoPremium);
            carrito.setUrgente(urgente);
            carrito.setEnvioGratis(envioGratis);
            carrito.calcularPrecioTotal();
            Carrito carritoGuardado = carritoDAO.save(carrito);
            return convertirACarritoDTO(carritoGuardado);
        } else {
            System.out.println("Creando nuevo elemento en el carrito");
            // Si no existe, crear un nuevo elemento
            Carrito nuevoCarrito = Carrito.builder()
                .usuario(usuario)
                .anuncio(anuncio)
                .cantidad(cantidad)
                .materialSeleccionado(materialSeleccionado)
                .colorSeleccionado(colorSeleccionado)
                .acabadoPremium(acabadoPremium != null ? acabadoPremium : false)
                .urgente(urgente != null ? urgente : false)
                .envioGratis(envioGratis != null ? envioGratis : false)
                .precioUnitario(anuncio.getPrecioBase())
                .fechaAgregado(new Date())
                .build();
            
            nuevoCarrito.calcularPrecioTotal();
            System.out.println("Precio total calculado: " + nuevoCarrito.getPrecioTotal());
            Carrito carritoGuardado = carritoDAO.save(nuevoCarrito);
            return convertirACarritoDTO(carritoGuardado);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarritoDTO> obtenerCarritoUsuario(Long usuarioId) {
        List<Carrito> elementos = carritoDAO.findByUsuarioId(usuarioId);
        
        return elementos.stream()
            .map(this::convertirACarritoDTO)
            .collect(Collectors.toList());
    }

    @Override
    public CarritoDTO actualizarCantidad(Long carritoId, Integer nuevaCantidad) {
        Carrito carrito = carritoDAO.findById(carritoId)
            .orElseThrow(() -> new RuntimeException("Elemento del carrito no encontrado con ID: " + carritoId));

        if (nuevaCantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }

        carrito.setCantidad(nuevaCantidad);
        carrito.calcularPrecioTotal();
        Carrito carritoGuardado = carritoDAO.save(carrito);
        
        return convertirACarritoDTO(carritoGuardado);
    }

    @Override
    public CarritoDTO actualizarServiciosAdicionales(Long carritoId, Boolean acabadoPremium, 
                                                   Boolean urgente, Boolean envioGratis) {
        Carrito carrito = carritoDAO.findById(carritoId)
            .orElseThrow(() -> new RuntimeException("Elemento del carrito no encontrado con ID: " + carritoId));

        carrito.setAcabadoPremium(acabadoPremium != null ? acabadoPremium : false);
        carrito.setUrgente(urgente != null ? urgente : false);
        carrito.setEnvioGratis(envioGratis != null ? envioGratis : false);
        carrito.calcularPrecioTotal();
        
        Carrito carritoGuardado = carritoDAO.save(carrito);
        return convertirACarritoDTO(carritoGuardado);
    }

    @Override
    public void eliminarElementoCarrito(Long carritoId) {
        if (!carritoDAO.existsById(carritoId)) {
            throw new RuntimeException("Elemento del carrito no encontrado con ID: " + carritoId);
        }
        carritoDAO.deleteById(carritoId);
    }

    @Override
    public void vaciarCarrito(Long usuarioId) {
        carritoDAO.deleteByUsuarioId(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarElementosCarrito(Long usuarioId) {
        return carritoDAO.countByUsuarioId(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularTotalCarrito(Long usuarioId) {
        return carritoDAO.calcularTotalCarrito(usuarioId);
    }

    /**
     * Convierte una entidad Carrito a CarritoDTO
     */
    private CarritoDTO convertirACarritoDTO(Carrito carrito) {
        CarritoDTO dto = new CarritoDTO();
        dto.setId(carrito.getId());
        dto.setUsuarioId(carrito.getUsuario().getId());
        dto.setAnuncioId(carrito.getAnuncio().getId());
        dto.setTituloAnuncio(carrito.getAnuncio().getTitulo());
        dto.setDescripcionAnuncio(carrito.getAnuncio().getDescripcion());
        dto.setImagenAnuncio(carrito.getAnuncio().getImagen());
        dto.setNombreProveedor(carrito.getAnuncio().getUsuario().getNombre());
        dto.setCantidad(carrito.getCantidad());
        dto.setMaterialSeleccionado(carrito.getMaterialSeleccionado());
        dto.setColorSeleccionado(carrito.getColorSeleccionado());
        dto.setAcabadoPremium(carrito.getAcabadoPremium());
        dto.setUrgente(carrito.getUrgente());
        dto.setEnvioGratis(carrito.getEnvioGratis());
        dto.setPrecioUnitario(carrito.getPrecioUnitario());
        dto.setPrecioTotal(carrito.getPrecioTotal());
        dto.setFechaAgregado(carrito.getFechaAgregado());
        return dto;
    }
} 