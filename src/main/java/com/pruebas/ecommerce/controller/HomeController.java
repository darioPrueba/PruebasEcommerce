package com.pruebas.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

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
import com.pruebas.ecommerce.model.Usuario;
import com.pruebas.ecommerce.service.IUsuarioService;
import com.pruebas.ecommerce.service.IDetalleOrdenService;
import com.pruebas.ecommerce.service.IOrdenService;
import com.pruebas.ecommerce.service.IProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
 
	private final Logger LOG = LoggerFactory.getLogger(HomeController.class);;

	@Autowired
	private IProductoService productoService;	
	@Autowired
	private IUsuarioService usuarioService;
	@Autowired
	private IOrdenService ordenService;
	@Autowired 
	private IDetalleOrdenService detalleOrdenService;
	
	// Para almacenar los detalles de cada orden
	private List<DetalleOrden> detalles = new ArrayList<>();

	// Almacena los datos de la orden
	private Orden orden = new Orden();

	@GetMapping("")
	public String home(Model model, HttpSession session) {

		LOG.info("Sesion del usuario: {}", session.getAttribute("idusuario"));
		model.addAttribute("productos", productoService.findAll());
		
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		
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
	public String getCart(Model model, HttpSession session) {

		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);

		model.addAttribute("sesion", session.getAttribute("idusuario"));
		
		return "usuario/carrito";
	}
	
	@GetMapping("/order")
	public String resumenOrden (Model model, HttpSession session) {
	
	Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();	
		
	model.addAttribute("cart", detalles);
	model.addAttribute("orden", orden);
	model.addAttribute("usuario", usuario);
		
	return "usuario/resumenorden";	
	}
	
	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session) {
		
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());
		
		// Usuario // OJO que estoy pasando un número fijo
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();		
		
		orden.setUsuario(usuario);
		ordenService.save(orden);
		
		// Guardar detalle de la orden
		
		for (DetalleOrden detalleOrden : detalles) {
			detalleOrden.setOrden(orden);
			detalleOrdenService.save(detalleOrden);
		}
		
		/// limpiar lista y orden
		
		orden = new Orden();
		detalles.clear();
		
		return "redirect:/";
	}

	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre, Model model) {
		
		LOG.info("Nombre del producto: {}", nombre);		
		
		List<Producto> productos = productoService.findAll().stream().filter( p -> p.getNombre().contains(nombre)).collect(Collectors.toList());
		
		model.addAttribute("productos", productos);
		
		
		return "usuario/home";
		
		
		
	}
	
}
