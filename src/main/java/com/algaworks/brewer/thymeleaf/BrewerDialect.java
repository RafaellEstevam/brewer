package com.algaworks.brewer.thymeleaf;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import com.algaworks.brewer.thymeleaf.processor.AlertMessageElementTagProcessor;
import com.algaworks.brewer.thymeleaf.processor.ClassForErrorAttributeTagProcessor;
import com.algaworks.brewer.thymeleaf.processor.OrderElementTagProcessor;
import com.algaworks.brewer.thymeleaf.processor.PaginationElementTagProcessor;

/**
 * @author Rafaell Estevam
 *
 */
public class BrewerDialect extends AbstractProcessorDialect {

	
	public BrewerDialect() {//3
		super("Algaworks Brewer", "brewer", StandardDialect.PROCESSOR_PRECEDENCE);//1
	}

	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {//2
		Set<IProcessor> processadores = new HashSet<IProcessor>();
		
		processadores.add(new ClassForErrorAttributeTagProcessor(dialectPrefix));
		processadores.add(new AlertMessageElementTagProcessor(dialectPrefix));
		processadores.add(new OrderElementTagProcessor(dialectPrefix));
		processadores.add(new PaginationElementTagProcessor(dialectPrefix));
		
		return processadores;
	}

}

























/*
1. Na ordem do contrutor: nome do nosso dialeto; prefixo dele (que vai ser usado lá no template para chamar as tags); esse precedence que temos que colocar pois a 
documentação manda.
2. Aqui vamos passar a lista de processadores que esse dialeto tem (na verdade é conjunto e não lista). Cada processador que criamos, temos que vir aqui e adicionar
nessa lista.
3. ATENÇÃO" O CONSTRUTOR TEM QUE SER PÚBLICO. O ECLIPSE O COLOCA AUTOMATICAMENTE COMO PROTECTED. 
 
*/
