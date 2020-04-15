package com.rlsp.pedidovenda.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

import com.rlsp.pedidovenda.model.Usuario;
import com.rlsp.pedidovenda.repository.PedidosRepository;
import com.rlsp.pedidovenda.repository.UsuariosRepository;
import com.rlsp.pedidovenda.security.UsuarioLogado;
import com.rlsp.pedidovenda.security.UsuarioSistema;
import com.rlsp.pedidovenda.util.IntervaloDatas;

@Named
@RequestScoped
public class GraficoPedidosCriadosBean {

	private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM"); // Formatador de data
	
	@Inject
	private PedidosRepository pedidoRepository;
	
	@Inject
	private UsuariosRepository usuarioRepository;
	
	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado; // Filtra os dados basesado no Usuario Logado
	
	private LineChartModel model;
	private PieChartModel modelPizza;

	public void preRender() {
		this.model = new LineChartModel();
		adicionarSerie("Todos os pedidos", null);
		adicionarSerie("Meus pedidos", usuarioLogado.getUsuario());
		
		this.modelPizza = new PieChartModel();
		criarGraficoPizza();
	}
	
	private void adicionarSerie(String rotulo, Usuario criadoPor) {
		
		this.model.setTitle("Pedidos criados");
		this.model.setLegendPosition("e");
		this.model.setAnimate(true);
		
		this.model.getAxes().put(AxisType.X, new CategoryAxis());
		

		
		Map<Date, BigDecimal> valoresPorData = this.pedidoRepository.valoresTotaisPorData(15, criadoPor);
		
		ChartSeries series = new ChartSeries(rotulo);
		
		for (Date data : valoresPorData.keySet()) {
			series.set(DATE_FORMAT.format(data), valoresPorData.get(data));
		}
		
		this.model.addSeries(series);
	}
	
	
	private void criarGraficoPizza() {
		
		this.model.setTitle("Pedidos Criados por Usuário");
		this.model.setLegendPosition("w");
		this.model.setAnimate(true);
		
		List<Usuario> vendedores = this.usuarioRepository.vendedores();
		
		
		
		for(Usuario usuario : vendedores) {
			
			Map<Date,BigDecimal> valoresPorData = this.pedidoRepository
					.valoresTotaisPorData(IntervaloDatas.getIntefvaloDeDias(usuario.getDataCriacao(), new Date()), usuario);
			
			this.modelPizza.set(usuario.getNome(), somarValoresVendidos(valoresPorData));
		}
		
	}
	
	private BigDecimal somarValoresVendidos(Map<Date,BigDecimal> valoresPorData) {
		BigDecimal soma = BigDecimal.ZERO;
		
		for(Date data : valoresPorData.keySet()) {
			soma = soma.add(valoresPorData.get(data));
		}
		return soma;
	}

	public LineChartModel getModel() {
		return model;
		
	}

	public PieChartModel getModelPizza() {
		return modelPizza;
	}
	
	
	
}
