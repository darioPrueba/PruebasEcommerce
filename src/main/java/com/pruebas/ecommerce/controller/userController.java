package com.pruebas.ecommerce.controller;

import java.util.List;
import java.util.Optional;

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

import com.pruebas.ecommerce.model.Orden;
import com.pruebas.ecommerce.model.Usuario;
import com.pruebas.ecommerce.service.IOrdenService;
import com.pruebas.ecommerce.service.IUsuarioService;
import com.pruebas.ecommerce.service.UsuarioServiceImpl;

@Controller
@RequestMapping("/usuario")
public class userController {

	private final Logger LOG = LoggerFactory.getLogger(userController.class);
	
	@Autowired
	private IUsuarioService usuarioService;
	@Autowired
	private IOrdenService ordenService;
	
	@GetMapping("/registro")
	public String create() {
				
		return "usuario/registro";
	}
	
	@PostMapping("/save")
	public String save(Usuario usuario) {
		
	LOG.info("Usuario registro: {}", usuario);

	usuario.setTipo("USER");
	usuarioService.save(usuario);
	return "redirect:/";
	}

	@RequestMapping("/login")
	public String login() {
		
		return "usuario/login";
	}

	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {

		LOG.info("Accesos : {}", usuario);		
		Optional<Usuario> user = usuarioService.findByEmail(usuario.getEmail());
		LOG.info("Usuario de base de datos: {}", user.get());	
		
		if(user.isPresent()) {
			session.setAttribute("idusuario", user.get().getId());
			if(user.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			}else {
				return "redirect:/";
			}
		}else {
			
			LOG.info("Usuario no existe");
			
		}
		
		return "redirect:/";
		
			}
	
	@GetMapping("/compras")
	public String obtenerCompras(HttpSession session, Model model) {
		
		Usuario u = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		List<Orden> ordenes = ordenService.findByUsuario(u);
		model.addAttribute("ordenes", ordenes);
		return "usuario/compras";
		
	}

	@GetMapping("/detalle/{id}")
	public String detalleCompra(@PathVariable Integer id, HttpSession session, Model model) {
		
		Optional <Orden> orden = ordenService.findById(id);
		LOG.info("Id de la orden: {}", id);

		model.addAttribute("detalles", orden.get().getDetalle());
		// Session
		model.addAttribute("session", session.getAttribute("idusuario"));
		//return "usuario/compras";
		return "usuario/detallecompra";
		
	}
	
	@GetMapping("/cerrar")
	public String cerrarSesion(HttpSession session){
		
		session.removeAttribute("idusuario");
		return "redirect:/";
		
		
	}
}
