package com.pruebas.ecommerce.service;

import java.util.Optional;

import com.pruebas.ecommerce.model.Usuario;

public interface IUsuarioService {

	public Optional <Usuario> findById(Integer id);
	
	
}
