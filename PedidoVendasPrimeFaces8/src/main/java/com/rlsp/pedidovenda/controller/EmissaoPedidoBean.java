package com.rlsp.pedidovenda.controller;


import java.io.Serializable;

import javax.enterprise.event.Event;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.rlsp.pedidovenda.events.cdi.PedidoAlteradoEvent;
import com.rlsp.pedidovenda.model.Pedido;
import com.rlsp.pedidovenda.service.EmissaoPedidoService;
import com.rlsp.pedidovenda.service.NegocioException;
import com.rlsp.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class EmissaoPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EmissaoPedidoService emissaoPedidoService;
	
	/**
	 * Usando @Inject faz-se a injecao da instancia de PEDIDO (em CadastroPedidoBean.java) criado pelo QUALIFICADOR @PedidoEdicao
	 *  		@Produces 
	 *			@PedidoEdicao
	 *			private Pedido pedido;
	 */
	@Inject
	@PedidoEdicao 
	private Pedido pedido;
	
	/**
	 * Injeta o Tipo Especial do "javax.enterprise.event.Event" ==> EVENTO / EVENT
	 */
	@Inject
	private Event<PedidoAlteradoEvent> pedidoAlteradoEvent;
	
	public void emitirPedido() {
		this.pedido.removerItemVazio();
		
		try {
			this.pedido = this.emissaoPedidoService.emitir(this.pedido); // SALVA no DB, DA BAIXA em ESTOQUE e ATUALIZA todoas informacoes
			
			/**
			 * Lança um evento de CDI para ficar ouvindo em caso de ALTERACAO do PEDIDO e atualizar os "itensPedido.xml" com a nova quantidade em ESTOQUE
			 * 
			 *  ** fire = lance (pt_br)s
			 */
			this.pedidoAlteradoEvent.fire(new PedidoAlteradoEvent(this.pedido));
			
			FacesUtil.addInfoMessage("Pedido emitido com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		} finally {
			this.pedido.adicionarItemVazio(); // Adiciona a linha de inclusao de itens de volta a tela
		}
	}
	
}
