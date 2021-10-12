package com.algaworks.brewer.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

import com.algaworks.brewer.validation.validator.AtributoConfirmacaoValidator;

/**
 * @author Rafaell Estevam
 *
 */
@Target(ElementType.TYPE) //1
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {AtributoConfirmacaoValidator.class})//2
public @interface AtributoConfirmacao {
	
	@OverridesAttribute(constraint = Pattern.class, name = "message")
	String message() default "Atributos não conferem"; //3
	
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
	//
	
	String atributo(); //4
	
	String atributoConfirmacao(); //4
	
}



/*
 Essa é a estrutura padrão para criar uma anotação de validação para o BeanValidation. Sempre vamos criar dessa forma, tirando a partir de "//" que colocamos para receber 
 os atributos da chamada da anotação.
 
 1. Colocamos ElementType.Type para indicar que essa anotação vai ser colocada em cima de classes. Perceba a diferença com o @SKU.
 2. Estamos passando a classe java que vai fazer essa validação. Ela foi criada por nós.
 3. Definindo a mensagem padrão que será enviada ao usuário, caso os atributos não sejam iguais. Repare que essa mensagem só vao aparecer quando não tiver sido passada
 	nenhuma mensagem personalizada na chamada da anotação.
 4. colocamos para receber os atributos "atributo" e "atributoConfirmacao" da chamada da anotação na classe "Usuario".
 */
