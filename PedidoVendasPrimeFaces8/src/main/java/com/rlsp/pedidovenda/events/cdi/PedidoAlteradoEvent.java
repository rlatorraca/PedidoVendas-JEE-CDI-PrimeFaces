package com.rlsp.pedidovenda.events.cdi;

import com.rlsp.pedidovenda.model.Pedido;
/**
 * Essa CLASSE representa o EVENTO de ALTERACAO DE UM PEDIDO que encapsula dentro dele um PEDIDO
 *
 */
public class PedidoAlteradoEvent {


	private Pedido pedido;
	
	public PedidoAlteradoEvent(Pedido pedido) {
		this.pedido = pedido;
	}

	public Pedido getPedido() {
		return pedido;
	}
}
