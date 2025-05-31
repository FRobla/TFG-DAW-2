package com.proyecto3d.backend.apirest.model.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto3d.backend.apirest.model.dao.FavoritoDao;
import com.proyecto3d.backend.apirest.model.entity.Favorito;
import com.proyecto3d.backend.apirest.model.entity.Anuncio;
import com.proyecto3d.backend.apirest.model.entity.Usuario;

@Service
public class FavoritoServiceImpl implements FavoritoService {

    @Autowired
    private FavoritoDao favoritoDao;

    @Override
    @Transactional(readOnly = true)
    public List<Favorito> findAll() {
        return (List<Favorito>) favoritoDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Favorito findById(Long id) {
        return favoritoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Favorito> findByUsuarioId(Long usuarioId) {
        return favoritoDao.findByUsuarioIdOrderByFechaMarcadoDesc(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Favorito> findByUsuarioId(Long usuarioId, Pageable pageable) {
        return favoritoDao.findByUsuarioIdOrderByFechaMarcadoDesc(usuarioId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Favorito> findByUsuarioIdWithJoinFetch(Long usuarioId, Pageable pageable) {
        return favoritoDao.findByUsuarioIdWithJoinFetch(usuarioId, pageable);
    }

    @Override
    @Transactional
    public Favorito save(Favorito favorito) {
        return favoritoDao.save(favorito);
    }

    @Override
    @Transactional
    public void delete(Favorito favorito) {
        favoritoDao.delete(favorito);
    }

    @Override
    @Transactional
    public void deleteAll() {
        favoritoDao.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeFavoritoUsuarioAnuncio(Long anuncioId, Long usuarioId) {
        return favoritoDao.existsByAnuncioIdAndUsuarioId(anuncioId, usuarioId);
    }

    @Override
    @Transactional
    public Favorito toggleFavorito(Long anuncioId, Long usuarioId) {
        Optional<Favorito> favoritoOpt = favoritoDao.findByAnuncioIdAndUsuarioId(anuncioId, usuarioId);
        if (favoritoOpt.isPresent()) {
            favoritoDao.delete(favoritoOpt.get());
            return null;
        } else {
            Favorito nuevoFavorito = new Favorito();
            
            // Crear anuncio con solo el ID
            Anuncio anuncio = new Anuncio();
            anuncio.setId(anuncioId);
            nuevoFavorito.setAnuncio(anuncio);
            
            // Crear usuario con solo el ID
            Usuario usuario = new Usuario();
            usuario.setId(usuarioId);
            nuevoFavorito.setUsuario(usuario);
            
            // Establecer fecha actual
            nuevoFavorito.setFechaMarcado(new Date());
            
            return favoritoDao.save(nuevoFavorito);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getNumeroFavoritosByAnuncioId(Long anuncioId) {
        return favoritoDao.countByAnuncioId(anuncioId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getNumeroFavoritosByUsuarioId(Long usuarioId) {
        return favoritoDao.countByUsuarioId(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAnunciosMasFavoritos() {
        List<Object[]> resultados = favoritoDao.findAnunciosMasFavoritos();
        List<Map<String, Object>> anunciosConConteo = new ArrayList<>();
        for (Object[] resultado : resultados) {
            Map<String, Object> anuncioInfo = new HashMap<>();
            anuncioInfo.put("anuncioId", resultado[0]);
            anuncioInfo.put("cantidad", resultado[1]);
            anunciosConConteo.add(anuncioInfo);
        }
        return anunciosConConteo;
    }
} 