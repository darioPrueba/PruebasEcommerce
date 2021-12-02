package com.pruebas.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.pruebas.ecommerce.model.Producto;
import com.pruebas.ecommerce.model.Usuario;
import com.pruebas.ecommerce.service.ProductoService;
import com.pruebas.ecommerce.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	@Autowired
	private ProductoService productoService;
	@Autowired
	private Usuario u2;
	@Autowired
	private UploadFileService upload;
	
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
	public String save(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {

		LOGGER.info("Este es el objeto producto {}", producto);
		//Usuario u = new Usuario (1, null, null, null, null, null, null, null);
		u2.setId(1);
		
		// imagen
		if(producto.getId()==null) {// esto es cuando se crea el producto
			String nombreImagen= upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}else {
			
		}
		
		
		
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
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {

		Producto p = new Producto();
		p=productoService.get(producto.getId()).get();
		
		if(file.isEmpty()) {// si editamos el producto manteniendo la imagen
			producto.setImagen(p.getImagen());	
		} else { // al editar también la imagen			
			if(!p.getImagen().equals("default.jpg")) {
				upload.deleteImage(p.getImagen());
			}
			
			String nombreImagen= upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);
		
		return "redirect:/productos";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {

		Producto p = productoService.get(id).get();
		
		// antes de eliminar la imagen verifica que no sea la cargada por defecto
		if(!p.getImagen().equals("default.jpg")) {
			upload.deleteImage(p.getImagen());
		}
		
		productoService.delete(id);
		return "redirect:/productos";
		
		
	}
	
	
}