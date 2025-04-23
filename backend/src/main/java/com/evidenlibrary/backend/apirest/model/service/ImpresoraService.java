package com.evidenlibrary.backend.apirest.model.service;

import java.util.List;

import com.evidenlibrary.backend.apirest.model.entity.Impresora;

public interface ImpresoraService {

	public List<Impresora> findAll();
	
	public Impresora findById(Long id);
	
	public Impresora findByAnuncioId(Long id);
	
	public Impresora save(Impresora impresora);
	
	public void delete(Impresora impresora);
	
	public void deleteAll();

}
