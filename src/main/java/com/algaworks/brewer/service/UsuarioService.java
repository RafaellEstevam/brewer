package com.algaworks.brewer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Usuario;
import com.algaworks.brewer.repository.UsuarioRepository;
import com.algaworks.brewer.service.exception.EmailJaCadastradoException;
import com.algaworks.brewer.service.exception.SenhaObrigatoriaUsuarioException;

/**
 * @author Rafaell Estevam
 *
 */
@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public void salvar(Usuario usuario) {

		Optional<Usuario> usuarioProcurado = usuarioRepository.findByEmail(usuario.getEmail());

		if (usuarioProcurado.isPresent()) {
			throw new EmailJaCadastradoException("E-mail já cadastrado!");
		}

		if(usuario.isNovo() && StringUtils.isEmpty(usuario.getSenha())) {
			throw new SenhaObrigatoriaUsuarioException("Senha é obrigatória para novo usuário");
		}
		
		
		if(usuario.isNovo()) {
			usuario.setSenha(this.passwordEncoder.encode(usuario.getSenha()));
			usuario.setConfirmacaoSenha(usuario.getSenha()); //1
		}
		
		usuarioRepository.save(usuario);

	}

}


/*
1.(18.9) Tivemos que colocar a nova senha na 'confirmação senha' também pois quando chega na JPA para 
fazer a persistência, antes ela valida novamente os atrbutos. Estava dando erro sem esse código 
pois ele batia no '@atributoConfirmação' e via que elas eram diferentes.
 */
