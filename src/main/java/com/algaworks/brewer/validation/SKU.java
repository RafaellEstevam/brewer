package com.algaworks.brewer.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

/**
 * @author Rafaell Estevam
 *
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})//1
@Retention(RetentionPolicy.RUNTIME)//2
@Constraint(validatedBy = {})//3
@Pattern(regexp = "([a-zA-Z]{3}\\d{4})?")//4
public @interface SKU {

	@OverridesAttribute(constraint = Pattern.class, name = "message")//5
	String message() default "Sku deve seguir o padrão XXX0000";//5
	
	Class<?>[] groups() default {};//6
	Class<? extends Payload>[] payload() default {};//6
	
	
}





/*
1. Onde essa anotação pode ser aplicada. Em atributos, em campos e em outras anotações;
2. Essa é uma anotação que vai ser avaliada em tempo de execução.
3. Faz parte do BeanValidation. Para eu criar uma anotação que faz parte do BeanValidation, preciso colocar essa anotação. 
   O validateBY indica a classe que está levando o padrão da validação. Por enquanto está vazia, então temos que aplicar o padrão 
   aqui mesmo, com a anotação @Pattern.
4.O usuário tem que seguir esse padrão: [a-zA-Z](sign: caracteres de a-z ou A-Z) | {3}(sign: 3 caracteres) | \\(sign: seguido por) | d(sign: dígitos) | {4} (sign: 4 dígitos) 
  ?(essa validação só será aplicada se o campo não estiver vazio).     
5. Estamos sobrescrevendo o atributo mensagem do @Pattern. Para isso é necessário a anotação  @OverridesAttribute, onde indicamos a classe que queremos modificar e qual atributo.

6. São propriedades obrigatórias em uma anotação que quer fazer parte do BeanValidation.  Se não as colocarmos, dará erro. 
   A 1º é para agrupar validações e a segunda, não muito utilizada, é para carregar mais informações sobre essa validação,
   como por exemplo uma classe que classifique a gravidade do erro. Não vamos usar esse 2º no projeto.

*/