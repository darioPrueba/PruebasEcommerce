package com.pruebas.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.pruebas.ecommerce.model.Orden;
import com.pruebas.ecommerce.model.Usuario;

public interface IOrdenService {

	public List <Orden> findAll();
	public Orden save (Orden orden);
	public String generarNumeroOrden();
	List<Orden> findByUsuario(Usuario usuario);
	public Optional <Orden> findById(Integer id);

}
