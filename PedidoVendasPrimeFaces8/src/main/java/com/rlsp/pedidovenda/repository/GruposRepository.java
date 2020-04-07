package com.rlsp.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.rlsp.pedidovenda.model.Categoria;
import com.rlsp.pedidovenda.model.Grupo;
import com.rlsp.pedidovenda.model.Produto;
import com.rlsp.pedidovenda.model.Usuario;

public class GruposRepository 	implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager gerenciador;
	
	/*
	 * Busca por TODAS as Categorias
	 */
	public List<Grupo> buscarGrupos(){
		
		String jpql = "SELECT g from Grupo g order by nome asc";
		return gerenciador.createQuery(jpql, Grupo.class).getResultList(); // Pega todos os Objeto do Tipo "GRUPO" (da Entidade Categoria.java)	
	
	}

	public Grupo porId(Long id) {
		
		return gerenciador.find(Grupo.class, id);
	}
	
	public List<Grupo> subcategoriasDe(Grupo grupoEscolhido){
		String jpql = "from Grupo where  nome = :raiz";
		return gerenciador.createQuery(jpql, Grupo.class)
				.setParameter("raiz", grupoEscolhido)
				.getResultList();
	}
}
