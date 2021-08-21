package com.algaworks.brewer.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * @author Rafaell Estevam
 *
 */
public class OrderElementTagProcessor extends AbstractElementTagProcessor {

	private static final String NOME_TAG = "order";
	private static final int PRECEDENCE = 1000;

	public OrderElementTagProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, NOME_TAG, true, null, false, PRECEDENCE);
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {

		IModelFactory modelFactory = context.getModelFactory();
		IModel model = modelFactory.createModel();

		// 1
		IAttribute page = tag.getAttribute("page");
		IAttribute field = tag.getAttribute("field");
		IAttribute text = tag.getAttribute("text");
		
		

		//<th:block th:replace="fragments/Ordenacao :: order (${pagina}, 'sku', 'SKU')"></th:block>
		model.add(modelFactory.createStandaloneElementTag("<th:block>", "th:replace", 
				String.format("fragments/Ordenacao :: order (%s, %s, '%s')", page.getValue(), field.getValue(), text.getValue()))); // 2 e 3

		structureHandler.replaceWith(model, true);

	}

}










/*
 * Lembrando que esse processador é um processador de
 * elementos(ElementTagProcessor), pois vai adicionar um elemento HTML. O outro
 * tipo de processador é um processador de atributos e
 * classes(AttributeTagProcessor), ou seja, vai adicionar atributos e classes em
 * elementos HTML que já estão na página.
 * 
 * 
 * 1. Estamos pegando os atributos da tag que colocamos. Esses atributos
 * poderiam levar qqr nome lá na tag. 2. Usamos o String.format() só para
 * formatar a String que vai aqui, já que tinhamos que concatenar com variáveis
 * e ia ficar uma meleca se n fosse assim.
 *
 * 2. Não esquecer de adicionar esse processador na lista de processadores do
 * dialeto (BrewerDialect).
 * 
 * 3. Tivemos que colocar o último %s com aspas simples, pois se o texto que vier
 * possuir espaço, como "Tipo Pessoa", ele não consegue interpretar.
 *
 */
