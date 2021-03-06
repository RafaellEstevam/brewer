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

1. "AtributoConfirmacao" ?? o nome da anota????o, indicando que essa classe ?? o validador dessa anota????o. "Object" significa que a anota????o @AtributoConfirmacao e esse validador pode ser usada em qualquer classe, n??o somente na classe Usu??rio. 
Se coloc??ssemos "Usuario", s?? poderia ser usada nela. Assim, podemos us??-la em qualquer classe que precisar verificar se dois campos s??o iguais.
Perceba que estamos modelando essa classe de uma forma gen??rica, justamente para usarmos em outras classes, se precisarmos.

2. inicializando a classe. Com "constraintAnnotation.atributo();" e "constraintAnnotation.atributoConfirmacao();" estamos passando para as vari??veis somente os 
nomes dos atributos que foram passados na anota????o (e n??o o valor). O valor ser?? capturado no m??todo abaixo, como um getProperty(), passando dentro dele esse nome 
de atributo. Precisamos fazer dessa forma pois estamos usando um Object para representar a classe ques estamos validando (lembre-se que essa anota????o pode ser usada
por qqr classe que precisar comparar dois atributos). Usamos o getProperty() para pegar os valores dos atributos da classe em quest??o.
OBS: Para pegarmos os atributos com getProperty(), precisamos adicionar a depend??ncia do 'commons-beanutils', que ?? uma biblioteca que nos auxilia a trabalhar
com classes e objetos.

3. "object" ?? a classe que queremos validar, que foi passada l?? em cima em "Object", e em seguida estamos passando o nome do atributo dessa classe que queremos pegar. 
4. Aqui vamos rejeitar o atributo confirmacaoSenha caso as senhas n??o estejam v??lidas.  
5. Vai pegar a mensagem que foi passada na chamada da anota????o.
6. Informando qual dos dois atributos que foram passados na anota????o deve ser rejeitado. 
7. Desabilitando o comportamento padr??o para n??o fazer a valida????o duplicada.
 
*/