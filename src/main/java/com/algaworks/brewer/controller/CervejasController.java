package com.algaworks.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.Origem;
import com.algaworks.brewer.model.Sabor;
import com.algaworks.brewer.repository.CervejaRepository;
import com.algaworks.brewer.repository.EstiloRepository;
import com.algaworks.brewer.repository.filter.CervejaFilter;
import com.algaworks.brewer.service.CervejaService;

/**
 * @author Rafaell Estevam
 *
 */
@Controller
@RequestMapping("/cervejas")
public class CervejasController {

	@Autowired
	private CervejaService cervejaService;

	@Autowired
	private EstiloRepository estiloRepository;

	@Autowired
	private CervejaRepository cervejaRepository;

	@GetMapping("/novo") // 1
	public ModelAndView novo(Cerveja cerveja) { // 2
		ModelAndView mv = new ModelAndView("cerveja/CadastroCerveja"); // 8

		mv.addObject("sabores", Sabor.values());
		mv.addObject("estilos", estiloRepository.findAll()); // 9
		mv.addObject("origens", Origem.values());
		return mv;
	}

	@RequestMapping(value = "/novo", method = RequestMethod.POST) // 3
	public ModelAndView cadastrar(@Valid Cerveja cerveja, BindingResult result, RedirectAttributes attribute) { // 4

		if (result.hasErrors()) {

			return novo(cerveja); // 5
		}

		cervejaService.salvar(cerveja);// 10

		attribute.addFlashAttribute("mensagem", "Cerveja salva com sucesso!"); // 6
		return new ModelAndView("redirect:/cervejas/novo"); // 7
	}

	@GetMapping
	public ModelAndView pagePesquisa(CervejaFilter cervejaFilter, BindingResult result,
			@PageableDefault(size = 2) Pageable pageable, HttpServletRequest httpServletRequest) { // 11, 12, 13
		ModelAndView mv = new ModelAndView("cerveja/PesquisaCervejas");

		mv.addObject("estilos", estiloRepository.findAll());
		mv.addObject("sabores", Sabor.values());
		mv.addObject("origens", Origem.values());

		PageWrapper<Cerveja> pagina = new PageWrapper<Cerveja>(cervejaRepository.filtrar(cervejaFilter, pageable),
				httpServletRequest); // (15.11)

		mv.addObject("pagina", pagina); // 14

		return mv;
	}

}

































/*
 * 1. Recebe as requisições GET para essa URL
 * 
 * 2. O Spring sabe que qdo um objeto é passado no construror de um método aqui
 * do controller, significa que nós queremos que ele esteja disponível na view.
 * por isso não precisamos usar o "model" para pendurá-lo.
 * 
 * 3. recebe as requisições POST para essa URL.
 * 
 * 4. @Valid Cerveja cerveja usa o construtor vazio da classe e os setters dos
 * atributos para construir o objeto. Tem setSku? Tem setNome? Ele vai montando
 * o objeto assim.
 *
 * 5. Como havia repetição de código, o objeto cerveja foi passado no construtor
 * no 1º método. O método vai ser chamado e o objeto cerveja, com os erros de
 * validação, vai ser enviado para a view.
 * 
 * 6. Método que faz com que os dados de uma requisição que está voltando para o
 * navegador consiga passar para a nova requisição(redirecionamento).
 * 
 * 7. Toda operação de escrita vai fazer um redirect, lembra?
 *
 * 8. (9.5) Leva o nome da view.
 *
 * 9. (9.5) As operações de consulta com os repositories podem ser acessadas sem
 * problemas no Controller mesmo, mas as operações de escrita (criar, remover,
 * alterar) serão jogadas para um Service, por pode haver regras de negócio
 * envolvido.
 *
 * 10. Perceba que jogamos a responsabilidade de cadastrar para um service.
 * Lembre-se que não basta criar o service. Temos que falar para o Spring que
 * queremos gerenciar as transações manualmente. Lembra que as transações são
 * necessárias somente para as operações de escrita? Então, o problema é que
 * como o Hibernate não tem como saber que tipo de operação vai ser feita, por
 * padrão ele abre uma transação em todas as operações. Até nas de consulta. E
 * isso é desneessário. Por isso, vamos no JPAconfig definir que nós é que vamos
 * gerenciar as transações que envolvem o nosso repositório. Ler item 12 de
 * JPAconfig.
 *
 * 11. Mesmo que não usemos, temos que colocar o 'BindingResult result', caso
 * contrário dá um erro. Provavelmente é um bug que ainda não corrigiram.
 *
 * 12. (15.5)Paginação no cliente significa que vai ser feita uma requisição ao
 * servidor buscando todos os registros (findAll()) e todos esses dados serão
 * retornados ao browser. E no browser teremos um componente javascript que vai
 * fazer a paginação para nós. A desvantagem é que vamos trafegar muitos dados
 * na rede e vamos alocar mais memória no servidor para abrigar os dados antes
 * de enviar ao browser. Imagine se tivermos centenas de registros nessa tabela.
 * (lembre-se que o servidor faz uma requisição ao servidor de banco de dados e
 * dele recebe a lista, que é alocada em sua memória. Depois, ele repassa essa
 * lista ao browser). Paginação no servidor é mais eficiente no sentido de
 * economizar memória no servidor e trafegar menos dados na rede. Vou fazer a
 * requisição no servidor e a paginação vai ser feita lá. Com isso, o servidor
 * só aloca na memória os dados referente a uma página, ao invés de todos os
 * dados. O trabalho no servidor acaba sendo mais rápido. A desvantagem é que
 * para cada página será feita uma nova requisição ao servidor. Conclusão: Fazer
 * paginação no cliente pode trazer problemas se a quantidade de dados a ser
 * carregada for muito grande. Apesar de ser feita somente uma requisição
 * puxando todos os dados, essa primeira requisição pode tornar o sistema lento.
 * A paginação no servidor é mais eficiente, pois apesar de cada página
 * necessitar de uma requisição, como os dados puxados são somente de uma
 * página, isso continua deixando o sistema rápido.
 * 
 * PAGINAÇÃO: Existe uma integração entre o Spring MVC e o Spring Data para
 * fazer a paginação acontecer. Quando colocamos o parâmetro "?page = x" (tem
 * que ser esse nome) no link da página e outros atributos de ordenação, eles já
 * são recebidos no controller dentro da variável "pageable". Porém para isso
 * funcionar, precisamos ir lá em webConfig e adicionar a
 * anotação @EnableSpringDataWebSupport. Essa anotação vai adicionar suporte a
 * algumas coisas que o Spring data tem para a parte web. Ele vai conseguir
 * traduzir os parâmetros que vêm na requisição e construir o objeto "pageable"
 * no controller.
 * 
 * 
 * ### Pageable vai levar informações tanto de paginação quanto de ordenação. No
 * que diz respeito à paginação, ele leva basicamente dois atributos:
 * pageNumber(nº da página solicitada) e size(qtd de registros que devem ser
 * exibidas). Quanto à ordenação, ele tem o atributo sort que vai levar os
 * parâmetros de ordenação da requisição(ex: ?sort = sku, asc). Cada dupla desse
 * atributo sort é chamado de "order", onde o 1º item da dupla é a propriedade a
 * ser ordenada (property) e o segundo a direção da ordenação (direction). Em
 * '?sort=sku, asc' "sku" é a property e "asc" a direction. ###
 * 
 * A interface pageable tem alguns métodos interessantes:
 * 
 * >>> getPageNumber(): numero da página solicitada (é o page enviado no <a>). O
 * padrão é sempre 0. Assim, quando a página é carregada pela 1ºvez, sempre vem
 * a 1º página de registros. >>> getPageSize(): número de registros que vou
 * exibir por página. Valor default é 20. Podemos mandar isso na requisição
 * também, com o atributo "size = x".
 * 
 * >>> Também leva informações de ordenação (sort) como property e direction.
 * Passamos na URL as informações de property e direction(ex: ?sort = sku, asc)
 * e ao chegar no controller eles são colocados dentro do pageable em uma
 * variavel sort(+- assim).
 *
 * 
 * 
 * 13.Podemos definir valores padrões para todos os atributos do pageable,
 * através dessa anotação @PageableDefault(). Pelo "size" estamos definindo que
 * a quantidade de registros em cada página será sempre de 2.
 * 
 * 
 * 14. (15.5) Se a aplicação não envolvesse a parte de filtros, bastaria usar
 * mv.addObject("cervejas", cervejaRepository.findAll(pageable));
 *
 *
 *
 *
 *
 *
 * 
 */
