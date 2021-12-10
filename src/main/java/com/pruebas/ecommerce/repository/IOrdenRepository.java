package com.pruebas.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pruebas.ecommerce.model.Orden;

public interface IOrdenRepository extends JpaRepository <Orden, Integer> {

}
