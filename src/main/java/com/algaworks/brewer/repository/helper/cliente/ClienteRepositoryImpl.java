package com.algaworks.brewer.repository.helper.cliente;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Sort;

import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.repository.filter.ClienteFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

/**
 * @author Rafaell Estevam
 *
 */
public class ClienteRepositoryImpl implements ClienteRepositoryQueries {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Cliente> filtrar(ClienteFilter clienteFilter, Pageable pageable) {
		//select c from Cliente c where c.nome like concat('%',:nome,'%') and c.cpfOuCnpj = ?
		
		Criteria criteria = em.unwrap(Session.class).createCriteria(Cliente.class);
		
		paginacaoUtil.preparar(criteria, pageable);
		
		aplicarFiltros(clienteFilter, criteria);
		
		criteria.createAlias("endereco.cidade", "c", JoinType.LEFT_OUTER_JOIN); //1
		criteria.createAlias("c.estado", "e", JoinType.LEFT_OUTER_JOIN); //1
		
		
		return new PageImpl<Cliente>(criteria.list(), pageable, total(clienteFilter));
	}


	private void aplicarFiltros(ClienteFilter clienteFilter, Criteria criteria) {
		if(clienteFilter != null) {
			
			//c.nome like concat('%',:nome,'%') 
			if(!StringUtils.isEmpty(clienteFilter.getNome())) {
				criteria.add(Restrictions.ilike("nome", clienteFilter.getNome(), MatchMode.ANYWHERE));
			}
			
			//c.cpfOuCnpj = ?
			if(!StringUtils.isEmpty(clienteFilter.getCpfOuCnpj())) {
				criteria.add(Restrictions.eq("cpfOuCnpj", clienteFilter.getCpfOuCnpj()));
			}
			
		}
	}
	
	
	private long total(ClienteFilter clienteFilter) {
		
		Criteria criteria = em.unwrap(Session.class).createCriteria(Cliente.class);
		
		aplicarFiltros(clienteFilter, criteria);
		
		criteria.setProjection(Projections.rowCount());
				 
		return (long) criteria.uniqueResult();
 
	}
	

}
























/*
1.(16.13) Tivemos que fazer um join de cliente com cidade e estado pois, por outras razões, tivemos que colocar cidade.estado como fetch.lazy 
(não queríamos que as informaões do estado fossem buscadas quando uma cidade fosse carregada). Mas com essa decisão, toda vez que buscavamos
os clientes ele dava um lazy initialization exception, já que não podia buscar o estado. Para não termos que tirar o fetch.lazy que colocamos, 
a opção foi usar o join.

 */