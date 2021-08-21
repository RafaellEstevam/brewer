package com.algaworks.brewer.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Estilo;

/**
 * @author Rafaell Estevam
 *
 * Não esquecer de adicionar esse converter no WebConfig, no método "mvcConversionService().
 */
public class EstiloConverter implements Converter<String, Estilo>{ //1

	@Override
	public Estilo convert(String id) {
		if(!StringUtils.isEmpty(id)) {//2
			Estilo estilo = new Estilo();
			estilo.setId(Long.valueOf(id));
		
			return estilo;
		}
		
		return null;
	}
	
}







/*
 * 1. Cuidado ao importar. Existem várias interfaces com o mesmo nome. Tem que ser do pacote "core.convert.converter.Converter;". Em Converter<String, Estilo>, estamos 
 * 	  passando no construtor de quem para quem queremos converter. Então nesse caso queremos converter de String, o id do formulário vem como String, para o objeto estilo. 
 *    Esse vai ser usado automaticamente pelo Spring para converter um id para um objeto estilo. Para isso, temos que registrar esse converter no Web.Config. 
 *    Ir para Web.config.
 * 
 * 2. Se id não for "". Esse "" pode ser mandado se o usuário selecionar a opção "selecione um estilo".
 * 
 * 
 * 3. Resumo de como criar toda essa parte de Converters corretamente:
 * 
 * A. Criamos o pacote converter dentro do pacote controller.
 * B. Criamos a classe de conversao em si
 * C. Vamos informar o Spring que essa classe de conversão existe. Então vamos em WebConfig e adicionamos o método "mvcConversionService()". 
 * 
 * 
 */


