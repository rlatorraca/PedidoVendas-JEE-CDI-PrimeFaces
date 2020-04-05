package com.rlsp.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.rlsp.pedidovenda.model.Grupo;

public class GruposRepository 	implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager gerenciador;
	
	/*
	 * Busca por TODAS as Categorias
	 */
	public List<Grupo> buscarGrupos(){
		
		String jpql = "SELECT g from Grupo g";
		return gerenciador.createQuery(jpql, Grupo.class).getResultList(); // Pega todos os Objeto do Tipo "GRUPO" (da Entidade Categoria.java)	
	
	}
}
