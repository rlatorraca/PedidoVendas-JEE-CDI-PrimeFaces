package com.rlsp.pedidovenda.util.jsf;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

/**
 * Classe que serve para buscar os objetos GENERICOS abaixo
 *  ** usado em "LoginBean.java" e outras classes
 * @author rlatorraca
 *
 */
public class FacesProducer {

	@Produces
	@RequestScoped
	public FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}
	
	@Produces
	@RequestScoped
	public ExternalContext getExternalContext() {
		return getFacesContext().getExternalContext();
	}
	
//	@Produces
//	@RequestScoped
//	public HttpServletRequest getHttpServletRequest() {
//		return ((HttpServletRequest) getExternalContext().getRequest());	
//	}
	
	@Produces
	@RequestScoped
	public HttpServletResponse getHttpServletResponse() {
		return ((HttpServletResponse) getExternalContext().getResponse());	
	}
	
}