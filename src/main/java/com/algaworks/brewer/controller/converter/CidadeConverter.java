package com.algaworks.brewer.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Cidade;

/**
 * @author Rafaell Estevam
 *(16.9) 
 *Não esquecer de adicionar esse converter no WebConfig, no método "mvcConversionService(). 
 */
public class CidadeConverter implements Converter<String, Cidade> {

	@Override
	public Cidade convert(String id) {
		
		if(!StringUtils.isEmpty(id)) {
			Cidade cidade = new Cidade();
			cidade.setId(Long.valueOf(id));
			
			return cidade;
		}

		return null;
	}

}
