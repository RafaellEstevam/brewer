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
1. '@RequestBody' significa: "Pegue os dados do corpo da requisi????o e transforme nesse objeto (estilo, nesse caso). Por??m, para que o Spring consiga converter um JSON em 
 	um objeto Java, ?? necess??rio usar uma biblioteca chamada JACKSON. Ent??o temos que coloc??-la no pom.xml.

2.  'consumes = {MediaType.APPLICATION_JSON_VALUE}' informa que estamos recebendo um JSON.
 
3. Principais atributos do objeto result:
  defaultMessage  "O nome ?? obrigat??rio"
  objectName	  "estilo"
  field	          "nome"
  rejectedValue	  ""
  bindingFailure  false
  code      	  "NotBlank"
  
  Perceba que estamos enviando somente o defaultMessage como resposta.
  
  
4. Devolvendo o estilo criado (com o ID junto) para o Javascript popular os selects.  
 
5. (11.3) Fazendo o RequestMapping no controller para n??o termos que ficar repetindo "/estilo" RequestMapping de cada m??todo.
  	Essa aula tamb??m explica um pouco mais sobre o ResponseEntity, @ResponseBody e @RequestBody.
  	
  	
6. O tratamento da exce????o foi jogado para um controller advice. Um controller advice ?? uma esp??cie de monitor que vai ficar observando se acontece alguma exce????o nos 
   controllers. Se ocorrer uma exce????o num controller e n??o houver nele nenhum c??digo que a trate, esse controller advice ir?? trat??-la. 
   Em controller/handler/ControllerAdviceException temos nosso controller advice. Ele vai levar os m??todos para tratar cada exce????o que pode ser disparada num controller.
   A vantagem disso ?? que n??o precisamos mais usar o try/catch aqui para trat??-la, pois o Spring vai usar o controllerAdvice para isso. Perceba que mantivemos o tratamento
   com o try/catch no m??todo "cadastrar", pois o tratamento da exce????o, nesse caso, ?? diferente. Ele retorna uma view ao inv??s de um ResponseEntity. Foi s?? uma demonstra????o
   da exist??ncia e utiliza????o desse recurso, caso precisemos.    	
   
   
7. Como eu fiz a busca usando derivedQueries ao inv??s do criteria (j?? que tinhamos s?? um filtro):

   	
			if(estiloFilter.getNome() == null) {
				
				PageWrapper<Estilo> pagina = new PageWrapper<Estilo>(estiloRepository.findAll(pageable), httpServletRequest);
				mv.addObject("pagina", pagina);
	
			}else {
								
				PageWrapper<Estilo> pagina = new PageWrapper<Estilo>(estiloRepository.findByNomeContaining(estiloFilter.getNome(), pageable), httpServletRequest);
				mv.addObject("pagina", pagina);
			}
		
		return mv;
   
   
   
 */















