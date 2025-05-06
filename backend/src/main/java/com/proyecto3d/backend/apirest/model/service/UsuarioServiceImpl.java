package com.proyecto3d.backend.apirest.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto3d.backend.apirest.model.dao.UsuarioDao;
import com.proyecto3d.backend.apirest.model.entity.Usuario;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioDao usuarioDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findAll() {
		return (List<Usuario>) usuarioDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findById(Long id) {
		return usuarioDao.findById(id).orElse(null);
	}
	
    @Override
    @Transactional(readOnly = true)
    public Usuario findByEmail(String email) {
        return usuarioDao.findByEmail(email);
    }

	@Override
	@Transactional
	public Usuario save(Usuario usuario) {
		return usuarioDao.save(usuario);
	}

	@Override
	@Transactional
	public void delete(Usuario usuario) {
		//Eliminar los anuncios del usuario
		usuario.getAnuncios().forEach(anuncio -> anuncio.setUsuario(null));
		
		// Eliminar el usuario
	    usuarioDao.delete(usuario);
	}

}
