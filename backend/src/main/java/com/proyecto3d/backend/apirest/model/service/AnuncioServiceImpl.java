package com.proyecto3d.backend.apirest.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto3d.backend.apirest.model.dao.AnuncioDao;
import com.proyecto3d.backend.apirest.model.dao.ImpresoraDao;
import com.proyecto3d.backend.apirest.model.entity.Impresora;
import com.proyecto3d.backend.apirest.model.entity.DetallePedido;
import com.proyecto3d.backend.apirest.model.entity.Categoria;
import com.proyecto3d.backend.apirest.model.entity.Anuncio;

@Service
public class AnuncioServiceImpl implements AnuncioService {

    @Autowired
    private AnuncioDao anuncioDao;

    @Autowired
    private ImpresoraDao impresoraDao;

    @Autowired
    private DetallePedidoService detallePedidoService;

    @Override
    @Transactional(readOnly = true)
    public List<Anuncio> findAll() {
        return (List<Anuncio>) anuncioDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Anuncio> findAllPaginado(Pageable pageable) {
        return anuncioDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Anuncio> findByImpresoraIdPaginado(Long impresoraId, Pageable pageable) {
        return anuncioDao.findByImpresoraId(impresoraId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Anuncio> findByCategoriaIdPaginado(Long categoriaId, Pageable pageable) {
        return anuncioDao.findByCategoriasId(categoriaId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Anuncio> getMejorValorados() {
        // Actualizo valoraciones medias
        List<Anuncio> todosAnuncios = anuncioDao.findAll();
        for (Anuncio anuncio : todosAnuncios) {
            if (!anuncio.getValoraciones().isEmpty()) {
                double suma = anuncio.getValoraciones().stream()
                        .mapToDouble(valoracion -> valoracion.getPuntuacion())
                        .sum();
                anuncio.setValoracionMedia(suma / anuncio.getValoraciones().size());
                anuncioDao.save(anuncio);
            }
        }
        return anuncioDao.findTop10MejorValorados();
    }

    // Método para obtener el anuncio y su valoración media
    @Override
    @Transactional
    public Anuncio obtenerAnuncioConValoracionMedia(Long anuncioId) {
        Anuncio anuncio = anuncioDao.findById(anuncioId)
                .orElseThrow(() -> new RuntimeException("Anuncio no encontrado"));

        // Actualizo valoracion media
        if (anuncio.getValoraciones().isEmpty()) {
            anuncio.setValoracionMedia(0.0);
        } else {
            double suma = anuncio.getValoraciones().stream()
                    .mapToDouble(valoracion -> valoracion.getPuntuacion())
                    .sum();
            anuncio.setValoracionMedia(suma / anuncio.getValoraciones().size());
        }

        return anuncioDao.save(anuncio);
    }

    @Override
    @Transactional(readOnly = true)
    public Anuncio findById(Long id) {
        return anuncioDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Anuncio findByTitulo(String titulo) {
        return anuncioDao.findByTitulo(titulo).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Impresora> findImpresorasByAnuncioId(Long anuncioId) {
        // Devuelve todas las impresoras cuyo campo anuncio.id coincida
        return impresoraDao.findAll().stream()
            .filter(i -> i.getAnuncio() != null && i.getAnuncio().getId().equals(anuncioId))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Anuncio> findAnunciosByImpresoraId(Long id) {
        // Ahora, cada impresora solo tiene un anuncio, así que busca la impresora y devuelve su anuncio si existe
        Impresora impresora = impresoraDao.findById(id).orElse(null);
        if (impresora != null && impresora.getAnuncio() != null) {
            return List.of(impresora.getAnuncio());
        }
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Anuncio> findAnunciosByFavoritoId(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Anuncio> findAnunciosByCarritoId(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional
    public Anuncio save(Anuncio anuncio) {
        return anuncioDao.save(anuncio);
    }

    @Override
    @Transactional
    public void delete(Anuncio anuncio) {
        // Obtener los detalles de pedido relacionados con este anuncio
        List<DetallePedido> detallesPedido = detallePedidoService.findByPedidoId(anuncio.getId());

        // Desasociar el anuncio de todas sus impresoras (ahora ManyToOne)
        List<Impresora> impresoras = impresoraDao.findAll();
        for (Impresora impresora : impresoras) {
            if (impresora.getAnuncio() != null && impresora.getAnuncio().getId().equals(anuncio.getId())) {
                impresora.setAnuncio(null);
                impresoraDao.save(impresora);
            }
        }

        // Desasociar el anuncio de todas sus categorias
        for (Categoria categoria : anuncio.getCategorias()) {
            categoria.getAnuncios().remove(anuncio);
        }
        anuncio.getCategorias().clear();

        // Desasociar de pedidos
        for (DetallePedido detalle : detallesPedido) {
            detallePedidoService.delete(detalle);
        }

        // Actualizar el anuncio con las asociaciones eliminadas
        anuncioDao.save(anuncio);

        // Eliminar el anuncio
        anuncioDao.delete(anuncio);
    }

    @Override
    @Transactional
    public void deleteAll() {
        // Obtener todos los anuncios
        List<Anuncio> anuncios = findAll();
        // Obtener todos los impresoras
        List<Impresora> impresoras = impresoraDao.findAll();
        // Obtener los detalles de los pedidos
        List<DetallePedido> detallesPedido = detallePedidoService.findAll();

        // Desasociar impresoras de anuncios
        for (Impresora impresora : impresoras) {
            impresora.setAnuncio(null);
            impresoraDao.save(impresora);
        }

        // Para cada anuncio, desasociar todas sus categorias
        for (Anuncio anuncio : anuncios) {
            for (Categoria categoria : anuncio.getCategorias()) {
                categoria.getAnuncios().remove(anuncio);
            }
            anuncio.getCategorias().clear();
        }

        // Desasociar de pedidos
        for (DetallePedido detalle : detallesPedido) {
            detallePedidoService.delete(detalle);
        }

        // Eliminar todos los anuncios
        anuncioDao.deleteAll();
    } 

}
