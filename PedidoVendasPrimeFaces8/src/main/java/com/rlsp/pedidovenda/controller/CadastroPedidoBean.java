package com.rlsp.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.rlsp.pedidovenda.model.EnderecoEntrega;
import com.rlsp.pedidovenda.model.Pedido;

@Named
@ViewScoped
public class CadastroPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Pedido pedido;
	
	private List<Integer> itens;
	
	public CadastroPedidoBean() {
		pedido = new Pedido();
		pedido.setEnderecoEntrega(new EnderecoEntrega());
		itens = new ArrayList<>();
		itens.add(1);
	}

	public List<Integer> getItens() {
		return itens;
	}
	
	public void salvar() {
		//throw new NegocioException("Pedido ainda nao foi implementado");
	}

	public Pedido getPedido() {
		return pedido;
	}
	
	
	
}
