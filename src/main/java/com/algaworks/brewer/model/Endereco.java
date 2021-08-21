package com.algaworks.brewer.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

/**
 * @author Rafaell Estevam
 *
 */
@Embeddable
public class Endereco implements Serializable {

	private static final long serialVersionUID = 1L;

	private String logradouro;

	private String numero;

	private String complemento;

	private String cep;

	@ManyToOne // 1
	private Cidade cidade;

	@Transient // 2
	private Estado estado;
	
	
	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	public String getNomeCidadeSiglaEstado() {
		
		if(this.cidade != null) {
			return this.getCidade().getNome()+"/"+this.getCidade().getEstado().getSigla();
		}
		
		return null;
	}
	
	
	
	

}

/*
 * 1. @JoinColumn(name = "codigo_cidade") >>> se a coluna tivesse outro nome, é
 * assim que anotaríamos aqui em @ManyToOne. Perceba que é diferente dos outros
 * que estão em outras classes, que anotamos somente com @Column(name="").
 * 
 * 2. (16.9 +- 35min) @Transient informa que essa coluna não existe na
 * tabela(tabela cliente, no caso, já que essa classe "Endereco" está 'embedded'
 * com Cliente). Então esse dado 'estado' não será enviado para o banco quando
 * ocorrer a persistência de um objeto Cliente. Tivemos que fazer isso pois na
 * hora que o usuário tenta salvar um cliente e dá algum erro de validação,
 * quando a página volta, ela perdia o estado selecionado. Então tivemos que
 * acrescentar um th:field={endereco.estado} para o dado ir ao servidor e voltar
 * para a página, mas não queremos que a informação do estado vá para o banco,
 * pois nem temos coluna para ela na tabela Cliente.
 * 
 */