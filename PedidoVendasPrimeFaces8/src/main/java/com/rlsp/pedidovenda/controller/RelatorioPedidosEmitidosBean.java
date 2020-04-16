package com.rlsp.pedidovenda.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;

import com.rlsp.pedidovenda.util.jsf.FacesUtil;
import com.rlsp.pedidovenda.util.report.ExecutarRelatorio;

@Named
@RequestScoped
public class RelatorioPedidosEmitidosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date dataInicio;
	private Date dataFim;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	/**
	 * Necessaria a conexao com o DB
	 *  - Connection nao funciona no Hibernate ,para pegar a Sessao e o Manager 
	 */
	@Inject
	private EntityManager manager;

	public void emitir() {
		
		/**
		 * parametro para busca das informacoes do RELATORIO
		 */
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("data_inicio", this.dataInicio); // parametros IGUAIS aos presente dentro Arquivo *.jasper
		parametros.put("data_fim", this.dataFim);		// parametros IGUAIS aos presente dentro Arquivo *.jasper
		
		ExecutarRelatorio executor = new ExecutarRelatorio("/reports/relatorio_pedidos_emitidos.jasper", this.response, parametros, "Pedidos emitidos.pdf");
		
		Session session = manager.unwrap(Session.class);
		/**
		 * doWork()
		 *  - O relatorio depende de um conexao JDBC
		 *  - Usando "ExecutarRelatorio" faz essa conexao com JDBC par a consulta do Relatorio (para JasperReport)  
		 */
		session.doWork(executor);
		
		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete(); // Finaliza as renderizacoes de paginas com JSF, pos a entrega do relatorio
		} else {
			FacesUtil.addErrorMessage("A execução do relatório não retornou dados.");
		}
	}

	@NotNull
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@NotNull
	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

}
