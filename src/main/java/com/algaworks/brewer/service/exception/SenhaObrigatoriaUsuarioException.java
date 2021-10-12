package com.algaworks.brewer.service.exception;

/**
 * @author Rafaell Estevam
 *
 */
public class SenhaObrigatoriaUsuarioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SenhaObrigatoriaUsuarioException(String message) {
		super(message);
	}
	
}
