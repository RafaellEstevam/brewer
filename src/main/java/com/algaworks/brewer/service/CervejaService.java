package com.algaworks.brewer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.CervejaRepository;
import com.algaworks.brewer.service.event.cerveja.CervejaSalvaEvent;

/**
 * @author Rafaell Estevam
 *
 */
@Service
public class CervejaService { 

	
	@Autowired
	private CervejaRepository cervejaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	
	@Transactional //2
	public void salvar(Cerveja cerveja) {
		
		cervejaRepository.save(cerveja);
			
		publisher.publishEvent(new CervejaSalvaEvent(cerveja));//3
		
	}
	
	
	
	
	
}






















/*
 
Obs: Instrutor chamou essa classe de CadastroCervejaService. 
 
1. Ler CervejasController, item 10 e JPAConfig, item 12. Lembre-se que não basta criar o service. Temos que falar para o Spring que queremos gerenciar as transações manualmente. Caso contrário, 
   o Hibernate vai abrir transações pra tudo, inclusive para as operações de consulta.
   
2. Como desabilitamos as transações automáticas, nós próprios temos que abrir a transação antes da operação de escrita. Com a anotação @Transactional estamos falando "Olha, 
    abre uma transação quando for começar a executar esse método.
    
    
Resumo de como criar toda a parte de services corretamente:

    A. Dentro do pacote Service, criar a classe service para a classe de domínio em questão.
	B. Dentro da classe service criada, colocar a anotação @Service.
	C. Dentro da classe service criada, colocar a anotação @Autowired, para receber o repositório que vai fazer a operação.
	D. Ainda na classe service criada, construir o método que vai realizar a operação de escrita.
	E. Criar a classe "ServiceConfig" dentro do pacote "config". 
	F. Ir no AppInitializer e colocar a classe "ServiceConfig" dentro do 1º método, junto com JPAConfig. Estamos notificando o Spring que existe mais uma classe
	   de configuração relacionada a banco de dados.
	G. Precisamos ajustar o Hibernate, para que ele não faça mais as transações. Ler item 1 e 2.  




3.(15.3) Estamos publicando um evento e todos os listener que o estão escutando (em outras palavras, que estão esperando esse evento ser publicado) serão disparados.
Podemos ter um Listener fazendo uma atividade diferente. É usado quando tem que se realizar várias atividades diferentes depois de uma determinada ação. 
Ex: Depois que a cerveja for salva, redimensione a foto(1), mova para a pasta default(2) e mande um email para o usuário(3). Podemos ter cada uma dessas atividades
em um listener diferente, ou, se precisarmos que elas ocorram numa ordem específica, podemos tê-las em um mesmo listener, com cada método fazendo uma atividade. A
ordem vai ser ditada pela ordem dos métodos na classe listener. Lembrando que um evento é só uma classe que carrega alguma informação para ser usada pelos listeners.
 */
