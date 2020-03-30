package com.rlsp.pedidovenda.util.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Cria um MENSAGEM para ser jogado na tela do Usario em caso de ERRO em relacao as REGRAS DE NEGOCIO (em CadastroPedidoBean.salvar)
 *  - joga a mensagem na tela do usuario em Cadastro
 *
 */
public class FacesUtil {

	public static void addErrorMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
	}
	
}
