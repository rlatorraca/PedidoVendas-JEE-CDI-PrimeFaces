package com.rlsp.pedidovenda.controller;



import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.velocity.tools.generic.NumberTool;

import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;
import com.rlsp.pedidovenda.model.Pedido;
import com.rlsp.pedidovenda.util.jsf.FacesUtil;
import com.rlsp.pedidovenda.util.mail.Mailer;

@Named
@RequestScoped
public class EnvioPedidoEmailBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Mailer mailer;
	
	@Inject
	@PedidoEdicao // Injeta o PEDIDO existente na tela
	private Pedido pedido;
	
	public void enviarPedido() throws IOException {
		MailMessage message = mailer.novaMensagem(); //Messagem da API Simple-mail
		
		String filePath = getClass().getClassLoader().getResource("emails/pedido.template").getFile();
		
		message.to(this.pedido.getCliente().getEmail())
			.subject("Pedido " + this.pedido.getId())
			.bodyHtml(new VelocityTemplate(new File(filePath)))
			.put("pedido", pedido) // pedido ==> pedido que esta sendo INJETADO (que o pedido da tela)
			.put("numberTool", new NumberTool()) // numberTool ==> ajuda a formatar R$
			.put("locale", new Locale("pt","BR"))
			.send();
		
		FacesUtil.addInfoMessage("Pedido enviado por e-mail com sucesso!");
	}
	
	
}

