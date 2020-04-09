package com.rlsp.pedidovenda.model;

public enum FormaPagamento {

	
	BOLETO_BANCARIO ("Boleto Bancário"),
	CARTAO_CREDITO("Cartão de Credíto"), 
	CARTAO_DEBITO ("Cartão de Débito"), 
	CHEQUE ("Cheque"), 
	CRIPTOMONEY ("Cripto Moeda"), 
	DEPOSITO_BANCARIO ("Depósito Bancário"),
	DINHEIRO ("Dinheiro");
	
	
	FormaPagamento(String descricao) {
		this.descricao = descricao;
	}
	
	private String descricao;
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
	
}
