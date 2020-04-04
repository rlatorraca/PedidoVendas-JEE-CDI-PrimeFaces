package com.rlsp.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import com.rlsp.pedidovenda.model.Categoria;
import com.rlsp.pedidovenda.model.Produto;
import com.rlsp.pedidovenda.repository.CategoriasRepository;
import com.rlsp.pedidovenda.service.CadastroProdutoService;
import com.rlsp.pedidovenda.util.jsf.FacesUtil;



@Named
@ManagedBean
@ViewScoped
public class CadastroProdutoBean implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Faz buscas no DBpara prencher a tela e/ou geral
	 */
	@Inject
	private CategoriasRepository categoriasRepository;
	
	/**
	 * Usado para o CRUD
	 */
	@Inject
	private CadastroProdutoService cadastroProdutoService;
	
	@NotNull
	private Categoria categoriaPai;
	
	private Produto produto;
	
	private List<Categoria> categorias; 
	private List<Categoria> subcategorias ;
	
	public CadastroProdutoBean() {
		limpar();
	}
	
	public void inicializar() {
		
		System.out.println("Inicializando .... Categorias");

		if(FacesUtil.isNotPostBack()) {
			// Se nao for PostBack (nao for a primeira vez)
			categorias = categoriasRepository.buscarCategorias(); // Pega dentro de CategoriaRespository
		}
				
		System.out.println("Finalizada a Inicializacao de .... Categorias");
	}

	private void limpar() {
		produto = new Produto();
		categoriaPai = null;
		subcategorias = new ArrayList<>();
	}
	
	public void salvar() {
		this.produto = cadastroProdutoService.salvar(this.produto);
		limpar();
		
		FacesUtil.addInfoMessage("Produto salvo com sucesso!");
	}
	public void carregarSubcategorias() {
		
		subcategorias = categoriasRepository.subcategoriasDe(categoriaPai);
	}

	public Categoria getCategoriaPai() {
		return categoriaPai;
	}

	public void setCategoriaPai(Categoria categoriaPai) {
		this.categoriaPai = categoriaPai;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public List<Categoria> getSubcategorias() {
		return subcategorias;
	}

	public void setSubcategorias(List<Categoria> subcategorias) {
		this.subcategorias = subcategorias;
	}

	public Produto getProduto() {
		return produto;
	}

}