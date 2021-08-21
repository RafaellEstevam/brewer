package com.algaworks.brewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.algaworks.brewer.model.Usuario;

/**
 * @author Rafaell Estevam
 *
 */
@Controller
public class UsuarioController {
	
	@RequestMapping("/usuario/novo")
	public String novo(Usuario usuario) {
		return "usuario/CadastroUsuario";
	}
}
