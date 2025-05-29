package com.proyecto3d.backend.apirest.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto3d.backend.apirest.model.dao.ValoracionDao;
import com.proyecto3d.backend.apirest.model.entity.Valoracion;

@Service
public class ValoracionServiceImpl implements ValoracionService {
	
	@Autowired
	private ValoracionDao valoracionDao;

	@Override
	@Transactional(readOnly = true)
	public List<Valoracion> findAll() {
		return (List<Valoracion>) valoracionDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Valoracion findById(Long id) {
		return valoracionDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Valoracion> findByAnuncioId(Long anuncioId) {
		return valoracionDao.findByAnuncioIdOrderByFechaValoracionDesc(anuncioId);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Valoracion> findByAnuncioId(Long anuncioId, Pageable pageable) {
		return valoracionDao.findByAnuncioIdOrderByFechaValoracionDesc(anuncioId, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Valoracion> findByUsuarioId(Long usuarioId) {
		return valoracionDao.findByUsuarioIdOrderByFechaValoracionDesc(usuarioId);
	}

	@Override
	@Transactional
	public Valoracion save(Valoracion valoracion) {
		return valoracionDao.save(valoracion);
	}

	@Override
	@Transactional
	public void delete(Valoracion valoracion) {
		valoracionDao.delete(valoracion);
	}
	
    @Override
    @Transactional
    public void deleteAll() {
        valoracionDao.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Double getValoracionMediaByAnuncioId(Long anuncioId) {
        Double media = valoracionDao.findValoracionMediaByAnuncioId(anuncioId);
        return media != null ? media : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getNumeroValoracionesByAnuncioId(Long anuncioId) {
        return valoracionDao.countByAnuncioId(anuncioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDistribucionValoraciones() {
        List<Object[]> resultados = valoracionDao.findDistribucionValoraciones();
        List<Map<String, Object>> distribucion = new ArrayList<>();
        
        for (Object[] resultado : resultados) {
            Map<String, Object> valoracionInfo = new HashMap<>();
            valoracionInfo.put("estrellas", resultado[0]);
            valoracionInfo.put("cantidad", resultado[1]);
            
            distribucion.add(valoracionInfo);
        }
        
        return distribucion;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getValoracionesConConteoAnuncios() {
        List<Object[]> resultados = valoracionDao.findValoracionesConConteoAnuncios();
        List<Map<String, Object>> valoracionesConConteo = new ArrayList<>();
        
        for (Object[] resultado : resultados) {
            Map<String, Object> valoracionInfo = new HashMap<>();
            valoracionInfo.put("id", resultado[0]); // puntuacion será el ID
            valoracionInfo.put("estrellas", resultado[0]);
            valoracionInfo.put("cantidad", resultado[1]);
            
            valoracionesConConteo.add(valoracionInfo);
        }
        
        return valoracionesConConteo;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeValoracionUsuarioAnuncio(Long anuncioId, Long usuarioId) {
        return valoracionDao.existsByAnuncioIdAndUsuarioId(anuncioId, usuarioId);
    }
} 