package com.rlsp.pedidovenda.controller;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class RelatorioProdutosEstoqueBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal valorInicio;
	private BigDecimal valorFim;

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
		parametros.put("valor_inicial", this.valorInicio); // parametros IGUAIS aos presente dentro Arquivo *.jasper
		parametros.put("valor_final", this.valorFim);		// parametros IGUAIS aos presente dentro Arquivo *.jasper
		
		ExecutarRelatorio executor = new ExecutarRelatorio("/reports/relatorio_valores_unitarios.jasper", this.response, parametros, "Produtos no estoque.pdf");
		
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
	public BigDecimal getValorInicio() {
		return valorInicio;
	}

	public void setValorInicio(BigDecimal valorInicio) {
		this.valorInicio = valorInicio;
	}

	@NotNull
	public BigDecimal getValorFim() {
		return valorFim;
	}

	public void setValorFim(BigDecimal valorFim) {
		this.valorFim = valorFim;
	}

}
