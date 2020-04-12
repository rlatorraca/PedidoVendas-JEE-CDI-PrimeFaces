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
import com.rlsp.pedidovenda.model.Cliente;
import com.rlsp.pedidovenda.util.jsf.FacesUtil;
import com.rlsp.pedidovenda.util.mail.Mailer;

@Named
@RequestScoped
public class EnvioClienteEmailBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Mailer mailer;
	
	@Inject
	@ClienteEdicao// Injeta o PEDIDO existente na tela
	private Cliente cliente;
	
	public void enviarPedido() throws IOException {
		MailMessage message = mailer.novaMensagem(); //Messagem da API Simple-mail
		
		String filePath = getClass().getClassLoader().getResource("emails/cliente.template").getFile();
		
		message.to(this.cliente.getEmail())
			.subject("Cliente " + this.cliente.getNome()  + " código : RLSP-PV" + cliente.getId())
			.bodyHtml(new VelocityTemplate(new File(filePath)))
			.put("cliente", cliente) // pedido ==> pedido que esta sendo INJETADO (que o pedido da tela)
			.put("numberTool", new NumberTool()) // numberTool ==> ajuda a formatar R$
			.put("locale", new Locale("pt","BR"))
			.send();
		
		FacesUtil.addInfoMessage("Pedido enviado por e-mail com sucesso!");
	}
	
	
}

