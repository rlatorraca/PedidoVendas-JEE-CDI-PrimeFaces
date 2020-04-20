package com.rlsp.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.rlsp.pedidovenda.model.Pedido;
import com.rlsp.pedidovenda.model.StatusPedido;
import com.rlsp.pedidovenda.repository.PedidosRepository;
import com.rlsp.pedidovenda.util.jpa.Transactional;

public class CancelarPedidoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PedidosRepository pedidoRespository;
	
	@Inject
	private EstoqueService estoqueService;
	
	@Transactional
	public Pedido cancelar(Pedido pedido) throws NegocioException {
		pedido = this.pedidoRespository.porId(pedido.getId());
		
		if (pedido.isNaoCancelavel()) {
			throw new NegocioException("Pedido não pode ser cancelado no status "
					+ pedido.getStatus().getDescricao() + ".");
		}
		
		if (pedido.isEmitido()) {
			this.estoqueService.retornarItensEstoque(pedido);
		}
		
		pedido.setStatus(StatusPedido.CANCELADO);
		
		pedido = this.pedidoRespository.guardar(pedido);
		
		return pedido;
	}

}
