package com.pruebas.ecommerce.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pruebas.ecommerce.model.Usuario;
import com.pruebas.ecommerce.service.IUsuarioService;
import com.pruebas.ecommerce.service.UsuarioServiceImpl;

@Controller
@RequestMapping("/usuario")
public class userController {

	private final Logger LOG = LoggerFactory.getLogger(userController.class);
	
	@Autowired
	private IUsuarioService usuarioService;
	
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

	
}
