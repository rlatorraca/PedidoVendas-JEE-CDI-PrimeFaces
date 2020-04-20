package com.rlsp.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.rlsp.pedidovenda.model.ItemPedido;
import com.rlsp.pedidovenda.model.Pedido;
import com.rlsp.pedidovenda.repository.PedidosRepository;
import com.rlsp.pedidovenda.util.jpa.Transactional;

public class EstoqueService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PedidosRepository pedidoRepository;
	
	@Transactional
	public void baixarItensEstoque(Pedido pedido) throws NegocioException{
		pedido = this.pedidoRepository.porId(pedido.getId());
		
		for (ItemPedido item : pedido.getItens()) {
			item.getProduto().baixarEstoque(item.getQuantidade());
		}
	}
	

	public void retornarItensEstoque(Pedido pedido) {
		pedido = this.pedidoRepository.porId(pedido.getId());
		
		for (ItemPedido item : pedido.getItens()) {
			item.getProduto().adicionarEstoque(item.getQuantidade());
		}
	}
	
}
