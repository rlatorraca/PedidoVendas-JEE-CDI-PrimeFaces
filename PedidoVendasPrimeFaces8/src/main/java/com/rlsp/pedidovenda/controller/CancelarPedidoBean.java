package com.rlsp.pedidovenda.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import com.rlsp.pedidovenda.events.cdi.PedidoAlteradoEvent;
import com.rlsp.pedidovenda.model.Pedido;
import com.rlsp.pedidovenda.service.CancelarPedidoService;
import com.rlsp.pedidovenda.service.NegocioException;
import com.rlsp.pedidovenda.util.jsf.FacesUtil;



@Named
@RequestScoped
public class CancelarPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CancelarPedidoService cancelamentoPedidoService;
	
	@Inject
	private Event<PedidoAlteradoEvent> pedidoAlteradoEvent;
	
	@Inject
	@PedidoEdicao
	private Pedido pedido;
	
	public void cancelarPedido() {
		try {
			this.pedido = this.cancelamentoPedidoService.cancelar(this.pedido);
			this.pedidoAlteradoEvent.fire(new PedidoAlteradoEvent(this.pedido));
			
			FacesUtil.addInfoMessage("Pedido cancelado com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
		
	}
	
}
