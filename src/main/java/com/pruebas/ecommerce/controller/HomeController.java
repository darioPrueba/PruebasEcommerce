package com.pruebas.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pruebas.ecommerce.model.DetalleOrden;
import com.pruebas.ecommerce.model.Orden;
import com.pruebas.ecommerce.model.Producto;
import com.pruebas.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {

	private final Logger LOG = LoggerFactory.getLogger(HomeController.class);;

	@Autowired
	private ProductoService productoService;

	// Para almacenar los detalles de cada orden
	private List<DetalleOrden> detalles = new ArrayList<>();

	// Almacena los datos de la orden
	private Orden orden = new Orden();

	@GetMapping("")
	public String home(Model model) {

		model.addAttribute("productos", productoService.findAll());
		return "usuario/home";
	}

	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {

		LOG.info("Id del producto enviado como parámetro {}", id);
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id);
		producto = productoOptional.get();
		model.addAttribute("producto", producto);
		return "usuario/productohome";

	}

	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {

		DetalleOrden detalleOrden = new DetalleOrden();
		Producto p = new Producto();
		double sumaTotal = 0;

		Optional<Producto> optionalProducto = productoService.get(id);
		p = optionalProducto.get();
		// p = productoService.get(id).get();

		LOG.info("Producto añadido: {}", optionalProducto.get());
		LOG.info("Cantidad {}", cantidad);

		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(p.getPrecio());
		detalleOrden.setNombre(p.getNombre());
		detalleOrden.setTotal(p.getPrecio() * cantidad);
		detalleOrden.setProducto(p);

		// controlar que el producto solo se añada 1 vez
		Integer idProducto = p.getId();
		boolean ingresado = detalles.stream().anyMatch(prod -> prod.getProducto().getId() == idProducto);

		if (!ingresado) {
			detalles.add(detalleOrden);
		}

		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		orden.setTotal(sumaTotal);

		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "usuario/carrito";
	}

	// Método para quitar productos del carrito
	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model) {

		List<DetalleOrden> ordenesNueva = new ArrayList<>();

		for (DetalleOrden detalleOrden : detalles) {
			if (detalleOrden.getProducto().getId() != id) {
				// if(detalleOrden.getId()!=id) {
				ordenesNueva.add(detalleOrden);
			}
		}
		detalles = ordenesNueva;

		double sumaTotal = 0;
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);

		return "usuario/carrito";

	}

	@GetMapping("/getCart")
	public String getCart(Model model) {

		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "usuario/carrito";
	}
	
	@GetMapping("/order")
	public String resumenOrden () {
	
	return "usuario/resumenorden";	
	}
	
}
