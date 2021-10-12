package com.algaworks.brewer.config.init;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.algaworks.brewer.config.JPAConfig;
import com.algaworks.brewer.config.SecurityConfig;
import com.algaworks.brewer.config.ServiceConfig;
import com.algaworks.brewer.config.WebConfig;

/**
 * @author Rafaell Estevam
 *
 */
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() { 
		
		return new Class<?>[] {JPAConfig.class, ServiceConfig.class, SecurityConfig.class};//1
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		
		return new Class<?>[] {WebConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		
		return new String[] {"/"};
	}

	@Override
	protected Filter[] getServletFilters() { 
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		
		return new Filter[] {characterEncodingFilter};
	}
	
	@Override //2
	protected void customizeRegistration(Dynamic registration) {
		registration.setMultipartConfig(new MultipartConfigElement(""));
	}
	
	
}

/*
1 . (9.2) 

Adicionamos a classe de configuração JPAConfig nesse método. Mas porque nele e não em "getServletConfigClasses()" ? O instrutor usa a seguinte regra: Todos os 
arquivos de configuração que disserem respeito ao Controller pra frente(controlador e Thymeleaf, por exemplo) joga em "getServletConfigClasses()" e tudo o que 
for de controller para trás (tirando o controller, claro. Ou seja, parte de acesso a banco de dados e bd) joga para o  "getRootConfigClasses()". 
Como esse método será o primeiro a ser lido, queremos que os recursos de acesso ao banco de dados (os arquivos que ele leva configuram essa parte) estejam 
disponíveis para os controllers usarem, quando o método "getServletConfigClasses()" for lido, pois agora sim aquela configuração foi carregada.  

(9.8) Adicionamos a classe ServiceConfig. Arquivo de configuração dos nossos serviços. A princípio, leva somente a info de onde o Spring deve procurar os services.
(18.9) Adicionamos a classe SecurityConfig. Arquivo de configuração do Spring Security. 

2.(14.3) 

Método para configurar onde os arquivos de upload deverão ser armazenados e etc. Quando passamos uma String vazia em 'new MultipartConfigElement("")', estamos dizendo
para o servidor decidir onde ele quer armazenar os arquivos. Quando usamos esse contrutor, também estamos dizendo para o servidor não se preocupar com o tamanho dos 
arquivos(clicar para ver a implementação do construtor. Perceba que existe um atributo ' this.maxFileSize = -1L;', que define um tamanho de arquivo ilimitado.). Olhar 
'FotoController'.



*/