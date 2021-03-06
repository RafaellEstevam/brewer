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
1. Vai impedir o comportamento padr??o do evento 'submit' do modal, que ?? ser enviado quando o enter ?? pressionado.
2. Vai disparar a fun????o 'onModalShow' quando o modal for carregado.
3. Vai disparar a fun????o 'onModalClose' quando o modal for fechado.
4. Vai pegar os dados do atributo action do elemento form. 
5. Usando a biblioteca stringify para transformar o objeto Javascript em JSON.
6. O obj recebido ?? um ResponseEntity. Ele tem v??rios atributos, um deles ?? o responseText.
   Aqui estamos recebendo uma ??nica informa????o no corpo da requisi????o, que ?? o defaultMessage do atributo nome. Mas se tiv??ssemos
   v??rios atributos, l?? no controller ter??amos colocado 'result.getFieldErrors()' no 'ResponseEntity', ou seja, estar??amos passando
   uma lista de objetos do tipo erro. Aqui no m??todo ' onErroSalvandoEstilo()' ter??amos que acessar essa lista dentro do objeto  
   'ResponseEntity' que nos chega, dentro dessa lista acessar o default message de cada objeto 'erro' e exibir na tela. O c??digo ficaria
   mais ou menos assim:
   
   OBS: N??o esque??a que o que o controller tem que colocar dentro do 'ResponseEntity' ?? o m??todo 'result.getFieldErrors()'.
   
		function onErroSalvandoEstilo(response){
		var listaErros= response.responseJSON; (estou pegando o atributo responseJson dentro da resposta. ?? nele que fica o JSON que foi enviado pelo controlador).
		
		containerMensagemErro.removeClass('hidden');
		
		listaErros.forEach(function(erro) {
			containerMensagemErro.html('<div><span>'+erro.defaultMessage+'!<span></div>');

			//pegar o input do campo em quest??o e adicionar a classe 'has-error' do Bootstrap.
			//Como cada input do formul??rio leva como id o nome do field, ?? s?? dar um jeito de pegar o input por esse nome e adicionar a classe.
			//Acredito que seja algo como isso: form.find('#'+erro.field).addClass('has-error');	
		})
		
		
		
	}

7. O objeto que nos ?? retornado ?? exatamente o objeto que colocamos no 'ResponseEntity.ok()' l?? no controller. 

8. Quando o modal for fechado, vamos adicionar a classe 'hiden', se ela n??o estiver sendo aplicada. Quando d?? algum erro, 
   a classe hidden ?? retirada pela fun????o ' onErroSalvandoEstilo'. Temos que coloc??-la novamente para ela desaparecer.

9. Quando o modal for fechado, vamos remover a classe 'has-error', se ela estiver aplicada.
*/