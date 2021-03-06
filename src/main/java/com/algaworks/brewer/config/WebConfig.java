package com.algaworks.brewer.config;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeansException;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
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
import com.algaworks.brewer.controller.converter.GrupoConverter;
import com.algaworks.brewer.thymeleaf.BrewerDialect;
import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;
import com.google.common.cache.CacheBuilder;

import nz.net.ultraq.thymeleaf.LayoutDialect;

/**
 * @author Rafaell Estevam
 *
 */
@Configuration
@ComponentScan(basePackageClasses = { CervejasController.class })
@EnableWebMvc
@EnableSpringDataWebSupport//8
@EnableCaching//11
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
		conversionService.addConverter(new GrupoConverter());
		
		//7
		NumberStyleFormatter bigDecimalFormatter = new NumberStyleFormatter("#,##0.00"); 
		conversionService.addFormatterForFieldType(BigDecimal.class, bigDecimalFormatter);
		
		//7
		NumberStyleFormatter integerFormatter = new NumberStyleFormatter("#,##0");
		conversionService.addFormatterForFieldType(Integer.class, integerFormatter);
		
		
		//13 (API de Datas do Java 8)
		DateTimeFormatterRegistrar dateTimeFormatter = new DateTimeFormatterRegistrar();
		dateTimeFormatter.setDateFormatter(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		dateTimeFormatter.registerFormatters(conversionService);
		
		return conversionService;
	}
	
	
	
	@Bean
	public LocaleResolver localeResolver() {
		return new FixedLocaleResolver(new Locale("pt", "BR")); //6
	}
	
	
	@Bean
	public CacheManager cacheManager() {//11
		CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
				.maximumSize(3)// 12
				.expireAfterAccess(20, TimeUnit.SECONDS);// 12
		
		GuavaCacheManager cacheManager = new GuavaCacheManager();
		cacheManager.setCacheBuilder(cacheBuilder);
		
		return cacheManager;
	}
	
	
	@Bean
	public MessageSource messageSource() {//14
		ReloadableResourceBundleMessageSource bundle = new ReloadableResourceBundleMessageSource();
		bundle.setBasename("classpath:/messages");
		bundle.setDefaultEncoding("UTF-8"); // http://www.utf8-chartable.de/
		return bundle;
	}
	
	
	
}



/*
 *1. Inicio - configura????es do Thymeleaf
 * 
 *2. Como adicionamos o thymeleaf layout dialect para padronizar os layouts(ver o pom.xml), temos que vir aqui e adicionar esse novo dialeto ?? template engine.
 *   O que estamos falando para o thymeleaf ?? para ele adicionar esse dialeto para podermos construir nossas p??ginas com seus recursos.
 * 
 *3. Fim - configura????es do Thymeleaf 
 * 
 *4. (5.3) "Para todos os elementos est??ticos que vc encontrar nas views (arquivos css, javascript e imagens), procure os arquivos em classpath(pasta resources) / static.
 *   Estamos informando ao Spring onde ele deve procurar os arquivos css, javascript e as imagenss dos nossos templates. "/**" significa "para todos os recursos est??ticos das views".
 *	 O m??todo serve para adicionar recursos que n??o est??o no Controller. 
 *
 *5. (9.7) Estamos registrando nosso conversor. Ele vai converter um id recebido do campo estilo do formul??rio para um objeto estilo. O nome do m??todo tem que ser "mvcConversionService". 
 *	  ?? esse m??todo que o Spring vai procurar para localizar os conversores.
 *
 *
 *6. (10.3) Estamos fazendo com que o navegador interprete as informa????es que s??o envidas usando o Portugu??s. Como o envio dos dados do formul??rio ?? em Portugu??s, temos que  
 * 	 fazer com que o navegador os interprete em Portugu??s. Principalmente os n??meros. Pois os identificadores de decimal e milhar s??o diferentes nos dois idiomas. Ent??o se
 * 	 os n??meros s??o enviados num padr??o Portugu??s mas o navegador est?? configurado para interpretar em ingl??s, vai dar erro. Ent??o com esse c??digo estamos for??ando o 
 * 	 navegador a interpret??-los em Portugu??s.  
 *
 *7. (10.3) Estamos ensinando o Spring a converter String para BigDecimal e Integer. Lembre-se que os dados do formul??rio s??o enviados sempre como String. Temos que usar 
 *   conversores para transformar os ids em objetos (j?? vimos como fazer) e temos que ensinar o Spring a converter as Strings para esses tipos num??ricos, pois se digitarmos
 *   um BigDecimal ou um inteiro muito grande, ele n??o sabe como converter. Ent??o usamos esse c??digo.  
 *	
 *8. (15.7) Essa anota????o vai adicionar suporte a algumas coisas que o Spring data tem para a parte web. Ele vai conseguir traduzir os par??metros que v??m na requisi????o e 
 *	 construir o objeto "pageable" no controller.
 *
 *9. applicationContext ?? um objeto do Spring. Quando a aplica????o sobe no servidor ?? poss??vel receb??-lo implementando a interface ApplicationContextAware na classe. 
 *	 Isso nos obriga a implementar os m??todos dessa interface, que ?? somente o setApplicationContext().
 *
 *10. (16.4) Adicionando o dialeto thymeleaf-extras-data-attribute para que possamos gerar atributos nas tags HTML de forma mais f??cil.
 *
 *11. Olhar CidadeController item 3.
 *
 *12. (17.3) implementando cache profissinal com Guava do Google: 
 *	1. Adicionar a depend??ncia do Guava e do projeto do spring que vai dar suporte ?? ele, chamado Spring Context Support, no pom.xml
 *
 *	2. No WebConfig, alterar ou criar o m??todo cacheManager() implementando as configura????es do guava. Somente isso. N??o precisamos
 *	alterar mais nada no Controller. As anota????es continuam sendo as mesmas. Somente alteramos a implementa????o do cacheManager, de uma 
 *	que era baseada em mapas(do Spring) para uma onde consigo configurar mais op????es, como quantidade de entradas no cache(maximumSize). 
 *  e tempo de expira????o (expireAfterAccess). 
 *  Em rela????o ao maximumSize, nesse nosso caso, cada c??digo de lista de cidades seria uma entrada. Como colocamos 3, ent??o ele s?? consegue
 *  guardar na cache at?? 3 listas de cidades. Caso tentemos carregar a 4?? lista, ele vai retirar uma das 3 da cache(a mais antiga) para alocar
 *  a nova lista.
 *  Quanto ao expireAfterAcess, significa que a lista (ou dado em quest??o) ser?? expirada (apagada) depois de 20 segundos sem utiliza????o.
 *  Perceba que n??o ?? a cache toda que ?? apagada e sim somente as listas as quais se passaram 20s sem serem utilizadas.  
 *  Enquanto estou consultando o cache os dados permanecem l??, mas se passarem 20s e ele ningu??m o utilizou, ele ?? apagado. 
 *  
 * 13. (18.5) Adicionando o conversor de String para LocalDate. Estamos usando-o na p??gina de Usu??rio, para converter para LocalDate a data de nascimento 
 * que ?? passada como String no formul??rio. 
 * 
 * 14. (18.5 +- 21:55) O m??todo informa onde est?? a classe de configura????o das mensagens padr??es que o sistema exibe quando uma exce????o ?? lan??ada. Olhar 
 * CadastroUsuario.html, item 7. OBS: Perceba que n??o precisamos colocar o sufixo '.properties' em 'classpath:/messages'. 
 * Nesse arquivo :
 * 	*typeMismatch.java.time.LocalDate = {0} inv\u00E1lida >>> estamos dizendo que quando ele n??o conseguir converter para LocalDate, mostre 
 * a mensagem {0} inv\u00E1lida, sendo {0} o campo que deu erro e por causa do ?? de 'inv??lida', tivemos que olhar na tabela UTF-8 (link no c??digo) e 
 * pegar o c??digo desse caractere, colocando \ antes. Sugere-se ver a aula. Sempre que der erro de convers??o podemos usar sempre esse padr??o: 
 * typeMismatch + nome da classe para o qual ele tentou converter. 
 * 
 *  *usuario.dataNascimento = Data de nascimento >>> estamos falando que quando ele tiver que mostrar o texto do campo dataNascimento, para ele na vdd mostrar 
 *  o texto "Data de nascimento."
 * 
 * 
 * 
 */












