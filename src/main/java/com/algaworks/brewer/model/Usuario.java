package com.algaworks.brewer.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.algaworks.brewer.validation.AtributoConfirmacao;

/**
 * @author Rafaell Estevam
 *
 */
@AtributoConfirmacao(atributo = "senha", atributoConfirmacao = "confirmacaoSenha", message = "Confirmação de senha não confere") // 2
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O nome é obrigatório")
	private String nome;

	@NotBlank(message = "E-mail é obrigatório")
	@Email(message = "E-mail inválido")
	private String email;

	private String senha;

	@Transient // 1
	private String confirmacaoSenha;

	private boolean ativo;

	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;

	@Size(min = 1, message = "Selecione pelo menos um grupo")//3
	@ManyToMany//4
	@JoinTable(name = "usuario_grupo", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "grupo_id"))
	private List<Grupo> grupos;

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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getConfirmacaoSenha() {
		return confirmacaoSenha;
	}

	public void setConfirmacaoSenha(String confirmacaoSenha) {
		this.confirmacaoSenha = confirmacaoSenha;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	
	public boolean isNovo() {//5
		return id == null;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

/*
 * 1. Indicando que não existe essa coluna no banco de dados. É só para que o
 * valor do input confirmação senha do formulário chegue no Controller. Atenção
 * pois o
 * 
 * @Transient é do javax.Persistence.
 * 
 * 2. Vamos colocar essa anotação de validação em cima da classe pois precisamos
 * pegar mais de um atributo para fazer a validação. Se a colocássemos no
 * atributo "senha", não conseguiríamos capturar o atributo "confirmacaoSenha"
 * para fazer a validação. Além de passar os atributos que queremos comparar,
 * estamos passando a mensagem que deve ser retornada ao usuário, caso a
 * validação retorne negativo.
 * 
 * 3. O @NotNull não funciona aqui, pois mesmo que o usuário não selecione nenhum item, é enviada uma lista vazia.
 * 	E uma lista vazia é diferente de null e assim s validação nunca daria certo. 
 * 
 * 4. Não precisamos colocar cascade pois é um relacionamento ManyToMany. Sem o cascade ele já 'agarra' as duas tabelas, lembra?
 * 
 * 5. 18.9 - Colocamos isso pois provavelmente vamos usar a mesma lógica e código html para a parte de edição de usuários, e temos que verificar se o usuário é novo ou não
 * para saber que lógica usar naquela parte.
 * (ir no campo senha em CadastroUsuario.html). 
 
 */
