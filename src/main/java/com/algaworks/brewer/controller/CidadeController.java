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
	 * valor que está no parâmetro 'estado' da requisição e jogue na variável
	 * 'códigoEstado'. Podíamos ter chamado de id ao invés de estado, mas o
	 * professor preferiu deixar assim para ficar mais bonito na requisição.
	 * 'defaultValue = "-1" ' significa que quando o usuário não selecionar nenhuma
	 * opção(clicar em 'selecione um estado'), o valor que vai para 'códigoEstado' é
	 * -1.
	 * 
	 * Observação importante sobre as requisições com AJAX encontra-se em
	 * cliente.combo-estado-cidade.js. Mas perceba que quando a requisição Ajax é do
	 * tipo GET, temos que pegar o parâmetro com @RequestParam. Quando é um POST,
	 * pegamos com @RequestBody.
	 * 
	 * 
	 * 2. (16.7 - ultimos 10 min) Bom, tivemos um problema aqui na hora que o
	 * JACKSON tentava transformar a lista de cidades em um JSON. Nos dava uma
	 * exceção do tipo 'could not initialize proxy - no Session'. Vamos ver o que
	 * acontece exatamente: em "cidadeRepository.findByEstadoId(codigoEstado);" é
	 * feito um JOIN entre Cidade e Estado e selecionadas somente as cidades cujo
	 * estado_id = x. Então essa lista de cidades vem com todas as informações
	 * preenchidas, inclusive com todas as informações dos estados (sem as listas,
	 * por enquanto). O problema ocorre quando o Jackson tenta transformar essa
	 * lista em JSON. Ele vai entender que cada pra cada cidade dessa lista, ele tem
	 * que preencher o objeto estado com todas as suas informações, inclusive com
	 * sua lista de cidades. Então ele tenta forçar o Spring a buscar as cidades com
	 * o estado_id=x, porém em um momento em que não existe mais sessão do
	 * EntityManager aberta. Com isso ele não consegue fazer a busca, n consegue
	 * construir os objetos e dá uma exceção. A forma de contornarmos isso é colocar
	 * a anotação @JsonIgnore no atributo "estado" da classe Cidade, indicando que
	 * não queremos que esse objeto seja incluido no JSON, quando uma cidade tive
	 * que ser transformada.
	 * 
	 * 
	 * 3.(17.1) CACHEAMENTO: O ideal é colocarmos na cache somente aquilo que não
	 * vai sofrer muita alteração, pois os dados serão trazidos do BD e vão ficar na
	 * RAM (a dica aqui é sempre observar na tela que estamos construindo o que
	 * podemos colocar em cache). Então se houver frequentes alterações nesses dados
	 * no BD, os dados na cache podem ficar inconsistentes e fazer o cache nem vai
	 * valer a pena. No nosso caso vamos colocar as cidades em cache, já que elas
	 * vão sofrer poucas alterações. O primeiro passo é colocar a
	 * anotação @Cacheable(""). Perceba que damos um nome pra ela. Esse nome é o que
	 * estamos dando para a região da memória onde esses dados estão cacheados.
	 * Dessa forma fica fácil localizá-los. Repare também que a anotação é colocada
	 * sempre num método e que ele sempre devolve um tipo de dado, preferencialmente
	 * uma lista. Então o que vai acontecer é o seguinte: o método será executado
	 * normalmente da primeira vez. Quando ele devolver a lista de cidades, esses
	 * dados serão copiados para a RAM e devolvidos ao usuário. Da próxima vez que esse
	 * método for chamado, na verdade ele nem vai ser lido. Ele vai esbarrar na
	 * anotação @Cacheable e vai buscar os dados direto na RAM. O que o Spring faz é
	 * observar se aquele método já foi chamado com aquele valor de codigoEstado. Se sim, 
	 * ele manda retornar a lista que está na RAM e que está marcado com o códigoEstado 
	 * correspondente (os dados ficam armazenados em um Map).
	 * 
	 * A. Colocar a anotação @Cacheable("") >>> do springFramework e não do Javax.Persistence 
	 * 
	 * B. No WebConfig, adicionar a anotação @EnableCaching
	 *  
	 * C. adicionar o método "cacheManager()", pois precisamos de alguem que gerencie
	 * 	  esse cache pra gente. Precisamos de um objeto que faça isso. Dentro desse
	 *    método vai entrar a implementação de um gerenciador de cache. Podemos usar o
	 *    do próprio Spring (que não é recomendado para produção) ou um mais
	 *    profissional como o Google Guava. Vamos começar com o do Spring e depois
	 *    implementamos o Guava.
	 * 
	 *    Para adicionar o gerenciador de cache do Spring: basta colocar
	 *    "return new ConcurrentMapCacheManager();". Os dados são guardados em um Map.
	 *    
	 *    
	 * 4. (17.2) INVALIDANDO O CACHE: Invalidar a cache significa apagar certas informações
	 * que estão contidas nela. O que estava acontecendo era que toda vez que uma nova cidade
	 * era cadastrada, quando iamos para uma tela que tinha o carregamento de cidades, e ela
	 * já estava na cache, a nova cidade, claro, não aparecia na lista. Então a solução é o 
	 * seguinte: toda vez que uma cidade for cadastrada, temos que apagar a cache de cidades. 
	 * Primeiramente vamos aprender como apagar toda a cache de cidades e depois apagar somente
	 * a cache relacionada a um determinado estado. 
	 * 
	 * >>> 1. Apagando toda a cache de cidades: 
	 * Usamos"@CacheEvict(value="cidades", allEntries = true)".  
	 * Com isso, estamos informando o que queremos apagar da cache(value="cidades") e se queremos apagar 
	 * tudo(allEntries=true). Perceba que a anotação foi colocada no método salvar. Então antes 
	 * mesmo de realizar o cadastro, a cache já será apagada.
	 *    
	 * 
	 * >>> 2. Apagar da cache somente as cidades de um determinado estado (06:20):
	 * Usamos @CacheEvict(value="cidades", key="#cidade.estado.id")
	 * Perceba que no método "buscarPorCodigoEstado" colocamos o atributo "key" na anotação @Cacheable, 
	 * passando o código do estado. Com esse atributo estamos identificando a lista na região da cache 
	 * chamada "cidades". Então em @CacheEvict passamos a chave, navegando pelo objeto Cidade que estamos 
	 * recebendo, e conseguimos identificar a lista que queremos apagar no ato de salvar uma cidade.  
	 * 
	 * 
	 * OBS: No final de 17.2 foi colocado o atributo "condition" no @CacheEvict. Com ele estamos falando 
	 * que ele vai tentar apagar a cache indicada se aquela condição for satisfeita. No caso, somente se
	 * a cidade passada no formulário vier com um estado preenchido.Vamos colocar essa condição num método.
	 * (mesmo aplicando o bean validation, temos que aplicar o condition, pois o bean validation só vai ser 
	 * executado na linha seguinte. Então até lá, o spring vai tentar apagar a cache mas não sabe qual lista, 
	 * já que não foi passado o código).  
	 * 
	 * 
	 */

}
