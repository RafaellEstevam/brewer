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
 * 1. Recebe as requisi????es GET para essa URL
 * 
 * 2. O Spring sabe que qdo um objeto ?? passado no construror de um m??todo aqui
 * do controller, significa que n??s queremos que ele esteja dispon??vel na view.
 * por isso n??o precisamos usar o "model" para pendur??-lo.
 * 
 * 3. recebe as requisi????es POST para essa URL.
 * 
 * 4. @Valid Cerveja cerveja usa o construtor vazio da classe e os setters dos
 * atributos para construir o objeto. Tem setSku? Tem setNome? Ele vai montando
 * o objeto assim.
 *
 * 5. Como havia repeti????o de c??digo, o objeto cerveja foi passado no construtor
 * no 1?? m??todo. O m??todo vai ser chamado e o objeto cerveja, com os erros de
 * valida????o, vai ser enviado para a view.
 * 
 * 6. M??todo que faz com que os dados de uma requisi????o que est?? voltando para o
 * navegador consiga passar para a nova requisi????o(redirecionamento).
 * 
 * 7. Toda opera????o de escrita vai fazer um redirect, lembra?
 *
 * 8. (9.5) Leva o nome da view.
 *
 * 9. (9.5) As opera????es de consulta com os repositories podem ser acessadas sem
 * problemas no Controller mesmo, mas as opera????es de escrita (criar, remover,
 * alterar) ser??o jogadas para um Service, por pode haver regras de neg??cio
 * envolvido.
 *
 * 10. Perceba que jogamos a responsabilidade de cadastrar para um service.
 * Lembre-se que n??o basta criar o service. Temos que falar para o Spring que
 * queremos gerenciar as transa????es manualmente. Lembra que as transa????es s??o
 * necess??rias somente para as opera????es de escrita? Ent??o, o problema ?? que
 * como o Hibernate n??o tem como saber que tipo de opera????o vai ser feita, por
 * padr??o ele abre uma transa????o em todas as opera????es. At?? nas de consulta. E
 * isso ?? desneess??rio. Por isso, vamos no JPAconfig definir que n??s ?? que vamos
 * gerenciar as transa????es que envolvem o nosso reposit??rio. Ler item 12 de
 * JPAconfig.
 *
 * 11. Mesmo que n??o usemos, temos que colocar o 'BindingResult result', caso
 * contr??rio d?? um erro. Provavelmente ?? um bug que ainda n??o corrigiram.
 *
 * 12. (15.5)Pagina????o no cliente significa que vai ser feita uma requisi????o ao
 * servidor buscando todos os registros (findAll()) e todos esses dados ser??o
 * retornados ao browser. E no browser teremos um componente javascript que vai
 * fazer a pagina????o para n??s. A desvantagem ?? que vamos trafegar muitos dados
 * na rede e vamos alocar mais mem??ria no servidor para abrigar os dados antes
 * de enviar ao browser. Imagine se tivermos centenas de registros nessa tabela.
 * (lembre-se que o servidor faz uma requisi????o ao servidor de banco de dados e
 * dele recebe a lista, que ?? alocada em sua mem??ria. Depois, ele repassa essa
 * lista ao browser). Pagina????o no servidor ?? mais eficiente no sentido de
 * economizar mem??ria no servidor e trafegar menos dados na rede. Vou fazer a
 * requisi????o no servidor e a pagina????o vai ser feita l??. Com isso, o servidor
 * s?? aloca na mem??ria os dados referente a uma p??gina, ao inv??s de todos os
 * dados. O trabalho no servidor acaba sendo mais r??pido. A desvantagem ?? que
 * para cada p??gina ser?? feita uma nova requisi????o ao servidor. Conclus??o: Fazer
 * pagina????o no cliente pode trazer problemas se a quantidade de dados a ser
 * carregada for muito grande. Apesar de ser feita somente uma requisi????o
 * puxando todos os dados, essa primeira requisi????o pode tornar o sistema lento.
 * A pagina????o no servidor ?? mais eficiente, pois apesar de cada p??gina
 * necessitar de uma requisi????o, como os dados puxados s??o somente de uma
 * p??gina, isso continua deixando o sistema r??pido.
 * 
 * PAGINA????O: Existe uma integra????o entre o Spring MVC e o Spring Data para
 * fazer a pagina????o acontecer. Quando colocamos o par??metro "?page = x" (tem
 * que ser esse nome) no link da p??gina e outros atributos de ordena????o, eles j??
 * s??o recebidos no controller dentro da vari??vel "pageable". Por??m para isso
 * funcionar, precisamos ir l?? em webConfig e adicionar a
 * anota????o @EnableSpringDataWebSupport. Essa anota????o vai adicionar suporte a
 * algumas coisas que o Spring data tem para a parte web. Ele vai conseguir
 * traduzir os par??metros que v??m na requisi????o e construir o objeto "pageable"
 * no controller.
 * 
 * 
 * ### Pageable vai levar informa????es tanto de pagina????o quanto de ordena????o. No
 * que diz respeito ?? pagina????o, ele leva basicamente dois atributos:
 * pageNumber(n?? da p??gina solicitada) e size(qtd de registros que devem ser
 * exibidas). Quanto ?? ordena????o, ele tem o atributo sort que vai levar os
 * par??metros de ordena????o da requisi????o(ex: ?sort = sku, asc). Cada dupla desse
 * atributo sort ?? chamado de "order", onde o 1?? item da dupla ?? a propriedade a
 * ser ordenada (property) e o segundo a dire????o da ordena????o (direction). Em
 * '?sort=sku, asc' "sku" ?? a property e "asc" a direction. ###
 * 
 * A interface pageable tem alguns m??todos interessantes:
 * 
 * >>> getPageNumber(): numero da p??gina solicitada (?? o page enviado no <a>). O
 * padr??o ?? sempre 0. Assim, quando a p??gina ?? carregada pela 1??vez, sempre vem
 * a 1?? p??gina de registros. >>> getPageSize(): n??mero de registros que vou
 * exibir por p??gina. Valor default ?? 20. Podemos mandar isso na requisi????o
 * tamb??m, com o atributo "size = x".
 * 
 * >>> Tamb??m leva informa????es de ordena????o (sort) como property e direction.
 * Passamos na URL as informa????es de property e direction(ex: ?sort = sku, asc)
 * e ao chegar no controller eles s??o colocados dentro do pageable em uma
 * variavel sort(+- assim).
 *
 * 
 * 
 * 13.Podemos definir valores padr??es para todos os atributos do pageable,
 * atrav??s dessa anota????o @PageableDefault(). Pelo "size" estamos definindo que
 * a quantidade de registros em cada p??gina ser?? sempre de 2.
 * 
 * 
 * 14. (15.5) Se a aplica????o n??o envolvesse a parte de filtros, bastaria usar
 * mv.addObject("cervejas", cervejaRepository.findAll(pageable));
 *
 *
 *
 *
 *
 *
 * 
 */
