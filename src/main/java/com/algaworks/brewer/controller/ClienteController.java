package com.algaworks.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.model.TipoPessoa;
import com.algaworks.brewer.repository.ClienteRepository;
import com.algaworks.brewer.repository.EstadoRepository;
import com.algaworks.brewer.repository.filter.ClienteFilter;
import com.algaworks.brewer.service.ClienteService;
import com.algaworks.brewer.service.exception.CpfCnpjJaCadastradoException;

/**
 * @author Rafaell Estevam
 *
 */
@Controller
@RequestMapping("/cliente")
public class ClienteController {
	
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
		
	@Autowired
	private ClienteService clienteService;
	
	
	
	
	@GetMapping("/novo")
	public ModelAndView novo(Cliente cliente) {
		ModelAndView mv = new ModelAndView("cliente/CadastroCliente");
		
		mv.addObject("tiposPessoa", TipoPessoa.values());
		mv.addObject("estados", estadoRepository.findAll());
		
		return mv;
	}
	
	
	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attribute) {
				
		if(result.hasErrors()) {
			return novo(cliente);
		}
		
		try {
			clienteService.salvar(cliente);
		}catch(CpfCnpjJaCadastradoException e) {
			result.rejectValue("cpfOuCnpj", e.getMessage(), e.getMessage());
			return novo(cliente);
		}
		
		
		attribute.addFlashAttribute("mensagem", "Cliente salvo com sucesso!");
		return new ModelAndView("redirect:/cliente/novo") ;
	}
	
	
	
	@GetMapping
	public ModelAndView pesquisa(ClienteFilter clienteFilter, @PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
		
		
		ModelAndView mv = new ModelAndView("cliente/PesquisaCliente"); 
		
		PageWrapper<Cliente> pagina = new PageWrapper<Cliente>(clienteRepository.filtrar(clienteFilter, pageable), httpServletRequest);
		
		mv.addObject("pagina", pagina);
		
		return mv;
	}
	
	
	
	
}


















