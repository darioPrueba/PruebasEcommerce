package com.pruebas.ecommerce.service;

import java.util.List;

import com.pruebas.ecommerce.model.Orden;

public interface IOrdenService {

	public List <Orden> findAll();
	public Orden save (Orden orden);
	public String generarNumeroOrden();
	
}
