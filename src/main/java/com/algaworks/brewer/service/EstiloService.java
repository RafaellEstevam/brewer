package com.algaworks.brewer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.EstiloRepository;
import com.algaworks.brewer.service.exception.NomeEstiloJaCadastradoException;

/**
 * @author Rafaell Estevam
 *
 */
@Service
public class EstiloService {
	
	
	@Autowired
	EstiloRepository estiloRepository;
	
	@Transactional
	public Estilo salvar(Estilo estilo) { //1
		
		Optional<Estilo> estiloOptional = estiloRepository.findByNomeIgnoreCase(estilo.getNome());
			
		if(estiloOptional.isPresent()) {
			throw new NomeEstiloJaCadastradoException("Nome do estilo já cadastrado");
		}
		
		return estiloRepository.saveAndFlush(estilo);//1
		
	}
	
	
	
}


/*
1.  'saveAndFlush(estilo)' salve no BD e retorne o objeto persistido( com a info do ID que foi gerado). 
	Tivemos que retornar um estilo que foi persistido para o controller, para ele devolver para o javascript e popular a lista.

*/