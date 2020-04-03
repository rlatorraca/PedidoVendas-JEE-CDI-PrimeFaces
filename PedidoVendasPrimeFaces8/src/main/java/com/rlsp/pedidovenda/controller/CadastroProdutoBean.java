package com.rlsp.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import com.rlsp.pedidovenda.model.Categoria;
import com.rlsp.pedidovenda.model.Produto;



@Named
@ManagedBean
@ViewScoped
public class CadastroProdutoBean implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager gerenciador;
	
	private Produto produto;
	
	private List<Categoria> categorias; 
	private List<Categoria> subcategorias ;
	
	public CadastroProdutoBean() {
		produto = new Produto();
	}
	
	public void inicializar() {
		
		System.out.println("Inicializando .... Categorias");
//		EntityManagerFactory fabrica = Persistence.createEntityManagerFactory("PedidoVendaPU");
		
//		EntityManager gerenciador = fabrica.createEntityManager();
		
		String jpql = "SELECT c from Categoria c";
		
		categorias = gerenciador.createQuery(jpql, Categoria.class).getResultList(); // Pega todos os Objeto do Tipo "CATEGORIA" (da Entidade Categoria.java)
		
		System.out.println("Finalizada a Inicializacao de .... Categorias");
	}

	public void salvar() {
		//throw new RuntimeException("Teste de exceção.");
	}

	public Produto getProduto() {
		return produto;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public List<Categoria> getSubcategorias() {
		return subcategorias;
	}

	
	
	
}