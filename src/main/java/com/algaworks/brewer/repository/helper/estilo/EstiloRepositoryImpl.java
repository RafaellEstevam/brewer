package com.algaworks.brewer.repository.helper.estilo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.filter.EstiloFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

/**
 * @author Rafaell Estevam
 *
 */
public class EstiloRepositoryImpl implements EstiloRepositoryQueries {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private PaginacaoUtil paginacao;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Estilo> filtrar(EstiloFilter estiloFilter, Pageable pageable) {
		Criteria criteria = em.unwrap(Session.class).createCriteria(Estilo.class); // select e from Estilo e

		paginacao.preparar(criteria, pageable); // 1

		aplicarFiltros(estiloFilter, criteria);

		return new PageImpl<Estilo>(criteria.list(), pageable, total(estiloFilter));
	}

	private void aplicarFiltros(EstiloFilter estiloFilter, Criteria criteria) {
		if (estiloFilter != null) {

			if (!StringUtils.isEmpty(estiloFilter.getNome())) {
				criteria.add(Restrictions.ilike("nome", estiloFilter.getNome(), MatchMode.ANYWHERE)); // (where) e.nome
																										// like
																										// concat('%,:nome,'%')
			}
		}
	}

	private Long total(EstiloFilter estiloFilter) {
		Criteria criteria = em.unwrap(Session.class).createCriteria(Estilo.class);

		aplicarFiltros(estiloFilter, criteria);

		criteria.setProjection(Projections.rowCount());

		return (Long) criteria.uniqueResult();

	}

}

/*
 * 1.(15.18) Como essa parte estava se repetindo aqui e em
 * EstiloRepositoryImpl.java, preferimos jogar esse c??digo repetido para a
 * classe PaginacaoUtil.java. A anotamos com um @Component pois assim o Spring
 * vai instanci??-la quando a aplica????o subir, a deixar?? em mem??ria e poder?? ser
 * chamada em outras classes,atrav??s de um @Autowired, se precisarmos. Vamos
 * chamar esse m??todo l?? em EstiloRepositoryImpl.java tamb??m. Agora sempre que
 * eu precisar fazer pagina????o e ordenacao, ?? s?? adicionar esse m??todo na
 * classe.
 */
