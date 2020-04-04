package com.rlsp.pedidovenda.repository;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import com.rlsp.pedidovenda.model.Produto;

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
		EntityTransaction transacao = gerenciador.getTransaction();
		
		transacao.begin();
		produto = gerenciador.merge(produto); //Inserir ou Alterar
		transacao.commit();
		
		return produto;
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

}
