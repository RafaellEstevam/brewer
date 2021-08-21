var Brewer = Brewer || {};

Brewer.MaskMoney = (function() {
	
	function MaskMoney() {
		this.decimal = $('.js-decimal');
		this.plain = $('.js-plain');
	}
	
	MaskMoney.prototype.enable = function() {
		this.decimal.maskMoney({ decimal: ',', thousands: '.' });
		this.plain.maskMoney({ precision: 0, thousands: '.' });
	}
	
	return MaskMoney; //retorna o construtor
	
}());



Brewer.MaskPhoneNumber = (function() {
	
	function MaskPhoneNumber(){
		this.inputPhoneNumber = $('.js-phone-number');
	}
	
	
	MaskPhoneNumber.prototype.enable = function() {
		
		var maskBehavior = function (val) {
		 return val.replace(/\D/g, '').length === 11 ? '(00) 00000-0000' : '(00) 0000-00009';
		};
		
		var options = {
			onKeyPress: function(val, e, field, options) {
				field.mask(maskBehavior.apply({}, arguments), options);
		 	}
		};
		
		
		this.inputPhoneNumber.mask(maskBehavior, options);
		
		
	}
	
	return MaskPhoneNumber;
	
}());


Brewer.mascaraCep = (function() {

	
	function mascaraCep() {
		this.inputCep = $('#cep');
	}
	
	
	mascaraCep.prototype.iniciar = function() {
		this.inputCep.mask('00000-000');
	};
	
	return mascaraCep;
	
}());


$(function() {
	var maskMoney = new Brewer.MaskMoney();
	maskMoney.enable();
	
	var maskPhoneNumber = new Brewer.MaskPhoneNumber();
	maskPhoneNumber.enable();
	
	var mascaraCep = new Brewer.mascaraCep();
	mascaraCep.iniciar();
	
});

