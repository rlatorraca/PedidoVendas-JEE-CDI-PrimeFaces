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
import com.rlsp.pedidovenda.service.NegocioException;
import com.rlsp.pedidovenda.util.jsf.FacesUtil;

@Named
@ManagedBean
@ViewScoped
public class CadastroProdutoBean implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Faz buscas no DB para prencher a tela e/ou geral
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
	
	
	/**
	 * Atributo para trabalhar o Objeto do BEAN
	 */
	private Produto produto;
	
	private List<Categoria> categorias; 
	private List<Categoria> subcategorias ;
	
	public CadastroProdutoBean() {
		limpar();
	}
	
	public void inicializar() {
		
		System.out.println("Inicializando .... Categorias em CadastroProdutoBean");

		if(FacesUtil.isNotPostBack()) {
			// Se nao for PostBack (nao for a primeira vez)
			categorias = categoriasRepository.buscarCategorias(); // Pega dentro de CategoriaRespository
			
			// Carrega as subcategorias quando estiver usando o EDITAR
			if (this.categoriaPai != null) {
				carregarSubcategorias();
			}
		}
				
		System.out.println("Finalizada a Inicializacao de .... Categorias");
	}

	private void limpar() {
		produto = new Produto();
		categoriaPai = null;
		subcategorias = new ArrayList<>();
	}
	
	public void salvar() throws NegocioException {
	
		try {
			this.produto = cadastroProdutoService.salvar(this.produto);
			limpar();
			
			FacesUtil.addInfoMessage("Produto salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}  
		
		

	}
	
	public boolean isEditando() {
		// Returna TRUE se tiver o ID and TRUE se for Novo Produto
		return this.produto.getId() != null;
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

	public void setProduto(Produto produto) {
		this.produto = produto;
		
		if (this.produto != null) {
			this.categoriaPai = this.produto.getCategoria().getCategoriaPai();
		}
	}
	
	

}