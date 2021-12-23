package com.pruebas.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pruebas.ecommerce.model.Orden;
import com.pruebas.ecommerce.model.Usuario;

public interface IOrdenRepository extends JpaRepository <Orden, Integer> {

	
	List<Orden> findByUsuario(Usuario usuario);
	
}
