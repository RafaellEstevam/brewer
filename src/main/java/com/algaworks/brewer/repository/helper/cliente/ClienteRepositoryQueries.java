package com.algaworks.brewer.repository.helper.cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.repository.filter.ClienteFilter;

/**
 * @author Rafaell Estevam
 *
 */
public interface ClienteRepositoryQueries {

	
	Page<Cliente> filtrar(ClienteFilter clienteFilter, Pageable pageable);
	
}
