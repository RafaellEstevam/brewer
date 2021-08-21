package com.algaworks.brewer.repository.helper.estilo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.filter.EstiloFilter;

/**
 * @author Rafaell Estevam
 *
 */
public interface EstiloRepositoryQueries {

	Page<Estilo>filtrar(EstiloFilter estiloFilter, Pageable pageable);
}
