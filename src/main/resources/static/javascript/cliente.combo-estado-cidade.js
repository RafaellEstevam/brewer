//16.7
var Brewer = Brewer || {};


Brewer.ComboEstado = (function() {
	
	function ComboEstado() {
		this.selectEstado = $('#estado');
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}
	
	
	
	ComboEstado.prototype.iniciar = function() {
		this.selectEstado.on('change', onEstadoAlterado.bind(this));
	}
	
	
	
	function onEstadoAlterado() {
		this.emitter.trigger('alterado', this.selectEstado.val()); //disparando um evento que será ouvido ali em combo cidade.
	}
	
	
	return ComboEstado;
	
}());






Brewer.ComboCidade = (function() {
	
	function ComboCidade(comboEstado) {
		this.comboEstado = comboEstado;
		this.selectCidade = $('#cidade');
		this.imgLoading = $('.js-img-loading');
		this.inputHiddenCidade = $('#inputHiddenCidadeSelecionada');
	}
	
	
	
	ComboCidade.prototype.iniciar = function() {
		reset.call(this); //5
		
		this.comboEstado.on('alterado', onEstadoAlterado.bind(this));
		var codigoEstado = this.comboEstado.selectEstado.val(); //6
		
		inicializarCidades.call(this, codigoEstado); 
	}
	
	
	
	function onEstadoAlterado(evento, codigoEstado) {
		this.inputHiddenCidade.val(''); // apagando o valor do input hidden cidade
		
		inicializarCidades.call(this, codigoEstado); 
			
	}
	
	
	function inicializarCidades(codigoEstado) {
		if(codigoEstado){
			
			var requisicao = $.ajax({ //2
				method:'GET',
				url: this.selectCidade.data('url'),
				contentType: 'application/json',
				data: {'estado': codigoEstado}, //1
				beforeSend: iniciarRequisicao.bind(this),
				complete: finalizarRequisicao.bind(this)
			
			});
			
			requisicao.done((onBuscarCidadesFinalizado.bind(this)));
		}else{
			reset.call(this);
			
		}
	}



	function reset() {
		this.selectCidade.html('<option>Selecione a cidade</option>');
		this.selectCidade.val('');
		this.selectCidade.attr('disabled', 'disabled');
	}
	
	function iniciarRequisicao() {
		reset.call(this); //6
		this.imgLoading.show();//4
	}
	
	
	function finalizarRequisicao() {
		this.imgLoading.hide();//4
	}
	
	
	
	function onBuscarCidadesFinalizado(cidades) {
		var codigoCidadeSelecionada =  this.inputHiddenCidade.val();
		
			var options = []
			cidades.forEach(function(cidade) {
				
				options.push('<option value='+cidade.id+'>'+cidade.nome+'</option>'); 		
				
			});
			
			
			this.selectCidade.html(options.join('')); //3
			this.selectCidade.removeAttr('disabled');
			
			if(codigoCidadeSelecionada){
				this.selectCidade.val(codigoCidadeSelecionada);
			}
			
		}
	
	return ComboCidade;
	
}());



$(function() {
	var comboEstado = new Brewer.ComboEstado();
	comboEstado.iniciar();
	
	var comboCidade = new Brewer.ComboCidade(comboEstado);
	comboCidade.iniciar();
	
});


/*

1. Professor chamou o parâmetro de estado ao invés de id só pra ficar melhor quando aparecer na requisição.

2. Observação importante sobre requisições POST e GET via ajax: Quando fazemos um POST via ajax, precisamos 
   usar o 'JSON.stringify({}). Como os dados estão sendo enviados no corpo da requisição (atributo data),
   temos que capturá-los lá no controller com @RequestBody. Quando fazemos um GET via ajax, NÃO precisamos 
   usar o 'JSON.stringify({}). Como os dados são enviados como parâmetros na URL, temos que capturá-los 
   lá no controller com @RequestParam. Detalhe que só podemos enviar um parâmetro. Se colocarmos 2 já n
   funcionar por algum motivo q ainda n sei.

3. Coloca cada item do array um embaixo do outro, separando-os por um espaco vazio. Se fosse .join(',') colocaria-os
   um embaixo do outro mas com uma vírgula entre cada linha. 
 
4. .show() é uma função do Jquery que coloca no elemento envolvido o código style= "display:block", fazendo o elemento
aparecer. O .hide() coloca um style= "display:none", fazendo o elemento ficar escondido. É como se colocassemos a classe 
hidden.

5. Vai colocar o disabled no select cidade. Poderíamos ter colocado o disabled direto no html tbem.

6.Em this.comboEstado.selectEstado.val(), usando o 'this.comboEstado' temos acesso à function comboEstado e todos os seus atributos 
e métodos. Por isso conseguimos acessar a variável 'selectEstado' e pegamos o seu valor. Obs: jogamos esse 'this.comboEstado.selectEstado.val()'
para a variável 'codigoEstado' na linha de cima.

Obs. (16.9 +-44:14) Primeiro vamos explicar o contexto: Estamos tentando fazer com que o select cidade não perca a cidade 
selecionada quando a página é recarregada, depois de um erro de validação. Tudo isso foi abordado na aula 16.9. 

SOMENTE PARA CONHECIMENTO, POIS O CÓDIGO SEGUINTE FOI RETIRADO: Em onEstadoAlterado.call(this, undefined, this.comboEstado.selectEstado.val());
tivemos que colocar undefined e passar o código do estado pois o nome do método nos obriga a passar um evento e um
código de estado  'function onEstadoAlterado(evento, codigoEstado) {}'. O this é do próprio call, para que possamos acessar
os atributos mas perceba que passamos o evento como undefined. Foi somente por causa disso. A idéia de chamar esse método 
nesse ponto é para que haja um recarregamento das cidades no select, e então definiremos o val do select cidade com a cidade 
que foi selecionada antes de o usuário enviar e ocorrer o recarregamento.
 
 */
