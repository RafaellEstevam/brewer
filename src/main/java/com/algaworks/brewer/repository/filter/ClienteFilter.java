package com.algaworks.brewer.repository.filter;

/**
 * @author Rafaell Estevam
 *
 */
public class ClienteFilter {

	private String nome;
	private String cpfOuCnpj;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpfOuCnpj() {
		return cpfOuCnpj;
	}

	public void setCpfOuCnpj(String cpfOuCnpj) {
		this.cpfOuCnpj = cpfOuCnpj;
	}

	@Override
	public String toString() {
		return "nome=" + nome + ", cpfOuCnpj=" + cpfOuCnpj;
	}

}
