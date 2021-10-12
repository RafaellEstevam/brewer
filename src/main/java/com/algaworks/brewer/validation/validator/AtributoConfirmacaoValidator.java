package com.algaworks.brewer.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import org.apache.commons.beanutils.BeanUtils;

import com.algaworks.brewer.validation.AtributoConfirmacao;

/**
 * @author Rafaell Estevam
 *
 */
public class AtributoConfirmacaoValidator implements ConstraintValidator<AtributoConfirmacao, Object>{//1

	private String atributo;
	private String  atributoConfirmacao;
	
	
	@Override
	public void initialize(AtributoConfirmacao constraintAnnotation) { //2
		this.atributo = constraintAnnotation.atributo();
		this.atributoConfirmacao = constraintAnnotation.atributoConfirmacao();
	}

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {//3
		boolean valido = false;
		
		
		try {
			Object valorAtributo = BeanUtils.getProperty(object, this.atributo);
			Object valorAtributoConfirmacao = BeanUtils.getProperty(object, this.atributoConfirmacao);

			valido = ambosSaoNull(valorAtributo, valorAtributoConfirmacao) || ambosSaoIguais(valorAtributo, valorAtributoConfirmacao);
			
		}catch(Exception e) {
			throw new RuntimeException("Erro recuperando valores dos atributos", e);
		}
		
		if(!valido) {//4
			context.disableDefaultConstraintViolation();//7
			String message = context.getDefaultConstraintMessageTemplate();//5
			ConstraintViolationBuilder violationBuilder = context.buildConstraintViolationWithTemplate(message);//6
			violationBuilder.addPropertyNode(atributoConfirmacao).addConstraintViolation();//6
		}
		
		return valido;
	}

		
	private boolean ambosSaoIguais(Object valorAtributo, Object valorAtributoConfirmacao) {
		return valorAtributo != null && valorAtributo.equals(valorAtributoConfirmacao);
	}

	private boolean ambosSaoNull(Object valorAtributo, Object valorAtributoConfirmacao) {
		return valorAtributo == null && valorAtributoConfirmacao == null;
	}

}





































/*
(18.4)

1. "AtributoConfirmacao" é o nome da anotação, indicando que essa classe é o validador dessa anotação. "Object" significa que a anotação @AtributoConfirmacao e esse validador pode ser usada em qualquer classe, não somente na classe Usuário. 
Se colocássemos "Usuario", só poderia ser usada nela. Assim, podemos usá-la em qualquer classe que precisar verificar se dois campos são iguais.
Perceba que estamos modelando essa classe de uma forma genérica, justamente para usarmos em outras classes, se precisarmos.

2. inicializando a classe. Com "constraintAnnotation.atributo();" e "constraintAnnotation.atributoConfirmacao();" estamos passando para as variáveis somente os 
nomes dos atributos que foram passados na anotação (e não o valor). O valor será capturado no método abaixo, como um getProperty(), passando dentro dele esse nome 
de atributo. Precisamos fazer dessa forma pois estamos usando um Object para representar a classe ques estamos validando (lembre-se que essa anotação pode ser usada
por qqr classe que precisar comparar dois atributos). Usamos o getProperty() para pegar os valores dos atributos da classe em questão.
OBS: Para pegarmos os atributos com getProperty(), precisamos adicionar a dependência do 'commons-beanutils', que é uma biblioteca que nos auxilia a trabalhar
com classes e objetos.

3. "object" é a classe que queremos validar, que foi passada lá em cima em "Object", e em seguida estamos passando o nome do atributo dessa classe que queremos pegar. 
4. Aqui vamos rejeitar o atributo confirmacaoSenha caso as senhas não estejam válidas.  
5. Vai pegar a mensagem que foi passada na chamada da anotação.
6. Informando qual dos dois atributos que foram passados na anotação deve ser rejeitado. 
7. Desabilitando o comportamento padrão para não fazer a validação duplicada.
 
*/