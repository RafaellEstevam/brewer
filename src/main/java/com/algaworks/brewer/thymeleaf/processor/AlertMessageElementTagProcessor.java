package com.algaworks.brewer.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * @author Rafaell Estevam
 * 
 * 
 * Obs: Professor chamou essa classe de 'MessageElementTagProcessor' 
 */
public class AlertMessageElementTagProcessor extends AbstractElementTagProcessor{ //1

	private static final String NOME_TAG="alertmessage";
	private static final int PRECEDENCE = 1000;
	
	public AlertMessageElementTagProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, NOME_TAG, true, null, false, PRECEDENCE); //2
	} 

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		
		//<th:block th:replace="fragments/MensagensErroValidacao :: mensagemErro"></th:block>
		//<th:block th:replace="fragments/MensagemSucessoValidacao :: mensagemSucesso"></th:block>
				
		
		IModelFactory modelFactory = context.getModelFactory();
		IModel model = modelFactory.createModel();
		
		
		model.add(modelFactory.createStandaloneElementTag("<th:block>", "th:replace", "fragments/MensagensErroValidacao :: mensagemErro"));
		model.add(modelFactory.createStandaloneElementTag("<th:block>", "th:replace", "fragments/MensagemSucessoValidacao :: mensagemSucesso"));
		
		structureHandler.replaceWith(model, true);//3
		
	}

}


/*
1. Perceba que a classe que extendemos é diferente de 'ClassForErrorAttributeTagProcessor', pois esse processador aqui é para criar um elemento HTML no template, enquanto
o outro é para incluírmos um atributo ou classe num elemento HTML que já existe no template. 
2.Na ordem dos atributos do construtor: Tipo de template que estamos usando; nome do nosso dialeto; nome da tag que esse processador vai processar; se foi passado nome 
  para a tag(true); nome do atributo(não é um atributo, então é null); não passamos nome de atributo(false);  Precedência(?) ; Se a tag será substituída por algum 
  código ou não. O padrão é true, então nem precisamos colocar. é true aqui também pois a tag 'brewer:alertmessage' vai ser substituída no HTML pelo código processado.
3. structureHandler é o cara que vai construir o elemento no template. Passamos no construtor qual elemento ele vai construir(model) e que depois que ele construir o
   elemento no template, que ele ainda tem que fazer um processamento que existe dentro desse elemento. Nesse caso é a parte do 'th:replace'. Se fosse um elemento 
   HTML estático, poderíamos passsar false; 

 */


