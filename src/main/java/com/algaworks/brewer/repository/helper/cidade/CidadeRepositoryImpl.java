package com.algaworks.brewer.repository.helper.cidade;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Cidade;
import com.algaworks.brewer.repository.filter.CidadeFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

/**
 * @author Rafaell Estevam
 *
 */
public class CidadeRepositoryImpl implements CidadeRepositoryQueries {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private PaginacaoUtil paginacao;
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Cidade> filtrar(CidadeFilter cidadeFilter, Pageable pageable) {
		//select c from Cidade c where c.estado = :estado and c.nome = :nome;
		
		Criteria criteria = em.unwrap(Session.class).createCriteria(Cidade.class);
		
		paginacao.preparar(criteria, pageable);
		
		aplicarFiltros(cidadeFilter, criteria);
		
		criteria.createAlias("estado", "e", JoinType.LEFT_OUTER_JOIN);
		
		return new PageImpl<Cidade>(criteria.list(), pageable, total(cidadeFilter));
	}


	private long total(CidadeFilter cidadeFilter) {
		Criteria criteria = em.unwrap(Session.class).createCriteria(Cidade.class);
		
		aplicarFiltros(cidadeFilter, criteria);
		
		criteria.setProjection(Projections.rowCount());
		
		
		return (long) criteria.uniqueResult();
	}



	private void aplicarFiltros(CidadeFilter cidadeFilter, Criteria criteria) {
		if(cidadeFilter != null) {
			
			
			if(cidadeFilter.getEstado() != null) {
				criteria.add(Restrictions.eq("estado", cidadeFilter.getEstado()));
			}
			
			if(!StringUtils.isEmpty(cidadeFilter.getNome())) {
				criteria.add(Restrictions.eq("nome", cidadeFilter.getNome()));
			}

		}
	}

}
