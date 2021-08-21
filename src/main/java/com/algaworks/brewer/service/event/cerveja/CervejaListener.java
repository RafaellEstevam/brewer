package com.algaworks.brewer.service.event.cerveja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.algaworks.brewer.storage.FotoStorage;

/**
 * @author Rafaell Estevam
 *
 */
@Component //1
public class CervejaListener {
	
	
	@Autowired
	private FotoStorage fotoStorage;
	
	
	@EventListener(condition = "#evento.temFoto()")//2
	public void cervejaSalva(CervejaSalvaEvent evento) {
		
		fotoStorage.salvar(evento.getCerveja().getFoto());
			
	}
	
}



/*
1. Não esquecer de colocar essa anotação, se não a classe não é instanciada pelo Spring quando a app sobe e assim esse listener não vai ser disparado.

2.(15.3) Esse listener só vai disparar se a condição for true. Veja que passamos um método que criamos dentro do evento (CervejaSalvaEvent). Esse método vai 
verificar se o nome da foto está vazio ou não. Então o listener só vai disparar se o atributo foto não estiver vazio. É a mesma coisa se colocassemos um if 
nesse método "cervejaSalva" verificando se evento.getCerveja().getFoto() != null.   

*/