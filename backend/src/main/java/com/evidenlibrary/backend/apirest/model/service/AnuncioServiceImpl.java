package com.evidenlibrary.backend.apirest.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evidenlibrary.backend.apirest.model.dao.AnuncioDao;
import com.evidenlibrary.backend.apirest.model.entity.Impresora;
import com.evidenlibrary.backend.apirest.model.entity.DetallePedido;
import com.evidenlibrary.backend.apirest.model.entity.Categoria;
import com.evidenlibrary.backend.apirest.model.entity.Anuncio;

@Service
public class AnuncioServiceImpl implements AnuncioService {

    @Autowired
    private AnuncioDao anuncioDao;

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
        return anuncioDao.findByImpresorasId(impresoraId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Anuncio> findByCategoriaIdPaginado(Long categoriaId, Pageable pageable) {
        return anuncioDao.findByCategoriasId(categoriaId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Anuncio> findByCategoriaIdAndImpresoraIdPaginado(Long categoriaId, Long impresoraId, Pageable pageable) {
        return anuncioDao.findByCategoriasIdAndImpresorasId(categoriaId, impresoraId, pageable);
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
    public List<Anuncio> findAnunciosByImpresoraId(Long id) {
        // TODO Auto-generated method stub
        return null;
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

        // Desasociar el anuncio de todas sus impresoras
        for (Impresora impresora : anuncio.getImpresora()) {
            impresora.getAnuncios().remove(anuncio);
        }

        anuncio.getImpresora().clear();

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

        // Obtener los detalles de los pedidos
        List<DetallePedido> detallesPedido = detallePedidoService.findAll();

        // Para cada anuncio, desasociar todas sus impresoras y categorias
        for (Anuncio anuncio : anuncios) {
            // Desasociar el anuncio de todas sus impresoras
            for (Impresora impresora : anuncio.getImpresora()) {
                impresora.getAnuncios().remove(anuncio);
            }
            anuncio.getImpresora().clear();

            // Desasociar el anuncio de todos sus géneros
            for (Categoria categoria : anuncio.getCategorias()) {
                categoria.getAnuncios().remove(anuncio);
            }
            anuncio.getCategorias().clear();
        }

        // Desasociar de pedidos
        for (DetallePedido detalle : detallesPedido) {
            detallePedidoService.delete(detalle);
        }

        // Actualizar los anuncios con las asociaciones eliminadas
        for (Anuncio anuncio : anuncios) {
            anuncioDao.save(anuncio);
        }

        // Eliminar todos los anuncios
        anuncioDao.deleteAll();
    } 

}
