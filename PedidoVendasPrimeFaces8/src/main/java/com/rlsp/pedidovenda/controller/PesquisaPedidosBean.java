package com.rlsp.pedidovenda.controller;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.rlsp.pedidovenda.filter.PedidoFilter;
import com.rlsp.pedidovenda.model.Pedido;
import com.rlsp.pedidovenda.model.StatusPedido;
import com.rlsp.pedidovenda.repository.PedidosRepository;



@Named
@ViewScoped
public class PesquisaPedidosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private PedidosRepository pedidosRepostitory;
	
	private PedidoFilter filtro;
	
	private LazyDataModel<Pedido> model;
	
	public PesquisaPedidosBean() {
		filtro = new PedidoFilter();
		
		/**
		 * Seve para fazer a pesquisa por pagina LAZY
		 */
		model = new LazyDataModel<Pedido>() {

			private static final long serialVersionUID = 1L;
			
			@Override
			public List<Pedido> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, FilterMeta> filterBy) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				
				setRowCount(pedidosRepostitory.quantidadeFiltrados(filtro));
				
				return pedidosRepostitory.filtrados(filtro);
				
			}
			
			
			
		};
	}

	
	public void posProcessarXls(Object documento) {
		HSSFWorkbook planilha = (HSSFWorkbook) documento;
		HSSFSheet folha = planilha.getSheetAt(0);
		HSSFRow cabecalho = folha.getRow(0);
		HSSFCellStyle estiloCelula = planilha.createCellStyle();
		Font fonteCabecalho = planilha.createFont();
		
		fonteCabecalho.setColor(IndexedColors.WHITE.getIndex());
		fonteCabecalho.setBold(true);
		fonteCabecalho.setFontHeightInPoints((short) 16);
		
		estiloCelula.setFont(fonteCabecalho);
		estiloCelula.setFillForegroundColor(IndexedColors.BLACK.getIndex());
		estiloCelula.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		for (int i = 0; i < cabecalho.getPhysicalNumberOfCells(); i++) {
			cabecalho.getCell(i).setCellStyle(estiloCelula);
		}
	}
	
	
	
	public StatusPedido[] getStatuses() {
		return StatusPedido.values();//retorna um Array com a ENUMERACAO
	}
	

	public PedidoFilter getFiltro() {
		return filtro;
	}

	public LazyDataModel<Pedido> getModel() {
		return model;
	}

	
	
	
}
