package com.algaworks.brewer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.algaworks.brewer.model.Cidade;
import com.algaworks.brewer.repository.CidadeRepository;

/**
 * @author Rafaell Estevam
 *
 */
@Controller
@RequestMapping("/cidade")
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;

	@RequestMapping("/novo")
	public String novo(Cidade cidade) {
		return "cidade/CadastroCidade";
	}

	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Cidade> buscarPorCodigoEstado(
			@RequestParam(name = "estado", defaultValue = "-1") Long codigoEstado) { // 1
		
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		
		}
		
		return cidadeRepository.findByEstadoId(codigoEstado); //2

	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * 1. '@RequestParam(name = "estado") Long codigoEstado' significa "pegue o
	 * valor que está no parâmetro 'estado' da requisição e jogue na variável
	 * 'códigoEstado'. Podíamos ter chamado de id ao invés de estado, mas o
	 * professor preferiu deixar assim para ficar mais bonito na requisição.
	 * 'defaultValue = "-1" ' significa que quando o usuário não selecionar nenhuma
	 * opção(clicar em 'selecione um estado'), o valor que vai para 'códigoEstado' é
	 * -1.
	 * 
	 * Observação importante sobre as requisições com AJAX encontra-se em
	 * cliente.combo-estado-cidade.js. Mas perceba que quando a requisição Ajax é do tipo GET, temos que pegar o parâmetro com @RequestParam.
	 * Quando é um POST, pegamos com @RequestBody.
	 * 
	 * 
	 * 2. (16.7 - ultimos 10 min) Bom, tivemos um problema aqui na hora que o JACKSON tentava transformar a lista de cidades em um JSON. Nos dava
	 * uma exceção do tipo 'could not initialize proxy - no Session'. Vamos ver o que acontece exatamente: em "cidadeRepository.findByEstadoId(codigoEstado);"
	 * é feito um JOIN entre Cidade e Estado e selecionadas somente as cidades cujo estado_id = x. Então essa lista de cidades vem com todas as informações
	 * preenchidas, inclusive com todas as informações dos estados (sem as listas, por enquanto). O problema ocorre quando o Jackson tenta transformar essa lista 
	 * em JSON. Ele vai entender que cada pra cada cidade dessa lista, ele tem que preencher o objeto estado com todas as suas informações, inclusive com sua lista
	 * de cidades. Então ele tenta forçar o Spring a buscar as cidades com o estado_id=x, porém em um momento em que não existe mais sessão do EntityManager aberta.
	 * Com isso ele não consegue fazer a busca, n consegue construir os objetos e dá uma exceção. A forma de contornarmos isso é colocar a anotação @JsonIgnore
	 * no atributo "estado" da classe Cidade, indicando que não queremos que esse objeto seja incluido no JSON, quando uma cidade tive que ser transformada.         
	 * 
	 * 
	 */

}