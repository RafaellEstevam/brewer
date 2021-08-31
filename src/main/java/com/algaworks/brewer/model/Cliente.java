package com.algaworks.brewer.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

import com.algaworks.brewer.model.validation.ClienteGroupSequenceProvider;
import com.algaworks.brewer.model.validation.group.CnpjGroup;
import com.algaworks.brewer.model.validation.group.CpfGroup;

/**
 * @author Rafaell Estevam
 * 
 */
@Entity
@Table(name = "cliente")
@GroupSequenceProvider(ClienteGroupSequenceProvider.class) //3 (valide segundo uma sequência)
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O nome é obrigatório")
	private String nome;
	
	@NotNull(message = "Tipo Pessoa é obrigatório")
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_pessoa")
	private TipoPessoa tipoPessoa;

	@NotBlank(message = "CPF/CNPJ é obrigatório")
	@CPF(groups = CpfGroup.class)//2
	@CNPJ(groups = CnpjGroup.class)//2 
	@Column(name = "cpf_cnpj")
	private String cpfOuCnpj;

	private String telefone;
	
	@NotBlank(message = "O e-mail é obrigatório")
	@Email(message = "E-mail inválido") 
	private String email;

	@Embedded //1
	private Endereco endereco;

	
	
	
	@PrePersist @PreUpdate
	private void prePersistPreUpdate() { //4
		this.cpfOuCnpj = TipoPessoa.getCpfCnpjSemFormatacao(this.cpfOuCnpj); //5
	}
	
	
	public String getCpfCnpjSemFormatacao() {
		return  TipoPessoa.getCpfCnpjSemFormatacao(this.cpfOuCnpj);
	}
	
	@PostLoad 
	private void postLoad() { //6
		this.cpfOuCnpj = this.tipoPessoa.formatar(this.cpfOuCnpj); //7	
	}
	
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

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getCpfOuCnpj() {
		return cpfOuCnpj;
	}

	public void setCpfOuCnpj(String cpfOuCnpj) {
		this.cpfOuCnpj = cpfOuCnpj;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
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
		Cliente other = (Cliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}



















/*
 1. Anotação para indicar que a classe Endereço foi imbutida aqui. Significa que Cliente e Endereco estão na mesma tabela. Na classe Endereco, colocamos
 a anotação @Embeddable.
  
 2. Explicação em 16.9 aos 22 min. Aconselha-se assistir a aula pois são vários passos. Explica sobre o "group sequence provider", que é uma forma de dizer ao 
 Bean Validation qual a order que queremos que ele faça a validação. O que vamos fazer é falar que primeiro ele deve validar os atributos que não tem 
 nenhuma anotação de grupo (ou seja, todos menos o atributo cpfOuCnpj) e depois ele deve validar os que tem alguma anotação de group (atributo cpfOuCnpj, que 
 levam CpfGroup.class e CnpjGroup.class) mas da forma que eu ensinei na classe "ClienteGroupSequenceProvider.class" (por isso a anotação @GroupSequenceProvider
 lá em cima). E lá, vamos dizer que se tipoPessoa.grupo for CpfGroup, valide como cpf. Se tipoPessoa.grupo for CnpjGroup, valide como cnpj. 
 Por isso modificamos o Enum TipoPessoa, adicionando o atributo grupo e colocando a classe correspondente  a cada tipo de pessoa.(Olhar Enum TipoPessoa).  
 Lembrando que esses grupos são somente interfaces de marcação, ou seja, interfaces vazias, sem nada escrito. É só para marcação mesmo.
 
 
 3. Essa anotação diz o seguinte: "valide o bean segundo uma sequência que eu vou te ensinar". Sendo assim, na classe 'ClienteGroupSequenceProvider' definimos a 
 ordem que queremos que a validação seja feita e podemos colocar também qqr regra de negócio que precisarmos.
 
 4. (16.10) Antes de persistir ou atualizar o cliente, vamos tirar toda a pontuação do cpf/cnpj. O regex "\\.|-|/" significa: (substitua) todos os '.' e '-' e '/'.
 Em outras palavras, '\\' significa "todos os" enquanto '|' significa "e". Poderíamos fazer a mesma coisa com o Cep. Nesse caso o código entraria nesse mesmo método.
 
 5. (16.11) O código 'this.cpfOuCnpj.replaceAll("\\.|-|/", "");' foi jogado para o Enum TipoPessoa.
 
 6. (16.13) Da mesma forma que temos o @PostPersist e @PostUpdate, temos também o @PostUpload que será executado logo depois que o sistema carregar um Cliente do BD.
 
 7. (16.13) Atenção aqui. Estamos pegando um método abstrato, que pertence ao enum específico e não à classe. Se chamarmos o enum com "this", podemos pegar os
  métodos implementados dentro do escopo de cada enum específico (dentro do escopo 'FISICA' ou 'JURIDICA'). Se observarmos a classe 'TipoPessoa', veremos que o 
  método 'formatar' foi implementado dentro do escopo de cada enum. Observe também que tivemos que colocá-lo como abstrato.
  
 */
