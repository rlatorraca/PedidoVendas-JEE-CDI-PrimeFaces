package com.rlsp.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.rlsp.pedidovenda.service.NegocioException;

@Named
@RequestScoped
public class CadastroPedidoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Integer> itens;
	
	public CadastroPedidoBean() {
		itens = new ArrayList<>();
		itens.add(1);
	}

	public List<Integer> getItens() {
		return itens;
	}
	
	public void salvar() {
		throw new NegocioException("Pedido ainda nao foi implementado");
	}
	
}
