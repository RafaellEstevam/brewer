package com.algaworks.brewer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Cidade;
import com.algaworks.brewer.repository.CidadeRepository;
import com.algaworks.brewer.repository.EstadoRepository;
import com.algaworks.brewer.repository.filter.CidadeFilter;
import com.algaworks.brewer.service.CidadeService;
import com.algaworks.brewer.service.exception.NomeCidadeJaCadastradoException;

/**
 * @author Rafaell Estevam
 *
 */
@Controller
@RequestMapping("/cidade")
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private CidadeService cidadeService;

	@GetMapping("/novo")
	public ModelAndView novo(Cidade cidade) {
		ModelAndView mav = new ModelAndView("cidade/CadastroCidade");

		mav.addObject("estados", estadoRepository.findAll());

		return mav;
	}

	@Cacheable(value="cidades", key = "#codigoEstado" ) // 3 e 4.2
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Cidade> buscarPorCodigoEstado(@RequestParam(name = "estado", defaultValue = "-1") Long codigoEstado) { // 1

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {

		}

		return cidadeRepository.findByEstadoId(codigoEstado); // 2

	}

	
	@PostMapping("/novo")
	@CacheEvict(value="cidades", key= "#cidade.estado.id", condition = "#cidade.temEstado()")//4
	public ModelAndView salvar(@Valid Cidade cidade, BindingResult result, RedirectAttributes attribute) {

		if (result.hasErrors()) {
			return novo(cidade);
		}

		try {
			cidadeService.salvar(cidade);
		} catch (NomeCidadeJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());

			return novo(cidade);
		}

		attribute.addFlashAttribute("mensagem", "Cidade salva com sucesso!");

		return new ModelAndView("redirect:/cidade/novo");
	}

	@GetMapping
	public ModelAndView pesquisa(CidadeFilter cidadeFilter, @PageableDefault(size = 5) Pageable pageable,
			HttpServletRequest httpServletRequest) {
		ModelAndView mav = new ModelAndView("cidade/PesquisaCidades");

		mav.addObject("estados", estadoRepository.findAll());

		PageWrapper<Cidade> pagina = new PageWrapper<Cidade>(cidadeRepository.filtrar(cidadeFilter, pageable),
				httpServletRequest);

		mav.addObject("pagina", pagina);

		return mav;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/*
	 * 1. '@RequestParam(name = "estado") Long codigoEstado' significa "pegue o
	 * valor que est?? no par??metro 'estado' da requisi????o e jogue na vari??vel
	 * 'c??digoEstado'. Pod??amos ter chamado de id ao inv??s de estado, mas o
	 * professor preferiu deixar assim para ficar mais bonito na requisi????o.
	 * 'defaultValue = "-1" ' significa que quando o usu??rio n??o selecionar nenhuma
	 * op????o(clicar em 'selecione um estado'), o valor que vai para 'c??digoEstado' ??
	 * -1.
	 * 
	 * Observa????o importante sobre as requisi????es com AJAX encontra-se em
	 * cliente.combo-estado-cidade.js. Mas perceba que quando a requisi????o Ajax ?? do
	 * tipo GET, temos que pegar o par??metro com @RequestParam. Quando ?? um POST,
	 * pegamos com @RequestBody.
	 * 
	 * 
	 * 2. (16.7 - ultimos 10 min) Bom, tivemos um problema aqui na hora que o
	 * JACKSON tentava transformar a lista de cidades em um JSON. Nos dava uma
	 * exce????o do tipo 'could not initialize proxy - no Session'. Vamos ver o que
	 * acontece exatamente: em "cidadeRepository.findByEstadoId(codigoEstado);" ??
	 * feito um JOIN entre Cidade e Estado e selecionadas somente as cidades cujo
	 * estado_id = x. Ent??o essa lista de cidades vem com todas as informa????es
	 * preenchidas, inclusive com todas as informa????es dos estados (sem as listas,
	 * por enquanto). O problema ocorre quando o Jackson tenta transformar essa
	 * lista em JSON. Ele vai entender que cada pra cada cidade dessa lista, ele tem
	 * que preencher o objeto estado com todas as suas informa????es, inclusive com
	 * sua lista de cidades. Ent??o ele tenta for??ar o Spring a buscar as cidades com
	 * o estado_id=x, por??m em um momento em que n??o existe mais sess??o do
	 * EntityManager aberta. Com isso ele n??o consegue fazer a busca, n consegue
	 * construir os objetos e d?? uma exce????o. A forma de contornarmos isso ?? colocar
	 * a anota????o @JsonIgnore no atributo "estado" da classe Cidade, indicando que
	 * n??o queremos que esse objeto seja incluido no JSON, quando uma cidade tive
	 * que ser transformada.
	 * 
	 * 
	 * 3.(17.1) CACHEAMENTO: O ideal ?? colocarmos na cache somente aquilo que n??o
	 * vai sofrer muita altera????o, pois os dados ser??o trazidos do BD e v??o ficar na
	 * RAM (a dica aqui ?? sempre observar na tela que estamos construindo o que
	 * podemos colocar em cache). Ent??o se houver frequentes altera????es nesses dados
	 * no BD, os dados na cache podem ficar inconsistentes e fazer o cache nem vai
	 * valer a pena. No nosso caso vamos colocar as cidades em cache, j?? que elas
	 * v??o sofrer poucas altera????es. O primeiro passo ?? colocar a
	 * anota????o @Cacheable(""). Perceba que damos um nome pra ela. Esse nome ?? o que
	 * estamos dando para a regi??o da mem??ria onde esses dados est??o cacheados.
	 * Dessa forma fica f??cil localiz??-los. Repare tamb??m que a anota????o ?? colocada
	 * sempre num m??todo e que ele sempre devolve um tipo de dado, preferencialmente
	 * uma lista. Ent??o o que vai acontecer ?? o seguinte: o m??todo ser?? executado
	 * normalmente da primeira vez. Quando ele devolver a lista de cidades, esses
	 * dados ser??o copiados para a RAM e devolvidos ao usu??rio. Da pr??xima vez que esse
	 * m??todo for chamado, na verdade ele nem vai ser lido. Ele vai esbarrar na
	 * anota????o @Cacheable e vai buscar os dados direto na RAM. O que o Spring faz ??
	 * observar se aquele m??todo j?? foi chamado com aquele valor de codigoEstado. Se sim, 
	 * ele manda retornar a lista que est?? na RAM e que est?? marcado com o c??digoEstado 
	 * correspondente (os dados ficam armazenados em um Map).
	 * 
	 * A. Colocar a anota????o @Cacheable("") >>> do springFramework e n??o do Javax.Persistence 
	 * 
	 * B. No WebConfig, adicionar a anota????o @EnableCaching
	 *  
	 * C. adicionar o m??todo "cacheManager()", pois precisamos de alguem que gerencie
	 * 	  esse cache pra gente. Precisamos de um objeto que fa??a isso. Dentro desse
	 *    m??todo vai entrar a implementa????o de um gerenciador de cache. Podemos usar o
	 *    do pr??prio Spring (que n??o ?? recomendado para produ????o) ou um mais
	 *    profissional como o Google Guava. Vamos come??ar com o do Spring e depois
	 *    implementamos o Guava.
	 * 
	 *    Para adicionar o gerenciador de cache do Spring: basta colocar
	 *    "return new ConcurrentMapCacheManager();". Os dados s??o guardados em um Map.
	 *    
	 *    
	 * 4. (17.2) INVALIDANDO O CACHE: Invalidar a cache significa apagar certas informa????es
	 * que est??o contidas nela. O que estava acontecendo era que toda vez que uma nova cidade
	 * era cadastrada, quando iamos para uma tela que tinha o carregamento de cidades, e ela
	 * j?? estava na cache, a nova cidade, claro, n??o aparecia na lista. Ent??o a solu????o ?? o 
	 * seguinte: toda vez que uma cidade for cadastrada, temos que apagar a cache de cidades. 
	 * Primeiramente vamos aprender como apagar toda a cache de cidades e depois apagar somente
	 * a cache relacionada a um determinado estado. 
	 * 
	 * >>> 1. Apagando toda a cache de cidades: 
	 * Usamos"@CacheEvict(value="cidades", allEntries = true)".  
	 * Com isso, estamos informando o que queremos apagar da cache(value="cidades") e se queremos apagar 
	 * tudo(allEntries=true). Perceba que a anota????o foi colocada no m??todo salvar. Ent??o antes 
	 * mesmo de realizar o cadastro, a cache j?? ser?? apagada.
	 *    
	 * 
	 * >>> 2. Apagar da cache somente as cidades de um determinado estado (06:20):
	 * Usamos @CacheEvict(value="cidades", key="#cidade.estado.id")
	 * Perceba que no m??todo "buscarPorCodigoEstado" colocamos o atributo "key" na anota????o @Cacheable, 
	 * passando o c??digo do estado. Com esse atributo estamos identificando a lista na regi??o da cache 
	 * chamada "cidades". Ent??o em @CacheEvict passamos a chave, navegando pelo objeto Cidade que estamos 
	 * recebendo, e conseguimos identificar a lista que queremos apagar no ato de salvar uma cidade.  
	 * 
	 * 
	 * OBS: No final de 17.2 foi colocado o atributo "condition" no @CacheEvict. Com ele estamos falando 
	 * que ele vai tentar apagar a cache indicada se aquela condi????o for satisfeita. No caso, somente se
	 * a cidade passada no formul??rio vier com um estado preenchido.Vamos colocar essa condi????o num m??todo.
	 * (mesmo aplicando o bean validation, temos que aplicar o condition, pois o bean validation s?? vai ser 
	 * executado na linha seguinte. Ent??o at?? l??, o spring vai tentar apagar a cache mas n??o sabe qual lista, 
	 * j?? que n??o foi passado o c??digo).  
	 * 
	 * 
	 */

}
