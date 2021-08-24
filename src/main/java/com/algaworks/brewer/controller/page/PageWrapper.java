package com.algaworks.brewer.controller.page;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Rafaell Estevam
 * @param <T> 
 * 
 * Essa classe foi criada para facilitar o uso dos dados na
 * paginação. Todos os dados do objeto "Page" que precisamos expor e
 * usar no template, colocamos em métodos. Essa classe foi criada
 * principalmente para gerar a URL corretamente para ser usada nos
 * links de paginação, pois eles não enviavam os filtros (15.11).
 *
 */
public class PageWrapper<T> {

	private Page<T> page;
	private UriComponentsBuilder uriBuilder; // 1

	public PageWrapper(Page<T> page, HttpServletRequest httpServletRequest) {
		this.page = page;

		String httpUrl = httpServletRequest.getRequestURL()
				.append(httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString() : "")
				.toString().replaceAll("\\+", "%20"); // 6

		this.uriBuilder = UriComponentsBuilder.fromHttpUrl(httpUrl); // 6

	}

	public List<T> getConteudo() {
		return page.getContent();
	}

	public boolean isVazia() {
		return page.getContent().isEmpty();
	}

	public int getAtual() { // ou getPaginaAtual se preferir. Aí no thymeleaf chama como pagina.paginaAtual.
		return page.getNumber();
	}

	public boolean isPrimeira() {
		return page.isFirst();
	}

	public boolean isUltima() {
		return page.isLast();
	}

	public int getTotal() {
		return page.getTotalPages();
	}

	public String urlParaPagina(int pagina) { // Só vai ser chamado qdo o HTML for lido
		return uriBuilder.replaceQueryParam("page", pagina).build(true).encode().toUriString();// 1 e 2
	}

	public String urlParaOrdenacao(String propriedade) { // 3 Só vai ser chamado qdo o HTML for lido
		UriComponentsBuilder uriBuilderOrder = UriComponentsBuilder
				.fromUriString(uriBuilder.build(true).encode().toUriString()); // 4

		String valorSort = String.format("%s,%s", propriedade, inverterDirecao(propriedade));

		// sku,asc
		return uriBuilderOrder.replaceQueryParam("sort", valorSort).build(true).encode().toUriString();// 3.2

	}

	public String inverterDirecao(String propriedade) { // Ou opção logo abaixo
		String direcao = "asc";

		Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;

		if (order != null) {
			direcao = Sort.Direction.ASC.equals(order.getDirection()) ? "desc" : "asc";
		}

		return direcao;
	}

	public boolean descendente(String propriedade) {
		return inverterDirecao(propriedade).equals("asc");
	}

	public boolean ordenada(String propriedade) {
		Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;

		if (order == null) {
			return false;
		}

		return page.getSort().getOrderFor(propriedade) != null ? true : false;
	}

//	private String inverterDirecao(String propriedade) {
//	
//	String direcao = "asc";
//	
//	
//	if(page.getSort() !=null) {
//		Order order = page.getSort().getOrderFor(propriedade);
//		
//		if(order != null) {
//			
//			if(order.isAscending()) {
//				direcao = "desc";
//			}
//		}
//		
//	}
//	
//	
//	return direcao;
//}

}

/*
 * 1. (15.11). this.uriBuilder =
 * ServletUriComponentsBuilder.fromRequest(httpServletRequest) é o que tinhamos
 * antes de corrigir o bug da pesquisa. Basicamente vai pegar a URL da
 * requisição (que chega lá no controller) e no método "urlParaPagina" vamos
 * pegar essa url e se já tiver um parâmetro "page" ele vai substituí-lo pelo
 * numero que passamos (página) e se não tiver esse parâmetro ele vai
 * acrescentá-lo na requisição. Note que esse método só vai ser chamado quando
 * clicarmos pra mudar a página. A informação da URL com os filtros vai chegar
 * lá no controller quando o cliente clicar em "Pesquisar".
 * 
 * 
 * 2. (15.11 >> 19:10) Já sabemos que
 * "uriBuilder.replaceQueryParam("page", pagina).build(true).encode().toUriString();"
 * vai pegar o URL da requisição e vai acrescentar "&page= qqrvalor". Colocamos
 * "build(true).encode()" para que os caracteres codificados sejam decodificados
 * e a URL seja montada corretamente. Quando aplicamos o filtro valorDe ou
 * valorAte, o valor decimal colocado é codificado para ser colocado na URL.
 * Pode perceber que se colocarmos '20,00', na url vai aparecer algo como
 * 'valorAte=20%2C00'. O 20,00 foi codificado para entrar na url mas quando
 * chega aqui, não podemos deixar a nossa url ser formada com esse 20%2C00.
 * Então colocamos o 'build(true).encode()' para ele tranformar o '20%2C00' em
 * '20,00' novamente.
 * 
 * 3. (15.14) O método que monta a URL do link de paginação e o que monta a URL
 * do link de ordenação devem usar diferentes uriBuilders, caso contrário a URL
 * de um sempre vai levar os parâmetros específicos do outro no final.
 * OBSERVAÇÃO: "urlParaOrdenacao" vai vai ser lido antes que "urlParaPagina" no
 * html. Então qdo ele é lido, o uriBuilder ainda n está com o parâmetro "page".
 * Por isso podemos usar o uriBuilder para construir o uriBuilderOrder.
 * 
 * 3.2. Sempre preciso colocar ".build(true).encode().toUriString();" no final
 * para fazer funcionar.
 * 
 * ## OBSERVAÇÕES IMPORTANTES A RESPEITO DE PAGINAÇÃO E ORDENAÇÃO (15.14):
 *
 * >> Todos os links de paginação e ordenação são requisições feitas através da
 * URL. >> As URLs são montadas por métodos (aqui estão na classe PageWrapper) e
 * sempre vão levar os parâmetros da última requisição. >> O método que monta a
 * URL do link de paginação e o que monta a URL do link de ordenação devem usar
 * diferentes uriBuilders, caso contrário a URL de um sempre vai levar os
 * parâmetros específicos do outro no final.
 * 
 * 
 * 4. Formando uma URL a partir de uma outra URL(ou URI, se preferir), mas
 * passando-a no formato String. Ao invés de fazer dessa forma, poderíamos ter
 * criado o uriBuilderOrder no construtor (igual o outro uriBuilder), assim tbm
 * não haveria conflito entre eles.
 * 
 * 
 * 5. public String inverterDirecao(String propriedade) { String direcao =
 * "asc";
 * 
 * Order order = page.getSort() != null ?
 * page.getSort().getOrderFor(propriedade) : null;
 * 
 * if (order != null) {
 * 
 * direcao = Sort.Direction.ASC.equals(order.getDirection()) ? "desc" : "asc"; }
 * 
 * return direcao; }
 * 
 * 
 * 6.Quando é submetido um formulário com "espaco" em uma QUERY_PARAM (um
 * atributo qqr da requisição como nome="becks long"), ele n consegue converter
 * esse espaço e coloca um "+" no lugar (nome=becks+long). Porém esse "+" não
 * permitido lá no ServletUriComponentsBuilder, então ele dá uma exceção, não
 * conseguindo pegar a URL. O que tivemos que fazer foi: Ao inves de montar o
 * UriBuilder a partir da requisição de forma quase automática, vamos pegar a
 * URL da requisição e substituir na mão o "+" por um "espaço", que no UTF8 é
 * representado por um "%20". Isso é um bug do Spring na vdd. Perceba que se
 * mandarmos um número decimal no formulário, ele consegue converter a "," para
 * "%2C" mas o caractere "espaço" ele não converte para "%20".
 * 
 * O que tinhamos antes era :
 * 
 * this.uriBuilder =
 * ServletUriComponentsBuilder.fromRequest(httpServletRequest);//1
 * 
 * Observações:
 * 
 * 1. O getRequestURL() retorna só "localhost:8080/brewer/cervejas". 2. A parte
 * da consulta, que começa a partir de "?", se chama QUERY_STRING. 3. Cada
 * atributo dessa QUERY_STRING se chama QUERY_PARAM (ex: nome=becks). 4. "\\+"
 * vai pegar todo caractere "+" da String passada. 5. "%20" é o código que ele
 * vai entender para "espaço".
 * 
 * 
 * 
 * 
 * 
 */
