package com.rlsp.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import com.rlsp.pedidovenda.model.Categoria;
import com.rlsp.pedidovenda.model.Produto;
import com.rlsp.pedidovenda.repository.CategoriaRepository;
import com.rlsp.pedidovenda.util.jsf.FacesUtil;



@Named
@ManagedBean
@ViewScoped
public class CadastroProdutoBean implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CategoriaRepository categoriaRepository;
	
	@NotNull
	private Categoria categoriaPai;
	
	private Produto produto;
	
	private List<Categoria> categorias; 
	private List<Categoria> subcategorias ;
	
	public CadastroProdutoBean() {
		produto = new Produto();
		categoriaPai = new Categoria();
	}
	
	public void inicializar() {
		
		System.out.println("Inicializando .... Categorias");

		if(FacesUtil.isNotPostBack()) {
			// Se nao for PostBack (nao for a primeira vez)
			categorias = categoriaRepository.buscarCategorias(); // Pega dentro de CategoriaRespository
		}
				
		System.out.println("Finalizada a Inicializacao de .... Categorias");
	}

	public void salvar() {
		//throw new RuntimeException("Teste de exceção.");
		System.out.println("Categoria Pai selecionado : " + categoriaPai.getDescricao());
		System.out.println("Categoria Filha selecionado : " + produto.getCategoria().getDescricao());
	}
	
	public void carregarSubcategorias() {
		
		subcategorias = categoriaRepository.subcategoriasDe(categoriaPai);
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