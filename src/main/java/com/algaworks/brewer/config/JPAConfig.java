package com.algaworks.brewer.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.CervejaRepository;

/**
 * @author Rafaell Estevam
 *
 */
@Configuration // 10 //12
@EnableJpaRepositories(basePackageClasses = CervejaRepository.class, enableDefaultTransactions = false)
@ComponentScan(basePackageClasses = CervejaRepository.class) // 13
@EnableTransactionManagement // 12
public class JPAConfig {

	@Bean
	public DataSource dataSource() {
		JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
		dataSourceLookup.setResourceRef(true); // 1
		return dataSourceLookup.getDataSource("jdbc/brewerDB");// 2
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {// 3
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.MYSQL);// 4
		adapter.setShowSql(false);// 5
		adapter.setGenerateDdl(false);// 6
		adapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");// 7
		return adapter;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {// 8
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setJpaVendorAdapter(jpaVendorAdapter);
		factory.setPackagesToScan(Cerveja.class.getPackage().getName());// 9
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) { // 11
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}

}

/*
 * 1. Especifica que as informa????es sobre a conex??o e a base de dados deve ser
 * olhada dentro do container(Tomcat). Ou seja, vai olhar dentro de context.xml
 * O context.xml configura o Tomcat, lembra? 2. Deve ser o mesmo nome escrito em
 * context.xml.
 * 
 * 3. Vamos configurar o Hibernate. 4. Informa qual o BD que estamos usando. 5.
 * Se quero que ele mostre ou n??o o SQL no console. 6. N??o quero que ele gere as
 * tabelas no BD pra mim, pois estamos usando o Flyway para isso. Por isso
 * colocamos false. 7. Qual ?? o dialeto. Serve para conseguir traduzir os JPQL's
 * e Criterias para a linguagem que o MYSQL entende.
 * 
 * 8. Vamos configurar o Entity Manager. 9. Onde est??o as entidades da minha
 * aplica????o para que o EntityManager as gerencie. Est??o no pacote onde est?? a
 * classe Cerveja.
 * 
 * 10. Estamos configurando o Spring para utilizar os reposit??rios que criamos
 * para nossas entidades. Definimos tamb??m onde elas est??o(no mesmo pacote que
 * CervejaRepository.class). Nessa etapa criamos o pacote repository, com o
 * nosso primeiro repositorio, para a Classe Cerveja. O professor o chamou de
 * Cervejas mas eu preferi deix??-lo como CervejaRepository.
 * 
 * 11. Como o projeto vai conseguir salvar alguma coisa no BD se eu n??o tenho a
 * transa????o configurada? Lembra que todas as opera????es de escrita devem ocorrer
 * dentro de uma transa????o? Ent??o, temos que configurar isso na m??o aqui.
 * Estamos basicamente dizendo para o transactionManager usar o
 * entityManagerFactory para criar um EntityManager e com ele abrir as
 * transa????es.
 * 
 * 12. "@EnableTransactionManagement": estamos dizendo ao Spring que n??s ?? que
 * vamos gerenciar a transa????o do banco de dados.
 * "enableDefaultTransactions = false": estamos dizendo que nos nossos
 * reposit??rios, estamos desabilitando a transa????o padr??o do Hibernate (abre
 * transa????o pra tudo). Ler item 2 de CervejaService. Pois agora eu terei que
 * abrir uma transa????o antes de realizar uma opera????o de escrita.
 * 
 * 13. (15.18) Olhar repository/paginacao/PaginacaoUtil.java. Precisavamos fazer
 * com que o Spring encontrasse o bean "PaginacaoUtil.java".
 * O @EnableJpaRepositories s?? varre o pacote indicado em busca de @Repository.
 * Como "PaginacaoUtil.java" ?? um @Component, ele n??o estava sendo encotrado.
 * 
 */