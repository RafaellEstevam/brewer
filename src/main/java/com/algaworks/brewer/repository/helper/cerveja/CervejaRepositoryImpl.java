package com.algaworks.brewer.repository.helper.cerveja;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.filter.CervejaFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

/**
 * @author Rafaell Estevam
 *
 */
public class CervejaRepositoryImpl implements CervejaRepositoryQueries {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private PaginacaoUtil paginacao;

	@SuppressWarnings("unchecked") // 1
	@Transactional(readOnly = true) // 2
	@Override
	public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable) {

		Criteria criteria = em.unwrap(Session.class).createCriteria(Cerveja.class); // 2 (select c from Cerveja c)

		paginacao.preparar(criteria, pageable); // 14

		aplicarFiltros(filtro, criteria); // 5

		return new PageImpl<Cerveja>(criteria.list(), pageable, total(filtro)); // 4 e 9
	}

	private Long total(CervejaFilter filtro) { // 6
		Criteria criteria = em.unwrap(Session.class).createCriteria(Cerveja.class); // select c from cerveja c
		aplicarFiltros(filtro, criteria);// where c.sku = :sku and...
		criteria.setProjection(Projections.rowCount()); // coloca o count na frente da query

		return (Long) criteria.uniqueResult();

	}

	private void aplicarFiltros(CervejaFilter filtro, Criteria criteria) {// 7
		if (filtro != null) {

			if (!StringUtils.isEmpty(filtro.getSku())) { // where c.sku = :sku
				criteria.add(Restrictions.eq("sku", filtro.getSku()));
			}

			if (!StringUtils.isEmpty(filtro.getNome())) { // where c.nome like concat('%,:nome,'%')
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE)); // 8
			}

			if (isEstiloPresente(filtro)) { // where c.estilo = :estilo
				criteria.add(Restrictions.eq("estilo", filtro.getEstilo()));
			}

			if (filtro.getSabor() != null) { // where c.sabor = :sabor
				criteria.add(Restrictions.eq("sabor", filtro.getSabor()));
			}

			if (filtro.getOrigem() != null) {// where c.origem = :origem
				criteria.add(Restrictions.eq("origem", filtro.getOrigem()));
			}

			if (filtro.getValorDe() != null) { // where c.valor >= :valor (
				criteria.add(Restrictions.ge("valor", filtro.getValorDe())); // ge = greater than or equal
			}

			if (filtro.getValorAte() != null) {// where c.valor <= :valor
				criteria.add(Restrictions.le("valor", filtro.getValorAte())); // le = less than or equal
			}

		}
	}

	private boolean isEstiloPresente(CervejaFilter filtro) {

		return filtro.getEstilo() != null && filtro.getEstilo().getId() != null;
	}
	
}




















/*
 * 
 * 1. É só para tirar o "warning" que fica aparecendo em criteria.list(), por
 * conta do generics. Criteria.list() retorna uma list de nada <?>, por isso
 * fica dando um warning.
 * 
 * 2. A CRITERIA FICA DENTRO DO ENTITY MANAGER, POR ISSO TIVEMOS QUE RECUPERÁ-LA
 * DE LÁ DE DENTRO PARA PODER USÁ-LA. Para usar a criteria precisamos uma
 * transação, mesmo que a operação seja de leitura, por isso usamos
 * o @Transactional(readOnly = true). Resumindo:
 * 
 * 1. A criteria fica dentro do EntityManager; 2. Para usar a criteria,
 * precisamos abrir uma transação, mesmo que a operação seja só de leitura.
 * 
 * 3. (15.8) criteria.setFirstResult() --> a partir de qual registro eu quero
 * que a criteria comece a buscar criteria.setMaxResults() --> total de
 * registros que eu quero por página
 * 
 * Tipo assim: começa no 0 e me puxa 10 a partir dele. 0 =
 * criteria.setFirstResult() 10 = criteria.setMaxResults()
 * 
 * O criteria vai adicionar no final da query SQL o restriction limit ?, ?, onde
 * o 1º '?' é o criteria.setFirstResult() e o 2º é o criteria.setMaxResults().
 * 
 * 4. (15.8) O total() deve retornar a quantidade total de registros que se
 * encaixaram nos filtros aplicados, sem a paginação aplicada. Por isso não pode
 * ser a qtd que existe em de 'criteria.list()', pois nesse ponto a paginação já
 * foi feita e retorna somente os registros de uma página e não todos que
 * correspondem à pesquisa.
 * 
 * 5. Como aqui eu já tenho a paginação aplicada (linha 43 e 44), qdo
 * criteria.list() for executado abaixo, vai retornar somente os registros que
 * corresposndem à pagina solicitada. Diferente do que acontece no método
 * total().
 * 
 * 6. A ideia aqui é calcular a qtd total de registros da consulta com os
 * filtros aplicados.
 * 
 * 7. Adiciona o filtro na query do criteria que é passado pra esse método.
 * 
 * 8. Mathmode.ANYWHERE : se colocarmos "e" na busca, vai retornar tudo q tenha
 * a letra "e" envolvida.
 * 
 * 9. Note a diferença entre PAGEABLE e PAGE. O Page vai levar basicamente 3
 * informações: a lista com os registros daquela página; o Pageable com
 * informações de qual página foi solicitada e quantos registros devem ser
 * exibidos; a quantidade total de registros daquela pesquisa, sem a paginação.
 * Moral da história: o Pageable pode ser colocado dentro do Page.
 * 
 * PROBLEMA A SER RESOLVIDO NA PROXIMA AULA 15.9: Quando fazemos uma pesquisa
 * usando os filtros, ele me traz os resultados, mas se clicarmos em uma das
 * páginas, ele perde a pesquisa feita e me retorna todas as cervejas de novo.
 * Isso pq estamos passando como parâmetros no link de cada página só o page=x.
 * Com isso, o cervejaFilter lá no CervejaController chega vazio e a pesquisa
 * feita anteriormente é zerada. RESUMINDO: temos que mandar na requisição o
 * page para contruir o objeto Pageable e os os atributos necessários para
 * construir o objeto cervejaFilter quando a requisição chegar no controller.
 * Assim teremos a paginação funcionando junto com o filtro, pois os dois
 * objetos envolvidos nesses processos serão preenchidos com informações.
 * 
 * 
 * 
 * 10. O Pageable tbm tem o "sort" dentro dele. Se a requisição chegar no
 * controller sem a informação do sort, esse sort vai como null no obj pageable.
 * Por isso o if aqui.
 * 
 * 11.1. "pageable.getSort();" pega o cj de sorts que foram passados na URL.
 * Podemos ter mais de um. Ex: sort=nome,asc&sort=sku,desc. Aqui temos dois
 * sorts que foram enviados. O getSort() pega os dois. Para pegar a primeira
 * "dupla" de sort, temos que usar o iterator. Cada dupla é um "order". veja
 * item 11.
 * 
 * 11. Pegamos a próxima dupla de ordenação que estava dentro do pageable
 * (dentro de "pageable.getSort();" pois podemos passar vários duplas de
 * ordenação na url ). Cada dupla é um "order", formado pelo campo que queremos
 * ordenar e pela direction se é asc ou desc (sort= nome, asc).
 * 
 * 12. Após o item 11, o que temos dentro de "order" é a primeira dupla de
 * ordenação com os seguintes atributos: property e asc. Através de
 * getProperty() estamos pegando por qual atributo queremos ordenar. Aí é só
 * chamar o criteria e adicionar o criterio de ordenação na query.
 * 
 * 13. Cuidado com as diferenças: o "Order" aqui é do hibernate e o "Order" do
 * item 11 é do Spring (org.springframework.data.domain.Sort.Order). No item 11
 * colocamos daquele jeito para não dar conflito com o order do hibernate.
 * 
 * 
 * 
 * ## OBSERVAÇÕES IMPORTANTES A RESPEITO DE PAGINAÇÃO E ORDENAÇÃO (15.14):
 * 
 * Todos os links de paginação e ordenação são requisições feitas através da
 * URL. As URLs são montadas por métodos (aqui estão na classe PageWrapper) e
 * sempre vão levar os parâmetros da última requisição. O método que monta a URL
 * do link de paginação e o que monta a URL do link de ordenação devem usar
 * diferentes uriBuilders, caso contrário a URL de um sempre vai levar os
 * parâmetros específicos do outro no final.
 * 
 * 
 * 14.(15.18) Como essa parte estava se repetindo aqui e em
 * EstiloRepositoryImpl.java, preferimos jogar esse código repetido para a
 * classe PaginacaoUtil.java. A anotamos com um @Component pois assim o Spring
 * vai instanciá-la quando a aplicação subir, a deixará em memória e poderá ser
 * chamada em outras classes,através de um @Autowired, se precisarmos. Vamos
 * chamar esse método lá em EstiloRepositoryImpl.java também. Agora sempre que eu
 * precisar fazer paginação e ordenacao, é só adicionar esse método na classe.
 * 
 * 
 * 
 * 
 * 
 */
