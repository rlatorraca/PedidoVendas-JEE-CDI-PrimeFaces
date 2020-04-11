package com.rlsp.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.rlsp.pedidovenda.model.ItemPedido;
import com.rlsp.pedidovenda.model.Pedido;
import com.rlsp.pedidovenda.model.StatusPedido;
import com.rlsp.pedidovenda.repository.PedidosRepository;
import com.rlsp.pedidovenda.util.jpa.Transactional;;

public class EmissaoPedidoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CadastroPedidoService cadastroPedidoService;
	
	@Inject
	private EstoqueService estoqueService;
	
	@Inject
	private PedidosRepository pedidoRespository;
	
	@Transactional
	public Pedido emitir(Pedido pedido) {
		pedido = this.cadastroPedidoService.salvar(pedido);
		
		if (pedido.isNaoEmissivel()) {
			throw new NegocioException("Pedido não pode ser emitido com status " + pedido.getStatus().getDescricao() + ".");
		}
		
		this.estoqueService.baixarItensEstoque(pedido);
		
		pedido.setStatus(StatusPedido.EMITIDO);
		
		pedido = this.pedidoRespository.guardar(pedido);
		
		return pedido;
	}
	
	
}