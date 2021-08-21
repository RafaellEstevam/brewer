package com.algaworks.brewer.model.validation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import com.algaworks.brewer.model.Cliente;

/**
 * @author Rafaell Estevam
 * Vamos colocar a sequência que eu quero que ele faça as validaçoes em Cliente.class.
 */
public class ClienteGroupSequenceProvider implements DefaultGroupSequenceProvider<Cliente>{

	@Override
	public List<Class<?>> getValidationGroups(Cliente cliente) {
		
		List<Class<?>> grupos = new ArrayList<>(); //1
		
		grupos.add(Cliente.class);//2
		
		
		if(isPessoaSelecionada(cliente)) {
			grupos.add(cliente.getTipoPessoa().getGrupo()); //3
		}
		
		return grupos;
	}

	
	private boolean isPessoaSelecionada(Cliente cliente) {
		return cliente != null && cliente.getTipoPessoa() != null;
	}

}


/*(16.9)
1. Os grupos que eu colocar nessa lista são os que serão validados. A validação vai acontecer na ordem que eu escrever e da forma que eu especificar, ou seja, posso
colocar aqui qqr regra de negócio que eu queira. O objeto cliente é o mesmo que chega no controller para ser validado.

2. Estamos dizendo: "quero que vc valide as anotações de validação lá em Cliente.class que não tem grupo nenhum informado". Então ele vai validar todos os atributos
menos o cpfOuCnpj. Ele só vai passar para a próxima validação (linha 24 a 26) quando não tiver mais problemas de validação nesse primeiro cj de atributos(linha 21).

3. Estamos dizendo que a próxima validação que ele vai fazer é cuja anotação seja igual ao retorno de 'cliente.getTipoPessoa().getGrupo()'. Esse código vai retornar
'CpfGroup.class' ou 'CnpjGroup.class.', dependendo do tipo de pessoa dentro do objeto cliente. Assim, na hora de validar ele vai ler a anotação de validação ou de 
cpf ou de cnpj.

*/