package com.algaworks.brewer.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Estado;

/**
 * @author Rafaell Estevam
 * (16.9)
 *Não esquecer de adicionar esse converter no WebConfig, no método "mvcConversionService().
 */
public class EstadoConverter implements Converter<String, Estado> {

	@Override
	public Estado convert(String id) {
		
		if(!StringUtils.isEmpty(id)) {
			Estado estado = new Estado();
			estado.setId(Long.valueOf(id));
			
			return estado;
		}
		
		
		return null;
	}

}
