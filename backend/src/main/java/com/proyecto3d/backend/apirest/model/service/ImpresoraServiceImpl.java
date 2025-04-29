package com.proyecto3d.backend.apirest.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto3d.backend.apirest.model.dao.ImpresoraDao;
import com.proyecto3d.backend.apirest.model.entity.Impresora;

@Service
public class ImpresoraServiceImpl implements ImpresoraService {
	@Autowired
	private ImpresoraDao impresoraDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Impresora> findAll() {
		return (List<Impresora>) impresoraDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Impresora findById(Long id) {
		return impresoraDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Impresora findByAnuncioId(Long anuncioId) {
        return impresoraDao.findAll().stream()
            .filter(i -> i.getAnuncios() != null && i.getAnuncios().stream().anyMatch(a -> a.getId().equals(anuncioId)))
            .findFirst().orElse(null);
	}
	
	@Override
	@Transactional
	public Impresora save(Impresora impresora) {
		return impresoraDao.save(impresora);
	}
	
	@Override
	@Transactional
	public void delete(Impresora impresora) {
        // Simplemente elimina la impresora, ya que la relación es ManyToOne
        impresoraDao.delete(impresora);
	}
	
	@Override
    @Transactional
    public void deleteAll() {
        impresoraDao.deleteAll();
    }
}