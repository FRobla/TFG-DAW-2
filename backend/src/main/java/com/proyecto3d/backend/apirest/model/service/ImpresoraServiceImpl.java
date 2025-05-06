package com.proyecto3d.backend.apirest.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto3d.backend.apirest.model.dao.ImpresoraDao;
import com.proyecto3d.backend.apirest.model.entity.Impresora;

/**
 * Implementación del servicio de gestión de impresoras
 */
@Service
public class ImpresoraServiceImpl implements ImpresoraService {

    @Autowired
    private ImpresoraDao impresoraDao;

    /**
     * Obtiene todas las impresoras
     */
    @Override
    @Transactional(readOnly = true)
    public List<Impresora> findAll() {
        return (List<Impresora>) impresoraDao.findAll();
    }

    /**
     * Busca una impresora por su ID
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Impresora> findById(Long id) {
        return impresoraDao.findById(id);
    }

    /**
     * Guarda o actualiza una impresora
     */
    @Override
    @Transactional
    public Impresora save(Impresora impresora) {
        return impresoraDao.save(impresora);
    }

    /**
     * Elimina una impresora por su ID
     */
    @Override
    @Transactional
    public void delete(Long id) {
        impresoraDao.deleteById(id);
    }
} 