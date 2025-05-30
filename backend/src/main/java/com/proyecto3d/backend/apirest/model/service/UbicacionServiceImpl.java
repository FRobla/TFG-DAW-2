package com.proyecto3d.backend.apirest.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto3d.backend.apirest.model.dao.UbicacionDao;
import com.proyecto3d.backend.apirest.model.entity.Ubicacion;

@Service
public class UbicacionServiceImpl implements UbicacionService {
	
	@Autowired
	private UbicacionDao ubicacionDao;

	@Override
	@Transactional(readOnly = true)
	public List<Ubicacion> findAll() {
		return (List<Ubicacion>) ubicacionDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Ubicacion findById(Long id) {
		return ubicacionDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Ubicacion> findByActivoTrue() {
		return ubicacionDao.findByActivoTrue();
	}

	@Override
	@Transactional
	public Ubicacion save(Ubicacion ubicacion) {
		return ubicacionDao.save(ubicacion);
	}

	@Override
	@Transactional
	public void delete(Ubicacion ubicacion) {
		// Primero desasociar la ubicación de todos los usuarios
		if (ubicacion.getUsuarios() != null) {
			ubicacion.getUsuarios().forEach(usuario -> {
				usuario.setUbicacion(null);
			});
		}
		ubicacionDao.delete(ubicacion);
	}
	
    @Override
    @Transactional
    public void deleteAll() {
        // Obtener todas las ubicaciones
        List<Ubicacion> ubicaciones = findAll();
        
        // Para cada ubicación, desasociar todos sus usuarios
        for (Ubicacion ubicacion : ubicaciones) {
            if (ubicacion.getUsuarios() != null) {
                ubicacion.getUsuarios().forEach(usuario -> {
                    usuario.setUbicacion(null);
                });
            }
        }
        
        // Eliminar todas las ubicaciones
        ubicacionDao.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getUbicacionesConConteoAnuncios() {
        List<Object[]> resultados = ubicacionDao.findUbicacionesConConteoAnuncios();
        List<Map<String, Object>> ubicacionesConConteo = new ArrayList<>();
        
        for (Object[] resultado : resultados) {
            Map<String, Object> ubicacionInfo = new HashMap<>();
            ubicacionInfo.put("id", resultado[0]);
            ubicacionInfo.put("nombre", resultado[1]);
            ubicacionInfo.put("cantidad", resultado[2]);
            
            ubicacionesConConteo.add(ubicacionInfo);
        }
        
        return ubicacionesConConteo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ubicacion> findByTerm(String term) {
        return ubicacionDao.findByTerm(term);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ubicacion> findUbicacionesConAnuncios() {
        return ubicacionDao.findUbicacionesConAnuncios();
    }
} 