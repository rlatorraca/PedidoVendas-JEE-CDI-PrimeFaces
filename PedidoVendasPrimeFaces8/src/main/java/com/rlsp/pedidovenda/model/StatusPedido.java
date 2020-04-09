package com.rlsp.pedidovenda.model;

public enum StatusPedido {

	ORCAMENTO ("Orçamento"), 
	EMITIDO ("Emitido"), 
	CANCELADO ("Cancelado"),
	PAGO ("Pago");
	
	StatusPedido(String descricao) {
		this.descricao = descricao;
	}
	
	private String descricao;

	public String getDescricao() {
		return descricao;
	}
	
	
	
}
