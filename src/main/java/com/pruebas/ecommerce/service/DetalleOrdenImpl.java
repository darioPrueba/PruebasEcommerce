package com.pruebas.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pruebas.ecommerce.model.DetalleOrden;
import com.pruebas.ecommerce.repository.IDetalleOrdenRepository;

@Service
public class DetalleOrdenImpl implements IDetalleOrdenService {

	@Autowired
	private IDetalleOrdenRepository detalleOrdenRepository;
	
	@Override
	public DetalleOrden save(DetalleOrden detalleOrden) {

		return detalleOrdenRepository.save(detalleOrden);
	}

}
