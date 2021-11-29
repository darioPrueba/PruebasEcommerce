package com.pruebas.ecommerce.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pruebas.ecommerce.model.Producto;
import com.pruebas.ecommerce.model.Usuario;
import com.pruebas.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	@Autowired
	private ProductoService productoService;
	@Autowired
	private Usuario u2;
	
	@GetMapping("")
	public String show(Model model) {
		
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
		
	}

	@GetMapping("/create")
	public String create() {
		
		return "productos/create";
	}

	@PostMapping("/save")
	public String save(Producto producto) {

		LOGGER.info("Este es el objeto producto {}", producto);
		//Usuario u = new Usuario (1, null, null, null, null, null, null, null);
		u2.setId(1);
		
		producto.setUsuario(u2);
		productoService.save(producto);
		
		return "redirect:/productos";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Producto p = new Producto();
		Optional<Producto> optionalProducto = productoService.get(id);
		p = optionalProducto.get();
		LOGGER.info("Producto buscado : {}", p );
		model.addAttribute("producto", p);
		return "productos/edit";
		
	}
	
	@PostMapping("/update")
	public String update(Producto producto) {
		productoService.update(producto);
		
		return "redirect:/productos";
	}
}