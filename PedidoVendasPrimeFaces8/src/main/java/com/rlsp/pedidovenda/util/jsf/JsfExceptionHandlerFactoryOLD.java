package com.rlsp.pedidovenda.util.jsf;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class JsfExceptionHandlerFactoryOLD extends ExceptionHandlerFactory {

	private ExceptionHandlerFactory parent;
	
	
	@SuppressWarnings("deprecation")
	public JsfExceptionHandlerFactoryOLD(ExceptionHandlerFactory parent) {
		this.parent = parent;
	}
	
	@Override
	public ExceptionHandler getExceptionHandler() {
		return new JsfExceptionHandlerOLD(parent.getExceptionHandler());
	}
	
}