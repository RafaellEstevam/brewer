package com.algaworks.brewer.model;

import com.algaworks.brewer.model.validation.group.CnpjGroup;
import com.algaworks.brewer.model.validation.group.CpfGroup;

/**
 * @author Rafaell Estevam
 *
 */
public enum TipoPessoa {

	FISICA("Física", "CPF", "000.000.000-00", CpfGroup.class) {
		@Override
		public String formatar(String cpfOuCnpj) {
			
			return cpfOuCnpj.replaceAll("(\\d{3})(\\d{3})(\\d{3})", "$1.$2.$3-"); //1
		}
	},
	
	JURIDICA("Jurídica", "CNPJ", "00.000.000/0000-00", CnpjGroup.class) {
		@Override
		public String formatar(String cpfOuCnpj) {

			return cpfOuCnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})", "$1.$2.$3/$4-"); //1
		}
	};

	private String descricao;
	private String documento;
	private String mascara;
	private Class<?> grupo;

	private TipoPessoa(String descricao, String documento, String mascara, Class<?> grupo) {
		this.descricao = descricao;
		this.documento = documento;
		this.mascara = mascara;
		this.grupo = grupo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getDocumento() {
		return documento;
	}

	public String getMascara() {
		return mascara;
	}

	public Class<?> getGrupo() {
		return grupo;
	}	

	public static String getCpfCnpjSemFormatacao(String cpfOuCnpj) {
		return cpfOuCnpj.replaceAll("\\.|-|/", "");
	}

	
	public abstract String formatar(String cpfOuCnpj); //2
	
	
	
}

















/*
1. "(\\d{3})(\\d{3})(\\d{3})", "$1.$2.$3-" >>>  "(\\d{3})" significa "pegue 3 digitos". Então estamos dizendo "pegue 3 digitos (1º grupo), 
depois pegue mais 3 digitos (2ºgrupo) e depois mais 3 digitos (3ºgrupo). Assim, com  "$1.$2.$3-" estamos dizendo: "após o 1ºgrupo coloque um '.',
após o 2º grupo coloque também um '.' e após o 3º grupo coloque um '-'. A mesma idéia foi aplicada ao CNPJ (linha 24).

2. Fizemos um método abstrato aqui para que cada enum fosse forçado a definir sua implementação.
 
 */


