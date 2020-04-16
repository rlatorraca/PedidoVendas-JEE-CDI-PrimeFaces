package com.rlsp.pedidovenda.util.report;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import org.hibernate.jdbc.Work;

public class ExecutarRelatorio implements Work {

	private String caminhoRelatorio;		// onde o relatorio esta dentro do projeto
	private HttpServletResponse response;   // incluir o cabecalho, contenttype, dizer que eh um PDF, etc 
	private Map<String, Object> parametros; //
	private String nomeArquivoSaida;		// nome do arquivo de Saída
	
	private boolean relatorioGerado;
	
	public ExecutarRelatorio(String caminhoRelatorio, HttpServletResponse response, Map<String, Object> parametros, String nomeArquivoSaida) {
		this.caminhoRelatorio = caminhoRelatorio;
		this.response = response;
		this.parametros = parametros;
		this.nomeArquivoSaida = nomeArquivoSaida;
		
		this.parametros.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
	}

	/**
	 * Sera a responsavel por executar o RELATORIO e exportar para PDF
	 */
	@Override
	public void execute(Connection connection) throws SQLException {
		try {
			InputStream relatorioStream = this.getClass().getResourceAsStream(this.caminhoRelatorio); // Serve para pegar o fluxo de entrada de um arquivo (no caso o arquivo jasper)
			
			JasperPrint print = JasperFillManager.fillReport(relatorioStream, this.parametros, connection); // Resultado do Relatorio
			this.relatorioGerado = print.getPages().size() > 0;
			
			if (this.relatorioGerado) {
				/**
				 * Faz a exportacao para PDF
				 */
				Exporter<ExporterInput, PdfReportConfiguration, PdfExporterConfiguration, OutputStreamExporterOutput> exportador = new JRPdfExporter();
				exportador.setExporterInput(new SimpleExporterInput(print));
				
				/*
				 * response.getOutputStream() ==> a saida sera enviada para a resposta da requisicao HTTP (browser do usuario)
				 */
				exportador.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
				
				/**
				 * tipo do conteudo enviado (PDF no caso)
				 */
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + this.nomeArquivoSaida  + "\"");
				
				exportador.exportReport();
			}
		} catch (Exception e) {
			throw new SQLException("Erro ao executar relatório " + this.caminhoRelatorio, e);
		}
	}

	public boolean isRelatorioGerado() {
		return relatorioGerado;
	}

}