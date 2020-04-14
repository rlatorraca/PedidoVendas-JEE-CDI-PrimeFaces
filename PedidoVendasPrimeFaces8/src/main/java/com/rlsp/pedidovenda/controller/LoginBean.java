package com.rlsp.pedidovenda.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rlsp.pedidovenda.util.jsf.FacesUtil;

@Named
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * E contexto do JSF (Ciclo do vida )
	 *  - API de JSF
	 */
	@Inject
	private FacesContext facesContext;

	/**
	 * Objeto que representa a REQUISICAO (pedido) do servlet
	 *  - API de SERVLET
	 */
	@Inject 
	private HttpServletRequest request;
	
	/**
	 * Objeto que representa a RESPONSE (resposta) do servlet (que vai de volta ao cliente)
	 *  - API de SERVLET
	 */
	@Inject
	private HttpServletResponse response;
	
	private String email;


	public void preRender() {
		if ("true".equals(request.getParameter("invalid"))) {
			FacesUtil.addErrorMessage("Usuário ou senha inválido!");
		}
	}
	
	public void login() throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/Login.xhtml");
		dispatcher.forward(request, response);
		
		facesContext.responseComplete();
	}
	
	public String getEmail() {
		System.out.println("Email em LoginBean : " + email);
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
}