package com.rlsp.pedidovenda.util.jsf;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rlsp.pedidovenda.service.NegocioException;



public class JsfExceptionHandlerOLD extends ExceptionHandlerWrapper{

	private static Log log = LogFactory.getLog(JsfExceptionHandlerOLD.class);
	/**
	 * Para o usar ExcpectionHanderdo JSF
	 */
    private ExceptionHandler wrapped;

    @SuppressWarnings("deprecation")
	public JsfExceptionHandlerOLD(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	/**
     * Essa classe retorna o ExcepetionHandler EMPACOTADO
     */
	@Override
	public ExceptionHandler getWrapped() {
		return this.wrapped;
	}
	
	/**
	 * Chama essa METODO quando ocorrer alguma EXCECAO e sera responsavel pelo TRATAMENTO DA EXCECAO, que o JSP consegue capturar
	 */
	@Override
	public void handle() throws FacesException {
		Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents().iterator(); //coloca todos os EVENTOS DE EXCECOES em uma FILA
		 
		while (events.hasNext()) {
			
			ExceptionQueuedEvent event = events.next();
			
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource(); // Pega a EVENTO que possui a EXCECAO
			
			/**
			 * Throwable == PAI de todas as Excecoes
			 */
			Throwable exception = context.getException(); // retorna a EXCECAO (propriamente dita) lancada
			
			/**
			 * Passa o ERRO /EXCECAO capturada para o metodo abaixo para buscar o erro do Tipo 'com.rlsp.pedidovenda.service.NegocioException'
			 *  - Cria uma instancia de NegocioException negocioException  para receber a MENSAGEM DE ERRO
			 *  - 
			 */
			System.out.println(exception);
			NegocioException negocioException = getNegocioException(exception);
			
			boolean handled = false;
			
			try {
				/**
				 * trata a excecao 'ViewExpiredException'
				 * 	- Ocorre quando o browser perde a conexao com o servidor) jogando para pagina inicial
				 */
				if (exception instanceof ViewExpiredException) {
					handled = true;
					redirect("/");
				} else if (negocioException != null) {
					System.out.println("negocioException != null");
					handled = true;
					FacesUtil.addErrorMessage(negocioException.getMessage());
				} else {
					log.error("Erro de sistema : " + exception.getMessage(), exception); //Mensagem + Causa da Excecao (imprime a pilha do erro)
					handled=true;
					redirect("/Error.xhtml");
				}
			} finally {
				if(handled) {
					events.remove(); //Remove as excecoes de dentro do Iterator
				}
				
			}
		}
		
		getWrapped().handle(); //remete as excecoes NAO TRATADAS para ExceptionHandler() tratar as demais excecoes nao tratada pelo codigo acima
	}
	/**
	 * Usado acima em ==> "NegocioException negocioException = getNegocioException(exception);"
	 * 
	 */
	private NegocioException getNegocioException(Throwable exception) {
		System.out.println("getNegocioException(Throwable exception)");
		if (exception instanceof NegocioException) {
			return (NegocioException) exception;
		} else if (exception.getCause() != null) {
			return getNegocioException(exception.getCause());
		}
		
		return null;
	}
	
	private void redirect(String page) {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();       //Pega a instancia ATUAL do contexto do JSF
			System.out.println("facesContext : " + facesContext.toString());
			
			ExternalContext externalContext = facesContext.getExternalContext(); // Extrernal Context (no caso) = 'PedidoVenda-PrimeFaces8'
			System.out.println("externalContext : " + externalContext.toString());
			
			String contextPath = externalContext.getRequestContextPath();// ContextPath (no caso) = 'PedidoVenda-PrimeFaces8'
			System.out.println("contextPath : " + contextPath.toString());
	
			externalContext.redirect(contextPath + page);
			facesContext.responseComplete(); //mostra que a resposta esta completa, evitando qualquer outro processo do JSF
		
		} catch (IOException e) {
			throw new FacesException(e);
		}
	}

	
}
