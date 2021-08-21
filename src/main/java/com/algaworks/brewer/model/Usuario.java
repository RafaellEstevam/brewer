package com.algaworks.brewer.model;

import java.time.LocalDate;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Rafaell Estevam
 *
 */
public class Usuario {

	private Long id;
	@NotBlank(message="Nome é obrigatório!")
	private String nome;
	@NotBlank(message="E-mail é obrigatório!")
	private String email;
	@NotBlank(message="Data de nascimento é obrigatório!")
	private LocalDate dataNascimento;
	@NotBlank(message="Senha é obrigatório!")
	private String senha;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
