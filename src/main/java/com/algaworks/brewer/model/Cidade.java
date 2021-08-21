package com.algaworks.brewer.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Rafaell Estevam
 *
 */
@Entity
@Table(name = "cidade")
public class Cidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
	@ManyToOne(fetch = FetchType.LAZY)//2
	@JsonIgnore //1 
	private Estado estado;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cidade other = (Cidade) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}



/*
1. (16.7) Anotação do Jackson que diz que na hora que tiver que montar o JSON de cidade, n é pra colocar nada referente ao 'estado'. Olhar anotações 
em CidadeController.

2. (16.7 ,ultimos 5 min) Colocamos essa anotação pois não queremos que sempre que buscarmos uma cidade que o sistema busque o estado tbem. É o comportamento padrão 
	EAGER, lembra? Ele sempre vai inicializar o relacionamento ToOne fazendo uma busca.
	Colocamos essa anotação pois o código "cidadeRepository.findByEstadoId(codigoEstado);" lá em CidadeController faz um Join entre cidade e estado, e joga numa outra
	tabela somente as cidades com estado_id = x. Aí na hora que ele tem que preencher a lista com objetos do tipo Cidade, na hora que ele tem que preencher o objeto 
	Estado (Cidade tem um Estado, lembra?) ele faz um select para buscar o estado com id = x. Porém é desnecessário ele fazer essa busca pois não vamos usar nenhuma
	informação de Estado. Colocamos então o Fetch.Lazy como uma indicação de que ele não precisa fazer a busca de Estado quando tiver que preencher algum objeto cidade.
	Ele só vai fazer essa busca quando eu solicitar.   
	
	o sistema ainda estava fazendo um select para buscar o estado e preencher na tabela de cidades que ele retornou (que n entendi direito). Então colocamos o fetch = FetchType.LAZY
	para ele n fazer essa busca.
 */
