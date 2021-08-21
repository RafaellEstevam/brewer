package com.algaworks.brewer.repository.helper.cerveja;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.filter.CervejaFilter;

/**
 * @author Rafaell Estevam
 *
 */
public interface CervejaRepositoryQueries {

	
	Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable);
	
	
}


/*
A implementação desse método está em CervejaRepositoryImpl. 


 */
 