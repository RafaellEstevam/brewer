package com.algaworks.brewer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.brewer.model.Cidade;

/**
 * @author Rafaell Estevam
 *
 */
@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {
	
	
	List<Cidade>findByEstadoId(Long codigoEstado);
	
	
}
