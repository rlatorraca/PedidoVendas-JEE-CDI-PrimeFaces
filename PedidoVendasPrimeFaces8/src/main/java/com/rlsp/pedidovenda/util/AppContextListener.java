package com.rlsp.pedidovenda.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


/**
 * @WebListener ==> é um SERVELET LISTENER
 * 
 * Poderia ser feita a configuracao via "web.xml"
 *  <listener>
 *        <listener-class>         
 *           com.rlsp.pedidosvendas.util.AppServletContextListener
 *       </listener-class>
 *  </listener> 
 *
 */
@WebListener// è um SERVLET LISTINNER
public class AppContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	/**
	 * Quando feito o DEPLOY da aplicacao (for pegoo Context) esse metodos sera chamado
	 */
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false"); //Nao Precisa fazer a CONVERSAO PARA ZERO
	}

	

}