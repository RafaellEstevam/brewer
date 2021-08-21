package com.algaworks.brewer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.repository.filter.ClienteFilter;
import com.algaworks.brewer.repository.helper.cliente.ClienteRepositoryQueries;

/**
 * @author Rafaell Estevam
 *
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long>, ClienteRepositoryQueries{

	
	Optional<Cliente> findByCpfOuCnpj(String cpfOuCnpj);

	
	
	
}
