package com.algaworks.brewer.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.thymeleaf.util.StringUtils;

import com.algaworks.brewer.validation.SKU;

/**
 * @author Rafaell Estevam
 *
 */
@Entity
@Table(name = "cerveja")
public class Cerveja implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@SKU // 5
	@NotBlank(message = "SKU é obrigatório") // 1
	private String sku;

	@NotBlank(message = "Nome é obrigatório")
	@Size(max = 50, message = "O Nome deve conter no máximo 50 caracteres")
	private String nome;

	@Size(min = 1, max = 50, message = "O Tamanho da descrição deve estar entre 1 e 50 caracteres") // 2
	private String descricao;

	@NotNull(message = "Valor é obrigatório")
	@DecimalMin(value = "0.50", message = "O valor da cerveja deve ser no mínimo R$0,50")
	@DecimalMax(value = "9999.99", message = "O valor da cerveja deve ser menor que R$ 9.999,99")
	private BigDecimal valor;

	@NotNull(message = "Teor alcóolico é obrigatório")
	@DecimalMax(value = "100.00", message = "O valor do teor alcóolico deve ser menor ou igual a 100")
	@Column(name = "teor_alcoolico")
	private BigDecimal teorAlcoolico;

	@NotNull(message = "A comissão é obrigatória")
	@DecimalMax(value = "100.00", message = "O valor da comissão deve ser menor ou igual a 100")
	private BigDecimal comissao;

	@NotNull(message = "O estoque é obrigatório")
	@Max(value = 100000, message = "O estoque deve ser máximo de 100.000 unidades")
	@Column(name = "quantidade_estoque")
	private Integer quantidadeEstoque;

	@NotNull(message = "O sabor é obrigatório")
	@Enumerated(EnumType.STRING)
	private Sabor sabor;

	@NotNull(message = "A origem é obrigatória")
	@Enumerated(EnumType.STRING)
	private Origem origem;

	@NotNull(message = "O estilo é obrigatório")
	@ManyToOne // 4
	private Estilo estilo;

	
	private String foto;

	@Column(name="content_type")
	private String contentType;

	public Cerveja() { // 3

	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getTeorAlcoolico() {
		return teorAlcoolico;
	}

	public void setTeorAlcoolico(BigDecimal teorAlcoolico) {
		this.teorAlcoolico = teorAlcoolico;
	}

	public BigDecimal getComissao() {
		return comissao;
	}

	public void setComissao(BigDecimal comissao) {
		this.comissao = comissao;
	}

	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public Sabor getSabor() {
		return sabor;
	}

	public void setSabor(Sabor sabor) {
		this.sabor = sabor;
	}

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	public Estilo getEstilo() {
		return estilo;
	}

	public void setEstilo(Estilo estilo) {
		this.estilo = estilo;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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
		Cerveja other = (Cerveja) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "sku=" + sku + ", nome=" + nome + ", descricao=" + descricao + ", valor=" + valor + ", teorAlcoolico="
				+ teorAlcoolico + ", comissao=" + comissao + ", quantidadeEstoque=" + quantidadeEstoque + ", sabor="
				+ sabor + ", origem=" + origem + ", estilo=" + estilo;
	}

	@PrePersist
	@PreUpdate // 6
	public void prePersistUpdate() {
		sku = sku.toUpperCase();
	}
	
	
	public String getFotoOuMock() {
		return StringUtils.isEmpty(this.foto) ? "cerveja-mock.png" : foto;
	}
	
	

}

/*
 * 1. Anotação do BeanValidation, para informar que esse atributo não pode ser
 * null ou ficar em branco. "message" é a msg de erro que vai aparecer se o
 * quesito n for cumprido.
 * 
 * 
 * 2. Tamanho maximo de caracteres nesse atributo e msg de erro que vai
 * aparecer.
 * 
 * 3. Construtor padrão é obrigatório para o Spring construir o objeto, qdo
 * recebido parâmetros do formulário. Não precisaríamos colocá-lo aqui. Foi só
 * para explicar
 * 
 * 4. Professor acrescentou uma anotação depois do @MTO.
 * 
 * 5. (10.2) Validações customizadas com BeanValidation. Exitem duas formas de
 * fazer:
 * 
 * A. A primeira delas é aplicar o padrão de validação aqui mesmo. Assim,
 * colocaríamos a seguinte anotação:
 * 
 * @Pattern(regexp = "([a-zA-Z]{3}\\d{4})?") >>> O usuário tem que seguir esse
 * padrão: caracteres de a-z ou A-Z {3 caracteres} \\(seguido por) d(dígitos) {4
 * dígitos} ?(essa validação só será aplicada se o campo não estiver vazio).
 * 
 * B. É o que foi feito. Criar uma anotação nossa e jogar nessa anotattion
 * criada o padrão de validação. Ir no pacote validation e observar a classe
 * SKU.
 * 
 * 6. (11.5) Antes de persistir ou antes de atualizar, pegue o sku e jogue tudo
 * para maiúsculo. Existem outras anotações como @PreRemove. Essas funções que
 * criamos são chamadas de callbacks.
 * 
 * 
 */
