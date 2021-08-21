package com.algaworks.brewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.algaworks.brewer.service.CervejaService;
import com.algaworks.brewer.storage.FotoStorage;
import com.algaworks.brewer.storage.local.FotoStorageLocal;

/**
 * @author Rafaell Estevam
 *
 */
@Configuration
@ComponentScan(basePackageClasses = CervejaService.class)
public class ServiceConfig {
	
	@Bean
	public FotoStorage fotoStorage() {
		return new FotoStorageLocal();
	}
	
	
}
















/*
(14.6)
Basicamente quando você coloca a anotação @Bean, você está dizendo pro Spring que quer criar esse objeto e deixar ele (o retorno do método) disponível 
para outras classes utilizarem ele como dependência, por exemplo. Se a anotação @Bean está no método, é importante que a classe em si tenha alguma anotação 
que indique pro Spring que a classe deve ser "processada". Pode colocar, por exemplo a anotação @Component na classe em si. Sem essa anotação o método 
não é invocado. Chamamos um bean em uma classe através da anotação @Autowired.
 */
 