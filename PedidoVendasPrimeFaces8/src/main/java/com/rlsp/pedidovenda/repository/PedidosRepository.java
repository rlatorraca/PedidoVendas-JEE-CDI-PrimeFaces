package com.rlsp.pedidovenda.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.rlsp.pedidovenda.model.vo.DataValor;
import com.rlsp.pedidovenda.filter.PedidoFilter;
import com.rlsp.pedidovenda.model.Pedido;
import com.rlsp.pedidovenda.model.Usuario;



public class PedidosRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	

	private Criteria criarCriteriaParaFiltro(PedidoFilter filtro) {
		Session session = this.manager.unwrap(Session.class);
		
		Criteria criteria = session.createCriteria(Pedido.class)
				.createAlias("cliente", "cliente")
				.createAlias("vendedor", "v");
		
		if (filtro.getNumeroDe() != null) {
			// id deve ser maior ou igual (ge = greater or equals) a filtro.numeroDe
			criteria.add(Restrictions.ge("id", filtro.getNumeroDe()));
		}

		if (filtro.getNumeroAte() != null) {
			// id deve ser menor ou igual (le = lower or equal) a filtro.numeroDe
			criteria.add(Restrictions.le("id", filtro.getNumeroAte()));
		}

		if (filtro.getDataCriacaoDe() != null) {
			criteria.add(Restrictions.ge("dataCriacao", filtro.getDataCriacaoDe()));
		}
		
		if (filtro.getDataCriacaoAte() != null) {
			criteria.add(Restrictions.le("dataCriacao", filtro.getDataCriacaoAte()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNomeCliente())) {
			// acessamos o nome do cliente associado ao pedido pelo alias "c", criado anteriormente
			criteria.add(Restrictions.ilike("c.nome", filtro.getNomeCliente(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotBlank(filtro.getNomeVendedor())) {
			// acessamos o nome do vendedor associado ao pedido pelo alias "v", criado anteriormente
			criteria.add(Restrictions.ilike("v.nome", filtro.getNomeVendedor(), MatchMode.ANYWHERE));
		}
		
		if (filtro.getStatuses() != null && filtro.getStatuses().length > 0) {
			// adicionamos uma restrição "in", passando um array de constantes da enum StatusPedido
			criteria.add(Restrictions.in("status", filtro.getStatuses()));
		}
		
		return criteria;
	}
	
	@SuppressWarnings("unchecked")
	public List<Pedido> filtrados(PedidoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		
		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());
		
		if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
		} else if (filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
		}
		
		return criteria.list();
	}

	public int quantidadeFiltrados(PedidoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		
		criteria.setProjection(Projections.rowCount());
		
		return ((Number) criteria.uniqueResult()).intValue();
	}
	
	public Pedido guardar(Pedido pedido) {
		return this.manager.merge(pedido);
	}

	public Pedido porId(Long id) {
	
		return this.manager.find(Pedido.class, id);
	}

	@SuppressWarnings({ "unchecked" })
	public Map<Date, BigDecimal> valoresTotaisPorData(Integer numeroDeDias, Usuario criadoPor) {
		Session session = manager.unwrap(Session.class);
		
		numeroDeDias -= 1;
		
		Calendar dataInicial = Calendar.getInstance(); // Pega data atual
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH); //Trunca no DIA / MES, nao importa hora:minuto:segundo 
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1); //Subtrai o "numeroDeDias" recebido
		
		Map<Date, BigDecimal> resultado = criarMapaVazio(numeroDeDias, dataInicial);
		
		Criteria criteria = session.createCriteria(Pedido.class);
		
		/**
		 *  SELECT date(data_criacao) as data, sum(valor_total) as valor
		 *  	FROM pedido where data_criacao >= :dataInicial and vendedor_id = :criadoPor
		 *  	GROUP by date(data_criacao) 
		 */
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.sqlGroupProjection("date(data_criacao) as data", "date(data_criacao)", new String[] { "data" },new Type[] { StandardBasicTypes.DATE } ))
				.add(Projections.sum("valorTotal").as("valor"))
			)
			.add(Restrictions.ge("dataCriacao", dataInicial.getTime())); // ge = greater than equal
		
		if (criadoPor != null) {
			criteria.add(Restrictions.eq("vendedor", criadoPor)); // eq = equal
		}
		
		List<DataValor> valoresPorData = criteria.setResultTransformer(Transformers.aliasToBean(DataValor.class)).list(); // Transofmra em Objeto da Classe "DataValor"
		
		/**
		 * Coloca dentro do MAPA VAZIO (resultado) criado acima na linha 107
		 */
		for (DataValor dataValor : valoresPorData) {
			resultado.put(dataValor.getData(), dataValor.getValor());
		}
		
		return resultado;
	}

	/**
	 * CRIANDO UM MAPA VAZIO COM VALORES ZERO
	 * 	** Usado para normalizar os dias que nao TENHA PEDIDOS/VENDAS alguma, para que seja marcado ZERO
	 * 
	 */
	private Map<Date, BigDecimal> criarMapaVazio(Integer numeroDeDias, Calendar dataInicial) {
		
		/**
		 * Faz-se o clone da "dataInicial" pois abaixo dentrto do Loop (dataInicial.add(Calendar.DAY_OF_MONTH, 1); ) e feita a inclusao de 1 dia 
		 *  ** e como "dataInicial.add(Calendar.DAY_OF_MONTH, 1)" trabalha com a instancia original poderia altaerar o valores de data inicial em outars partes da aplicacao
		 */
		dataInicial = (Calendar) dataInicial.clone();	
		Map<Date, BigDecimal> mapaInicial = new TreeMap<>();

		for (int i = 0; i <= numeroDeDias; i++) {
			mapaInicial.put(dataInicial.getTime(), BigDecimal.ZERO);
			dataInicial.add(Calendar.DAY_OF_MONTH, 1); // Adiciona 1 dia a cada iteracao
		}
		
		return mapaInicial;
	}

}
