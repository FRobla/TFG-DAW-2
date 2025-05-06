package com.proyecto3d.backend.apirest.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto3d.backend.apirest.model.dao.AnuncioDao;
import com.proyecto3d.backend.apirest.model.dao.CategoriaDao;
import com.proyecto3d.backend.apirest.model.entity.Anuncio;
import com.proyecto3d.backend.apirest.model.entity.Categoria;

@Service
public class AnuncioServiceImpl implements AnuncioService {

    @Autowired
    private AnuncioDao anuncioDao;

    @Autowired
    private CategoriaDao categoriaDao;

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
    public Page<Anuncio> findByCategoriaIdPaginado(Long categoriaId, Pageable pageable) {
        return anuncioDao.findByCategoriaId(categoriaId, pageable);
    }

    /*
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
    } */

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

    /*
    @Override
    @Transactional(readOnly = true)
    public List<Impresora> findImpresorasByAnuncioId(Long anuncioId) {
        // Devuelve todas las impresoras cuyo campo anuncio.id coincida
        return impresoraDao.findAll().stream()
            .filter(i -> i.getAnuncios() != null && i.getAnuncios().stream().anyMatch(a -> a.getId().equals(anuncioId)))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Anuncio> findAnunciosByImpresoraId(Long id) {
        // Ahora, cada impresora solo tiene un anuncio, así que busca la impresora y devuelve su anuncio si existe
        Impresora impresora = impresoraDao.findById(id).orElse(null);
        if (impresora != null && impresora.getAnuncios() != null) {
            return impresora.getAnuncios();
        }
        return List.of();
    } */

    @Override
    @Transactional
    public Anuncio save(Anuncio anuncio) {
        return anuncioDao.save(anuncio);
    }

    @Override
    @Transactional
    public void delete(Anuncio anuncio) {
        // Eliminar anuncio de la categoria
        for (Categoria categoria : anuncio.getCategorias()) {
            categoria.getAnuncios().remove(anuncio);
        }
        anuncio.getCategorias().clear();

        // Eliminar el anuncio
        anuncioDao.delete(anuncio);
    }

    @Override
    @Transactional
    public void deleteAll() {   
        // Eliminar todas las categorias
        categoriaDao.deleteAll();
        // Eliminar todos los anuncios
        anuncioDao.deleteAll();
    } 



}
