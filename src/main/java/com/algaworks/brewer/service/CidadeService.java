package com.algaworks.brewer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Cidade;
import com.algaworks.brewer.repository.CidadeRepository;
import com.algaworks.brewer.service.exception.NomeCidadeJaCadastradoException;

/**
 * @author Rafaell Estevam
 *
 */
@Service
public class CidadeService {

	@Autowired
	private CidadeRepository cidadeRepository;

	@Transactional
	public void salvar(Cidade cidade) {

		Optional<Cidade> cidadeProcurada = cidadeRepository.findByNomeIgnoreCaseAndEstado(cidade.getNome(), cidade.getEstado());

		if (cidadeProcurada.isPresent()) {
			throw new NomeCidadeJaCadastradoException("Cidade já cadastrada!");
		}

		cidadeRepository.save(cidade);

	}

}
