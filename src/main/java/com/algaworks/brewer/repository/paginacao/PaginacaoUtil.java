package com.algaworks.brewer.repository.paginacao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * @author Rafaell Estevam
 *
 */
@Component // ATENÇÃO! LEIA OS COMENTÁRIOS ABAIXO SOBRE ESSE @Component. Precisamos fazer
			// com que o Spring o encontre, adicionando uma anotação lá em JPAConfig
public class PaginacaoUtil {

	public void preparar(Criteria criteria, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int quantidadeRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroApuxar = paginaAtual * quantidadeRegistrosPorPagina;

		criteria.setMaxResults(quantidadeRegistrosPorPagina); // 3
		criteria.setFirstResult(primeiroRegistroApuxar); // 3

		Sort sort = pageable.getSort();// 11.1

		if (sort != null) { // 10
			Sort.Order order = sort.iterator().next();// 11
			String property = order.getProperty();// 12
			criteria.addOrder(order.isAscending() ? Order.asc(property) : Order.desc(property)); // 13

		}
	}

}




















/*
 * Ver os comentários em CervejaRepositoryImpl.java. Como essa parte estava se
 * repetindo em CervejaRepositoryImpl.java e EstiloRepositoryImpl.java,
 * preferimos jogar esse código repetido para essa classe. A anotamos com
 * um @Component pois assim o Spring vai instanciá-la quando a aplicação subir,
 * a deixará em memória e poderá ser chamada em outras classes,através de
 * um @Autowired, se precisarmos. Vamos chamar esse método lá em
 * CervejaRepositoryImpl.java e EstiloRepositoryImpl.java. Agora sempre que eu
 * precisar fazer paginação e ordenacao, é só adicionar esse método na classe.
 * 
 * SOBRE O @Component: Já sabemos que quando colocamos essa anotação numa classe
 * o Spring vai instanciá-la quando a aplicação subir e dexá-la disponível para
 * ser chamada em outras classes, através de um @Autowired. Mas para isso o
 * Spring precisa saber que essa classe existe. Lembra que a leitura da
 * aplicação começa nos arquivos de configuração(@Configuration)? Então, perceba
 * que JPAConfig, por exemplo, leva uma anotação que indica onde estão os
 * repositórios e que WebConfig leva uma anotação que indica onde estão os
 * controllers. Sem essas anotações, as anotações @Repository e @Controller de
 * nada iam adiantar. Para se ter uma idéia melhor, quando
 * colocamos @EnableJpaRepositories lá em JPAConfig, o Spring procura na pasta
 * indicada por classes com a anotação @Repository. Com isso ele conhece as
 * classes e as instancia. No caso so WebConfig, usamos o @ComponentScan. Então
 * para que o Spring saiba que essa classe exista e consiga encontrá-la quando a
 * aplicação subir, lá em JPAConfig vamos acrescentar o @ComponentScan, igual
 * foi feito no WebConfig. Estamos falando que na mesma pasta de
 * CervejaRepository existem outros "Beans"
 * (seja @Component, @Service, @Controller ou Whatever) que devem sem
 * encontrados e instanciados!
 * 
 * 
 * Em outras palavras: Já sabemos que uma classe Bean pode ser anotada
 * com @Repository, @Component, @Service ou @Controller. Porém precisamos avisar
 * o Spring onde essas classes estão, para que ele as instancie quando a
 * aplicação subir. A maneira de fazer isso é colocar nas classes de
 * configuração (classes com @Configuration, que também é um Bean mas, é tipo
 * "mãe" dos outros) uma anotação. JPAConfig vai avisar o Spring em que pasta
 * estão os repositorios através da anotação @EnableJpaRepositories. WebConfig
 * vai avisar em que pasta estão os controllers através da
 * anotação @ComponentScan. ServiceConfig vai avisar onde estão as classes de
 * serviço também através da anotação @ComponentScan. Todo classe que seja
 * simplesmente um @Component também exige que em alguma classe de configuração
 * se tenha a anotação @ComponentScan, indicando o pacote o qual aquela classe
 * se encontra. >>> ENTÃO @Repository, @Component, @Service ou @Controller
 * PRECISAM SER ENCONTRADOS!
 * 
 * 
 * 
 */
