package com.rlsp.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.rlsp.pedidovenda.filter.ProdutoFilter;
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
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Produto> produtosFiltrados(ProdutoFilter filtro){
		/**
		 * Usando Criteria do HIBERNATE
		 *  ** A implementacao do JPA deve ser outra
		 */
		Session session = ((Session)gerenciador).unwrap(Session.class);// desempacota um SESSAO do Hibernate atraves do EntityManager
		
		/**
		 * Serve para incluir restricoes na pesquisa
		 */

		Criteria criteria = session.createCriteria(Produto.class);
		
		if(StringUtils.isNotBlank(filtro.getSku())) {
			criteria.add(Restrictions.eq("sku", filtro.getSku()));
		}
		
		/**
		 *  ilike ==> insensitive (nao importa se Maiuscula ou Minuscula
		 *  MatchMode.ANYWHERE ==> é % do sql (Serve como coringa)
		 */
		if(StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}
		
		return criteria.addOrder(Order.asc("nome")).list();
	}

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
