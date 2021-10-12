package com.algaworks.brewer.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring4.expression.Fields;
import org.thymeleaf.spring4.util.FieldUtils;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * @author Rafaell Estevam
 *
 */
public class ClassForErrorAttributeTagProcessor extends AbstractAttributeTagProcessor {

	private static final String NOME_TAG = "classforerror"; //2
	private static final int PRECEDENCIA = 1000;

	public ClassForErrorAttributeTagProcessor(String dialectPrefix){ //1
		super(TemplateMode.HTML, dialectPrefix, null, false, NOME_TAG, true, PRECEDENCIA,true); //3
	}

	
	
	//brewer:classforerror="sku"
	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
			String attributeValue, IElementTagStructureHandler structureHandler) { //4
		
		// ${#fields.hasErrors('sku')} ? 'has-error'"
		boolean hasError = FieldUtils.hasErrors(context, attributeValue); //5
		
		if(hasError) {
			String classesExistentes = tag.getAttributeValue("class");
			structureHandler.setAttribute("class", classesExistentes + " has-error");
		}
		
	}
	
	
	

}










/*
1.Vamos passar o nome do nosso dialeto, que será 'brewer', lembra? Quando tivermos que chamar as tags, usaremos 'brewer:nomedatag()'. CUIDADO. Esse construtor tem que
ser público e o Eclipse o coloca automaticamente como protected.

2.Qual o nome da tag que esse processador vai processar.

3.Na ordem dos atributos do construtor: Tipo de template que estamos usando; nome do nosso dialeto; nome do elemento criado(não estamos criando um elemento, por isso null); 
se foi passado um prefixo para o elemento; nome da tag que esse processador vai processar; se foi passado um nome para a tag; PRECEDENCIA - Não entendi muito bem; 
Se a tag será substituída por algum código ou não. Nesse caso sim, o que significa que a tag 'brewer:classforerror' vai desaparecer no HTML e dará lugar à classe
'has-error' que será incluída no atributo 'class' do elemento.

4. Aqui vai entrar a lógica do processamento. 
	context: é a página HTML onde o a a tag está. É só para ele saber em qual HTML ele deve gerar o processamento.
	tag: tag aqui refere-se à div onde "brewer:classforerror" foi colocada. atraves desse objeto conseguimos pegar qualquer atributo e valor da div.
	attribute name: seria "classforerror"
	attribute value: seria o "sku" de classforerror="sku"
	structureHandler: vai ser usado para construir a classe no elemento.

5. Repare que eles são equivalentes.
*/