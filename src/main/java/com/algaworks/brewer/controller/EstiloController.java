package com.algaworks.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.EstiloRepository;
import com.algaworks.brewer.repository.filter.EstiloFilter;
import com.algaworks.brewer.service.EstiloService;
import com.algaworks.brewer.service.exception.NomeEstiloJaCadastradoException;

/**
 * @author Rafaell Estevam
 *
 */
@Controller
@RequestMapping("/estilo")//5
public class EstiloController {
	
	
	@Autowired
	private EstiloService estiloService;
	
	@Autowired
	private EstiloRepository estiloRepository;
	
	
	
	@GetMapping("/novo")
	public ModelAndView novo(Estilo estilo) {
		return new ModelAndView("estilo/CadastroEstilo");
	}
	
	
	@RequestMapping(value="/novo", method = RequestMethod.POST)
	public ModelAndView cadastrar(@Valid Estilo estilo, BindingResult result, RedirectAttributes attributte) {
		
		if(result.hasErrors()) {
			
			return novo(estilo);
		}
		
		try {
			estiloService.salvar(estilo);
		}catch(NomeEstiloJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(estilo);
		}
		
		
		attributte.addFlashAttribute("mensagem", "Estilo salvo com sucesso!");
		return new ModelAndView("redirect:/estilo/novo");
	}
	
	
	
	@RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})//2
	public @ResponseBody ResponseEntity<?> salvar(@RequestBody @Valid Estilo estilo, BindingResult result){//1
		
		if(result.hasErrors()) {
			return ResponseEntity.badRequest().body(result.getFieldError("nome").getDefaultMessage()); //3
		}		
			
		estilo = estiloService.salvar(estilo); //6	
	
			
		return ResponseEntity.ok(estilo); //4
	
		
	}
	
	
	@GetMapping
	public ModelAndView pesquisa(EstiloFilter estiloFilter, @PageableDefault(size = 4) Pageable pageable, HttpServletRequest httpServletRequest) {
		
		ModelAndView mv = new ModelAndView("estilo/PesquisaEstilo");
			
		PageWrapper<Estilo> pagina = new PageWrapper<Estilo>(estiloRepository.filtrar(estiloFilter, pageable), httpServletRequest);
		
		mv.addObject("pagina", pagina);
		
		return mv;
	}
	
	
	
	
}




/*
1. '@RequestBody' significa: "Pegue os dados do corpo da requisição e transforme nesse objeto (estilo, nesse caso). Porém, para que o Spring consiga converter um JSON em 
 	um objeto Java, é necessário usar uma biblioteca chamada JACKSON. Então temos que colocá-la no pom.xml.

2.  'consumes = {MediaType.APPLICATION_JSON_VALUE}' informa que estamos recebendo um JSON.
 
3. Principais atributos do objeto result:
  defaultMessage  "O nome é obrigatório"
  objectName	  "estilo"
  field	          "nome"
  rejectedValue	  ""
  bindingFailure  false
  code      	  "NotBlank"
  
  Perceba que estamos enviando somente o defaultMessage como resposta.
  
  
4. Devolvendo o estilo criado (com o ID junto) para o Javascript popular os selects.  
 
5. (11.3) Fazendo o RequestMapping no controller para não termos que ficar repetindo "/estilo" RequestMapping de cada método.
  	Essa aula também explica um pouco mais sobre o ResponseEntity, @ResponseBody e @RequestBody.
  	
  	
6. O tratamento da exceção foi jogado para um controller advice. Um controller advice é uma espécie de monitor que vai ficar observando se acontece alguma exceção nos 
   controllers. Se ocorrer uma exceção num controller e não houver nele nenhum código que a trate, esse controller advice irá tratá-la. 
   Em controller/handler/ControllerAdviceException temos nosso controller advice. Ele vai levar os métodos para tratar cada exceção que pode ser disparada num controller.
   A vantagem disso é que não precisamos mais usar o try/catch aqui para tratá-la, pois o Spring vai usar o controllerAdvice para isso. Perceba que mantivemos o tratamento
   com o try/catch no método "cadastrar", pois o tratamento da exceção, nesse caso, é diferente. Ele retorna uma view ao invés de um ResponseEntity. Foi só uma demonstração
   da existência e utilização desse recurso, caso precisemos.    	
   
   
7. Como eu fiz a busca usando derivedQueries ao invés do criteria (já que tinhamos só um filtro):

   	
			if(estiloFilter.getNome() == null) {
				
				PageWrapper<Estilo> pagina = new PageWrapper<Estilo>(estiloRepository.findAll(pageable), httpServletRequest);
				mv.addObject("pagina", pagina);
	
			}else {
								
				PageWrapper<Estilo> pagina = new PageWrapper<Estilo>(estiloRepository.findByNomeContaining(estiloFilter.getNome(), pageable), httpServletRequest);
				mv.addObject("pagina", pagina);
			}
		
		return mv;
   
   
   
 */















