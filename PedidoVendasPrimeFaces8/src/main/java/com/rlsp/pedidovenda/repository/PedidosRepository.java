package com.rlsp.pedidovenda.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.rlsp.pedidovenda.filter.PedidoFilter;
import com.rlsp.pedidovenda.model.Cliente;
import com.rlsp.pedidovenda.model.Pedido;
import com.rlsp.pedidovenda.model.Usuario;
import com.rlsp.pedidovenda.model.vo.DataValor;



public class PedidosRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	public Map<Date, BigDecimal> valoresTotaisPorData(Integer numeroDeDias, Usuario criadoPor) {
		//numeroDeDias -= 1;
		
		Calendar dataInicial = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);
		
		Map<Date, BigDecimal> resultado = criarMapaVazio(numeroDeDias, dataInicial);
		
		String jpql = "select new com.rlsp.pedidovenda.model.vo.DataValor(date(p.dataCriacao), sum(p.valorTotal)) "
				+ "from Pedido p where p.dataCriacao >= :dataInicial ";
		
		if (criadoPor != null) {
			jpql += "and p.vendedor = :vendedor ";
		}
		
		jpql += "group by date(dataCriacao)";
		
		TypedQuery<DataValor> query = manager.createQuery(jpql, DataValor.class);
		
		query.setParameter("dataInicial", dataInicial.getTime());
		
		if (criadoPor != null) {
			query.setParameter("vendedor", criadoPor);
		}
		
		List<DataValor> valoresPorData = query.getResultList();
		
		for (DataValor dataValor : valoresPorData) {
			resultado.put(dataValor.getData(), dataValor.getValor());
		}
		
		return resultado;
	}
	
//	@SuppressWarnings({ "unchecked" })
//	public Map<Date, BigDecimal> valoresTotaisPorData(Integer numeroDeDias, Usuario criadoPor) {
//		Session session = manager.unwrap(Session.class);
//		
//		numeroDeDias -= 1;
//		
//		Calendar dataInicial = Calendar.getInstance(); // Pega data atual
//		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH); //Trunca no DIA / MES, nao importa hora:minuto:segundo 
//		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1); //Subtrai o "numeroDeDias" recebido
//		
//		Map<Date, BigDecimal> resultado = criarMapaVazio(numeroDeDias, dataInicial);
//		
//		Criteria criteria = session.createCriteria(Pedido.class);
//		
//		/**
//		 *  SELECT date(data_criacao) as data, sum(valor_total) as valor
//		 *  	FROM pedido where data_criacao >= :dataInicial and vendedor_id = :criadoPor
//		 *  	GROUP by date(data_criacao) 
//		 */
//		
//		criteria.setProjection(Projections.projectionList()
//				.add(Projections.sqlGroupProjection("date(data_criacao) as data", "date(data_criacao)", new String[] { "data" },new Type[] { StandardBasicTypes.DATE } ))
//				.add(Projections.sum("valorTotal").as("valor"))
//			)
//			.add(Restrictions.ge("dataCriacao", dataInicial.getTime())); // ge = greater than equal
//		
//		if (criadoPor != null) {
//			criteria.add(Restrictions.eq("vendedor", criadoPor)); // eq = equal
//		}
//		
//		List<DataValor> valoresPorData = criteria.setResultTransformer(Transformers.aliasToBean(DataValor.class)).list(); // Transofmra em Objeto da Classe "DataValor"
//		
//		/**
//		 * Coloca dentro do MAPA VAZIO (resultado) criado acima na linha 107
//		 */
//		for (DataValor dataValor : valoresPorData) {
//			resultado.put(dataValor.getData(), dataValor.getValor());
//		}
//		
//		return resultado;
//	}

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
			dataInicial.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return mapaInicial;
	}

//	/**
//	 * CRIANDO UM MAPA VAZIO COM VALORES ZERO
//	 * 	** Usado para normalizar os dias que nao TENHA PEDIDOS/VENDAS alguma, para que seja marcado ZERO
//	 * 
//	 */
//	private Map<Date, BigDecimal> criarMapaVazio(Integer numeroDeDias, Calendar dataInicial) {
//		
//		/**
//		 * Faz-se o clone da "dataInicial" pois abaixo dentrto do Loop (dataInicial.add(Calendar.DAY_OF_MONTH, 1); ) e feita a inclusao de 1 dia 
//		 *  ** e como "dataInicial.add(Calendar.DAY_OF_MONTH, 1)" trabalha com a instancia original poderia altaerar o valores de data inicial em outars partes da aplicacao
//		 */
//		dataInicial = (Calendar) dataInicial.clone();	
//		Map<Date, BigDecimal> mapaInicial = new TreeMap<>();
//
//		for (int i = 0; i <= numeroDeDias; i++) {
//			mapaInicial.put(dataInicial.getTime(), BigDecimal.ZERO);
//			dataInicial.add(Calendar.DAY_OF_MONTH, 1); // Adiciona 1 dia a cada iteracao
//		}
//		
//		return mapaInicial;
//	}
	
	private List<Predicate> criarPredicatesParaFiltro(PedidoFilter filtro, Root<Pedido> pedidoRoot, 
			From<?, ?> clienteJoin, From<?, ?> vendedorJoin) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (filtro.getNumeroDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(pedidoRoot.get("id"), filtro.getNumeroDe()));
		}

		if (filtro.getNumeroAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(pedidoRoot.get("id"), filtro.getNumeroAte()));
		}

		if (filtro.getDataCriacaoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(pedidoRoot.get("dataCriacao"), filtro.getDataCriacaoDe()));
		}
		
		if (filtro.getDataCriacaoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(pedidoRoot.get("dataCriacao"), filtro.getDataCriacaoAte()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNomeCliente())) {
			predicates.add(builder.like(clienteJoin.get("nome"), "%" + filtro.getNomeCliente() + "%"));
		}
		
		if (StringUtils.isNotBlank(filtro.getNomeVendedor())) {
			predicates.add(builder.like(vendedorJoin.get("nome"), "%" + filtro.getNomeVendedor() + "%"));
		}
		
		if (filtro.getStatuses() != null && filtro.getStatuses().length > 0) {
			predicates.add(pedidoRoot.get("status").in(Arrays.asList(filtro.getStatuses())));
		}
		
		return predicates;
	}
	
//	private Criteria criarCriteriaParaFiltro(PedidoFilter filtro) {
//	//Session session = this.manager.unwrap(Session.class);
//	Session session = (Session) manager;
//	
//	Criteria criteria = session.createCriteria(Pedido.class)
//			.createAlias("cliente", "cliente")
//			.createAlias("vendedor", "v");
//	
//	if (filtro.getNumeroDe() != null) {
//		// id deve ser maior ou igual (ge = greater or equals) a filtro.numeroDe
//		criteria.add(Restrictions.ge("id", filtro.getNumeroDe()));
//	}
//
//	if (filtro.getNumeroAte() != null) {
//		// id deve ser menor ou igual (le = lower or equal) a filtro.numeroDe
//		criteria.add(Restrictions.le("id", filtro.getNumeroAte()));
//	}
//
//	if (filtro.getDataCriacaoDe() != null) {
//		criteria.add(Restrictions.ge("dataCriacao", filtro.getDataCriacaoDe()));
//	}
//	
//	if (filtro.getDataCriacaoAte() != null) {
//		criteria.add(Restrictions.le("dataCriacao", filtro.getDataCriacaoAte()));
//	}
//	
//	if (StringUtils.isNotBlank(filtro.getNomeCliente())) {
//		// acessamos o nome do cliente associado ao pedido pelo alias "c", criado anteriormente
//		criteria.add(Restrictions.ilike("c.nome", filtro.getNomeCliente(), MatchMode.ANYWHERE));
//	}
//	
//	if (StringUtils.isNotBlank(filtro.getNomeVendedor())) {
//		// acessamos o nome do vendedor associado ao pedido pelo alias "v", criado anteriormente
//		criteria.add(Restrictions.ilike("v.nome", filtro.getNomeVendedor(), MatchMode.ANYWHERE));
//	}
//	
//	if (filtro.getStatuses() != null && filtro.getStatuses().length > 0) {
//		// adicionamos uma restrição "in", passando um array de constantes da enum StatusPedido
//		criteria.add(Restrictions.in("status", filtro.getStatuses()));
//	}
//	
//	return criteria;
//}
	
	public List<Pedido> filtrados(PedidoFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Pedido> criteriaQuery = builder.createQuery(Pedido.class);
		
		Root<Pedido> pedidoRoot = criteriaQuery.from(Pedido.class);
		From<?, ?> clienteJoin = (From<?, ?>) pedidoRoot.fetch("cliente", JoinType.INNER);
		From<?, ?> vendedorJoin = (From<?, ?>) pedidoRoot.fetch("vendedor", JoinType.INNER);
		
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, pedidoRoot, clienteJoin, vendedorJoin);
		
		criteriaQuery.select(pedidoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = pedidoRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(
					filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.getPropriedadeOrdenacao().startsWith("cliente.")) {
				orderByFromEntity = clienteJoin;
			}
			
			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}
		
		TypedQuery<Pedido> query = manager.createQuery(criteriaQuery);
		
		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());
		
		return query.getResultList();
	}
	
//	@SuppressWarnings("unchecked")
//	public List<Pedido> filtrados(PedidoFilter filtro) {
//		Criteria criteria = criarCriteriaParaFiltro(filtro);
//		
//		criteria.setFirstResult(filtro.getPrimeiroRegistro());
//		criteria.setMaxResults(filtro.getQuantidadeRegistros());
//		
//		if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
//			criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
//		} else if (filtro.getPropriedadeOrdenacao() != null) {
//			criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
//		}
//		
//		return criteria.list();
//	}
//
//	public int quantidadeFiltrados(PedidoFilter filtro) {
//		Criteria criteria = criarCriteriaParaFiltro(filtro);
//		
//		criteria.setProjection(Projections.rowCount());
//		
//		return ((Number) criteria.uniqueResult()).intValue();
//	}

	
	public int quantidadeFiltrados(PedidoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		
		Root<Pedido> pedidoRoot = criteriaQuery.from(Pedido.class);
		Join<Pedido, Cliente> clienteJoin = pedidoRoot.join("cliente", JoinType.INNER);
		Join<Pedido, Cliente> vendedorJoin = pedidoRoot.join("vendedor", JoinType.INNER);

		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, pedidoRoot, clienteJoin, vendedorJoin);
		
		criteriaQuery.select(builder.count(pedidoRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		
		return query.getSingleResult().intValue();
	}

	public Pedido guardar(Pedido pedido) {
		return this.manager.merge(pedido);
	}

	public Pedido porId(Long id) {
		return this.manager.find(Pedido.class, id);
	}




}
