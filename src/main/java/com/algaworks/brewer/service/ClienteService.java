package com.algaworks.brewer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.repository.ClienteRepository;
import com.algaworks.brewer.service.exception.CpfCnpjJaCadastradoException;

/**
 * @author Rafaell Estevam
 *
 */
@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Transactional
	public void salvar(Cliente cliente) {

		Optional<Cliente> clienteExistente = clienteRepository.findByCpfOuCnpj(cliente.getCpfCnpjSemFormatacao()); // 1

		if (clienteExistente.isPresent()) {
			throw new CpfCnpjJaCadastradoException("CPF/CNPJ já cadastrado");
		}

		clienteRepository.save(cliente);
	}

}





















/*
 * 1. Estava buscando pelo Cpf/Cnpj com formatação. Como no BD está sem, não
 * estava dando certo. Por isso criamos esse método para retornar o valor sem 
 * a formatação.
 *
 */