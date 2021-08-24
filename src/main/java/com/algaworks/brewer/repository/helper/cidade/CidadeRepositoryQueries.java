package com.algaworks.brewer.repository.helper.cidade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.algaworks.brewer.model.Cidade;
import com.algaworks.brewer.repository.filter.CidadeFilter;

/**
 * @author Rafaell Estevam
 *
 */
public interface CidadeRepositoryQueries {
	
	Page<Cidade>filtrar(CidadeFilter cidadeFilter, Pageable pageable);
	
	
}
