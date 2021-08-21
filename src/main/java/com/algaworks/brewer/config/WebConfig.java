package com.algaworks.brewer.config;

import java.math.BigDecimal;
import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.algaworks.brewer.controller.CervejasController;
import com.algaworks.brewer.controller.converter.CidadeConverter;
import com.algaworks.brewer.controller.converter.EstadoConverter;
import com.algaworks.brewer.controller.converter.EstiloConverter;
import com.algaworks.brewer.thymeleaf.BrewerDialect;
import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;

import nz.net.ultraq.thymeleaf.LayoutDialect;

/**
 * @author Rafaell Estevam
 *
 */
@Configuration
@ComponentScan(basePackageClasses = { CervejasController.class })
@EnableWebMvc
@EnableSpringDataWebSupport//8
public class WebConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {

	/*1*/
	
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {/*9*/
		this.applicationContext = applicationContext;

	}

	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		resolver.setCharacterEncoding("UTF-8");
		return resolver;
	}
	
	@Bean
	public TemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setEnableSpringELCompiler(true);
		engine.setTemplateResolver(templateResolver());
		
		engine.addDialect(new LayoutDialect()); /*2*/
		engine.addDialect(new BrewerDialect());
		engine.addDialect(new DataAttributeDialect()); /*10*/
		
		return engine;
	}
	

	
	private ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(applicationContext);
		resolver.setPrefix("classpath:/templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		return resolver;
	}
	
	/*3*/
	
	
	@Override /*4*/
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
	}
	
	
	
	@Bean
	public FormattingConversionService mvcConversionService() { /* 5 */
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
		conversionService.addConverter(new EstiloConverter());
		conversionService.addConverter(new CidadeConverter());
		conversionService.addConverter(new EstadoConverter());
		
		
		NumberStyleFormatter bigDecimalFormatter = new NumberStyleFormatter("#,##0.00"); //7
		conversionService.addFormatterForFieldType(BigDecimal.class, bigDecimalFormatter);
		
		NumberStyleFormatter integerFormatter = new NumberStyleFormatter("#,##0"); //7
		conversionService.addFormatterForFieldType(Integer.class, integerFormatter);
		
		return conversionService;
	}
	
	
	
	@Bean
	public LocaleResolver localeResolver() {
		return new FixedLocaleResolver(new Locale("pt", "BR")); //6
	}
	
	
	
	
	
	
}



/*
 *1. Inicio - configurações do Thymeleaf
 * 
 *2. Como adicionamos o thymeleaf layout dialect para padronizar os layouts(ver o pom.xml), temos que vir aqui e adicionar esse novo dialeto à template engine.
 *   O que estamos falando para o thymeleaf é para ele adicionar esse dialeto para podermos construir nossas páginas com seus recursos.
 * 
 *3. Fim - configurações do Thymeleaf 
 * 
 *4. (5.3) "Para todos os elementos estáticos que vc encontrar nas views (arquivos css, javascript e imagens), procure os arquivos em classpath(pasta resources) / static.
 *   Estamos informando ao Spring onde ele deve procurar os arquivos css, javascript e as imagenss dos nossos templates. "/**" significa "para todos os recursos estáticos das views".
 *	 O método serve para adicionar recursos que não estão no Controller. 
 *
 *5. (9.7) Estamos registrando nosso conversor. Ele vai converter um id recebido do campo estilo do formulário para um objeto estilo. O nome do método tem que ser "mvcConversionService". 
 *	  É esse método que o Spring vai procurar para localizar os conversores.
 *
 *
 *6. (10.3) Estamos fazendo com que o navegador interprete as informações que são envidas usando o Português. Como o envio dos dados do formulário é em Português, temos que  
 * 	 fazer com que o navegador os interprete em Português. Principalmente os números. Pois os identificadores de decimal e milhar são diferentes nos dois idiomas. Então se
 * 	 os números são enviados num padrão Português mas o navegador está configurado para interpretar em inglês, vai dar erro. Então com esse código estamos forçando o 
 * 	 navegador a interpretá-los em Português.  
 *
 *7. (10.3) Estamos ensinando o Spring a converter String para BigDecimal e Integer. Lembre-se que os dados do formulário são enviados sempre como String. Temos que usar 
 *   conversores para transformar os ids em objetos (já vimos como fazer) e temos que ensinar o Spring a converter as Strings para esses tipos numéricos, pois se digitarmos
 *   um BigDecimal ou um inteiro muito grande, ele não sabe como converter. Então usamos esse código.  
 *	
 *8. (15.7) Essa anotação vai adicionar suporte a algumas coisas que o Spring data tem para a parte web. Ele vai conseguir traduzir os parâmetros que vêm na requisição e 
 *	 construir o objeto "pageable" no controller.
 *
 *9. applicationContext é um objeto do Spring. Quando a aplicação sobe no servidor é possível recebê-lo implementando a interface ApplicationContextAware na classe. 
 *	 Isso nos obriga a implementar os métodos dessa interface, que é somente o setApplicationContext().
 *
 *10. (16.4) Adicionando o dialeto thymeleaf-extras-data-attribute para que possamos gerar atributos nas tags HTML de forma mais fácil.
 *
 */












