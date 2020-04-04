package com.rlsp.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import com.rlsp.pedidovenda.model.Categoria;

@Named
@ViewScoped
public class CategoriasRepository  implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager gerenciador;

	/*
	 * Busca por TODAS as Categorias
	 */
	public List<Categoria> buscarCategorias(){
		
		String jpql = "SELECT c from Categoria c where categoriaPai is null";
		return gerenciador.createQuery(jpql, Categoria.class).getResultList(); // Pega todos os Objeto do Tipo "CATEGORIA" (da Entidade Categoria.java)
	}
	
	/**
	 * Retorna objetos das Categorias existentes passando o ID
	 * 
	 */
	public Categoria porId(Long id) {
		return gerenciador.find(Categoria.class, id);
	}
	
	/**
	 * Retorna objetos de Subcategorias com base no valor recebido da CATEGORIA PAI
	 * 
	 */
	public List<Categoria> subcategoriasDe(Categoria categoriaPai){
		String jpql = "from Categoria where categoriaPai = :raiz";
		return gerenciador.createQuery(jpql, Categoria.class)
				.setParameter("raiz", categoriaPai)
				.getResultList();
	}
	
}
