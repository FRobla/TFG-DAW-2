package com.proyecto3d.backend.apirest.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto3d.backend.apirest.model.dao.MaterialDao;
import com.proyecto3d.backend.apirest.model.entity.Material;

@Service
public class MaterialServiceImpl implements MaterialService {
	
	@Autowired
	private MaterialDao materialDao;

	@Override
	@Transactional(readOnly = true)
	public List<Material> findAll() {
		return (List<Material>) materialDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Material findById(Long id) {
		return materialDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Material> findByActivoTrue() {
		return materialDao.findByActivoTrue();
	}

	@Override
	@Transactional
	public Material save(Material material) {
		return materialDao.save(material);
	}

	@Override
	@Transactional
	public void delete(Material material) {
		// Primero desasociar el material de todos los anuncios
		if (material.getAnuncios() != null) {
			material.getAnuncios().forEach(anuncio -> {
				anuncio.getMateriales().remove(material);
			});
		}
		materialDao.delete(material);
	}
	
    @Override
    @Transactional
    public void deleteAll() {
        // Obtener todos los materiales
        List<Material> materiales = findAll();
        
        // Para cada material, desasociar todos sus anuncios
        for (Material material : materiales) {
            if (material.getAnuncios() != null) {
                material.getAnuncios().forEach(anuncio -> {
                    anuncio.getMateriales().remove(material);
                });
            }
        }
        
        // Eliminar todos los materiales
        materialDao.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMaterialesConConteoAnuncios() {
        List<Object[]> resultados = materialDao.findMaterialesConConteoAnuncios();
        List<Map<String, Object>> materialesConConteo = new ArrayList<>();
        
        for (Object[] resultado : resultados) {
            Map<String, Object> materialInfo = new HashMap<>();
            materialInfo.put("id", resultado[0]);
            materialInfo.put("nombre", resultado[1]);
            materialInfo.put("cantidad", resultado[2]);
            
            materialesConConteo.add(materialInfo);
        }
        
        return materialesConConteo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> findByTerm(String term) {
        return materialDao.findByTerm(term);
    }
} 