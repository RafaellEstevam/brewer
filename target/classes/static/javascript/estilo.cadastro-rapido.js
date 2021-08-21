var Brewer = Brewer || {};


Brewer.EstiloCadastroRapido = (function() {
	
	function EstiloCadastroRapido(){
		this.modal = $('#modalCadastroRapidoEstilo');
		this.inputNomeEstilo = $('#nomeEstilo');
		this.form = this.modal.find('form');
		this.botaoSalvar = this.modal.find('.js-modal-cadastro-estilo-salvar-btn')
		this.url = this.form.attr('action');//4 
		this.containerMensagemErro = $('.js-mensagem-cadastro-rapido-estilo');
	}

	
	EstiloCadastroRapido.prototype.iniciar = function() {
		this.form.on('submit', function(event){event.preventDefault()});//1
		this.modal.on('shown.bs.modal', onModalShow.bind(this));//2
		this.modal.on('hide.bs.modal', onModalClose.bind(this));//3
		this.botaoSalvar.on('click', onBotaoSalvarClick.bind(this)); // aula 12.3
		
	}
	
	function onModalShow() {
		this.inputNomeEstilo.focus();
	}
	
	
	function onModalClose(){
		this.inputNomeEstilo.val('');
		this.containerMensagemErro.addClass('hidden');//8
		this.form.find(".form-group").removeClass('has-error'); //9
	}
	
	
	
	function onBotaoSalvarClick() {
		var nomeEstilo = inputNomeEstilo.val().trim();
		
		$.ajax({
			url: this.url,
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({nome: nomeEstilo}), //5
			error: onErroSalvandoEstilo.bind(this),
			success: onEstiloSalvo.bind(this)
		});
	}
	
	
	
	function onErroSalvandoEstilo(response) { //6
		var mensagemErro = response.responseText;

		
		this.containerMensagemErro.removeClass('hidden');
		this.containerMensagemErro.html('<span>'+mensagemErro+'!<span>');
		this.form.find('.form-group').addClass('has-error');	
		
	}
	
	
	function onEstiloSalvo(estilo) { //7
		var selectEstilo = $('#estilo');
		
		selectEstilo.append('<option value='+ estilo.id + '>' + estilo.nome + '</option>');
		selectEstilo.val(estilo.id);
		this.modal.modal('hide');
	}
	
	
	
	return EstiloCadastroRapido; //retorna o construtor

}());

$(function() {
	var estiloCadastroRapido = new Brewer.EstiloCadastroRapido();
	estiloCadastroRapido.iniciar();
	
	
	
	
	
});











/*
Aula 11.2
1. Vai impedir o comportamento padrão do evento 'submit' do modal, que é ser enviado quando o enter é pressionado.
2. Vai disparar a função 'onModalShow' quando o modal for carregado.
3. Vai disparar a função 'onModalClose' quando o modal for fechado.
4. Vai pegar os dados do atributo action do elemento form. 
5. Usando a biblioteca stringify para transformar o objeto Javascript em JSON.
6. O obj recebido é um ResponseEntity. Ele tem vários atributos, um deles é o responseText.
   Aqui estamos recebendo uma única informação no corpo da requisição, que é o defaultMessage do atributo nome. Mas se tivéssemos
   vários atributos, lá no controller teríamos colocado 'result.getFieldErrors()' no 'ResponseEntity', ou seja, estaríamos passando
   uma lista de objetos do tipo erro. Aqui no método ' onErroSalvandoEstilo()' teríamos que acessar essa lista dentro do objeto  
   'ResponseEntity' que nos chega, dentro dessa lista acessar o default message de cada objeto 'erro' e exibir na tela. O código ficaria
   mais ou menos assim:
   
   OBS: Não esqueça que o que o controller tem que colocar dentro do 'ResponseEntity' é o método 'result.getFieldErrors()'.
   
		function onErroSalvandoEstilo(response){
		var listaErros= response.responseJSON; (estou pegando o atributo responseJson dentro da resposta. É nele que fica o JSON que foi enviado pelo controlador).
		
		containerMensagemErro.removeClass('hidden');
		
		listaErros.forEach(function(erro) {
			containerMensagemErro.html('<div><span>'+erro.defaultMessage+'!<span></div>');

			//pegar o input do campo em questão e adicionar a classe 'has-error' do Bootstrap.
			//Como cada input do formulário leva como id o nome do field, é só dar um jeito de pegar o input por esse nome e adicionar a classe.
			//Acredito que seja algo como isso: form.find('#'+erro.field).addClass('has-error');	
		})
		
		
		
	}

7. O objeto que nos é retornado é exatamente o objeto que colocamos no 'ResponseEntity.ok()' lá no controller. 

8. Quando o modal for fechado, vamos adicionar a classe 'hiden', se ela não estiver sendo aplicada. Quando dá algum erro, 
   a classe hidden é retirada pela função ' onErroSalvandoEstilo'. Temos que colocá-la novamente para ela desaparecer.

9. Quando o modal for fechado, vamos remover a classe 'has-error', se ela estiver aplicada.
*/