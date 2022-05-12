package com.pruebas.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.pruebas.ecommerce.model.Orden;
import com.pruebas.ecommerce.model.Usuario;

public interface IOrdenService {

	
	List <Orden> findAll();
	Orden save (Orden orden);
	String generarNumeroOrden();
	List<Orden> findByUsuario(Usuario usuario);
	Optional <Orden> findById(Integer id);

}
