var Brewer = Brewer || {};


Brewer.mascaraCpfCnpj = (function() {
	
	
	function mascaraCpfCnpj(){
		this.radioTipoPessoa = $('.js-radio-tipo-pessoa');	
		this.labelCpfCnpj = $('[for=cpfOuCnpj]');
		this.inputCpfCnpj = $('#cpfOuCnpj');
	}
	
	
	mascaraCpfCnpj.prototype.iniciar = function(){
		this.radioTipoPessoa.on('change', onTipoPessoaAlterado.bind(this));
		var tipoPessoaSelecionada = this.radioTipoPessoa.filter(':checked')[0];//1
		
		if(tipoPessoaSelecionada){
			aplicarMascara.call(this, $(tipoPessoaSelecionada)); //2 e 3
		}
		
		
	}
	
	function onTipoPessoaAlterado(evento){
		
		var tipoPessoaSelecionada = $(evento.currentTarget);

		aplicarMascara.call(this, $(tipoPessoaSelecionada));//2
		
		//limpando o input caso tenha algo
		this.inputCpfCnpj.val('');
	
		
	}
	

	function aplicarMascara(tipoPessoaSelecionada){
		//Troca o label para CPF ou CNPJ de acordo com o "data-documento" do tipo pessoa selecionada
		this.labelCpfCnpj.text(tipoPessoaSelecionada.data('documento'));
		
		//Aplicando a mascara de cpf ou cnpj no input 
		this.inputCpfCnpj.mask(tipoPessoaSelecionada.data('mascara'));
		
		//tirando o disabled do input
		this.inputCpfCnpj.removeAttr('disabled');
	}
	
	
	
	
	
	return mascaraCpfCnpj;
	
}());


$(function() {
	var mascaraCpfCnpj = new Brewer.mascaraCpfCnpj();
	mascaraCpfCnpj.iniciar();
	
	
	
});



/*
1. (16.9) "this.radioTipoPessoa" nos retorna o conjunto de radios. "filter(':checked')" vai selecionar desse conjunto os que estão 'checked só que vai retorná-los num array
 (já que ele entende que pode-se ter mais de um radio checado). Por isso colocamos [0], para pegar o primeiro checked desse array(vai ter somente um mesmo).
 
2. (16.9 +-20MIN) Como a função 'data' é uma função do Jquery, tivemos que transformar a variável 'tipoPessoaSelecionada' em um objeto Jquery, para que pudessemos usar a função.

3. Criamos essa função pois quando a página recarregava depois de um salvar com erros de validação, o campo CNPJ/CPF ficava desabilitado e sem valor, mesmo quando 
era passado um valor no input na hora de salvar. 



 */



