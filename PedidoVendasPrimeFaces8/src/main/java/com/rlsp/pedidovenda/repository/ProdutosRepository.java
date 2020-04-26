package com.rlsp.pedidovenda.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.rlsp.pedidovenda.filter.ProdutoFilter;
import com.rlsp.pedidovenda.model.Categoria;
import com.rlsp.pedidovenda.model.Produto;
import com.rlsp.pedidovenda.service.NegocioException;
import com.rlsp.pedidovenda.util.jpa.Transactional;

@Named
@ViewScoped
public class ProdutosRepository  implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager gerenciador;

	/**
	 * Salva os produtos
	 */
	public Produto salvarAlterar(Produto produto) {
		
		return produto = gerenciador.merge(produto); //Inserir ou Alterar;
	}

	public Produto produtoPorSKU(String sku) {
		try {
			String jpql = "from Produto where upper(sku) = : sku";
			return gerenciador.createQuery(jpql, Produto.class).setParameter("sku", sku.toUpperCase())
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
			
		}
	}
	
	public List<Produto> produtosFiltrados(ProdutoFilter filtro) {
		CriteriaBuilder builder = gerenciador.getCriteriaBuilder();									// Construtor de Criteria
		CriteriaQuery<Produto> criteriaQuery = builder.createQuery(Produto.class);					// Cria um consultra (query) para Produto
		List<Predicate> predicates = new ArrayList<>();												// Serve para criar as RESTRICOES ( Predicates )
		
		Root<Produto> produtoRoot = criteriaQuery.from(Produto.class);								// FROM Produto(tabela)
		Fetch<Produto, Categoria> categoriaJoin = produtoRoot.fetch("categoria", JoinType.INNER);
		categoriaJoin.fetch("categoriaPai", JoinType.INNER);										// Ja faz o FETCH (a inclusao de CATEGORIA) ao fazer a consulta do Produto
		
		if (StringUtils.isNotBlank(filtro.getSku())) {
			predicates.add(builder.equal(produtoRoot.get("sku"), filtro.getSku()));					// Filtra pelo SKU (Atributo, valor)
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.lower(produtoRoot.get("nome")), 
					"%" + filtro.getNome().toLowerCase() + "%"));									//Filtra pelo NOME (case-insensitive)
		}
		
		criteriaQuery.select(produtoRoot);															
		criteriaQuery.where(predicates.toArray(new Predicate[0]));									// Passa a lista das RESTRICOES ( Predicates )
		criteriaQuery.orderBy(builder.asc(produtoRoot.get("nome"))); 								// Ordena por NBOME de forma ASCENDENTE
		
		TypedQuery<Produto> query = gerenciador.createQuery(criteriaQuery);							// Consulta Final criada
		return query.getResultList();
	}

	
//	@SuppressWarnings({ "unchecked", "deprecation" })
//	public List<Produto> produtosFiltrados(ProdutoFilter filtro){
//		/**
//		 * Usando Criteria do HIBERNATE
//		 *  ** A implementacao do JPA deve ser outra
//		 */
//		//Session session = ((Session)gerenciador).unwrap(Session.class);// desempacota um SESSAO do Hibernate atraves do EntityManager
//		Session session = (Session) gerenciador;
//		
//		/**
//		 * Serve para incluir restricoes na pesquisa
//		 */
//
//		Criteria criteria = session.createCriteria(Produto.class);
//		
//		if(StringUtils.isNotBlank(filtro.getSku())) {
//			criteria.add(Restrictions.eq("sku", filtro.getSku()));
//		}
//		
//		/**
//		 *  ilike ==> insensitive (nao importa se Maiuscula ou Minuscula
//		 *  MatchMode.ANYWHERE ==> é % do sql (Serve como coringa)
//		 */
//		if(StringUtils.isNotBlank(filtro.getNome())) {
//			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
//		}
//		
//		return criteria.addOrder(Order.asc("nome")).list();
//	}

	public Produto porId(Long id) {
		
		return gerenciador.find(Produto.class, id);
	}
	
	public Produto porSku(String sku) {
		try {
			return gerenciador.createQuery("from Produto where upper(sku) = :sku", Produto.class)
				.setParameter("sku", sku.toUpperCase())
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	
	@Transactional
	public void remover(Produto produto) throws NegocioException{
		try {
			produto = porId(produto.getId());
			gerenciador.remove(produto);
			gerenciador.flush(); // Para fazer a EXCLUSAO
		} catch (PersistenceException e) {
			throw new NegocioException("Produto não pode ser excluído."); // Lanca um ERROR em casa de problemas com a exclusao
		}
	}
	
	public List<Produto> porNome(String nome) {
		return this.gerenciador.createQuery("from Produto where upper(nome) like :nome", Produto.class)
				.setParameter("nome", "%" + nome.toUpperCase() + "%").getResultList();
	}

	
}
