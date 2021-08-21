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
public class PaginationElementTagProcessor extends AbstractElementTagProcessor {

	private static final String NOME_TAG = "pagination";
	private static final int PRECEDENCE = 1000;

	public PaginationElementTagProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, NOME_TAG, true, null, false, PRECEDENCE);
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {

		IModelFactory modelFactory = context.getModelFactory();
		IModel model = modelFactory.createModel();

		
		IAttribute page = tag.getAttribute("page");

		//<th:block th:replace="fragments/Paginacao :: paginacao (${pagina})"></th:block>
		model.add(modelFactory.createStandaloneElementTag("<th:block>", "th:replace", String.format("fragments/Paginacao :: paginacao (%s)", page.getValue())));

		structureHandler.replaceWith(model, true);

	} 
}

/*
 * Ver OrderElementTagProcessor para explicações. É igual e esse aqui foi baseado naquele.  
 * 
 * Não esquecer de incluir esse processador na lista de processadores do dialeto.
 */

