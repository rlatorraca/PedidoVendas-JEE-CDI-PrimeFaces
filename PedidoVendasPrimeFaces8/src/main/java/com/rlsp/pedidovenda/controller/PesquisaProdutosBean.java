package com.rlsp.pedidovenda.controller;



import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped; //Bean CDI 9 (javax.faces.bean = JSF)
import javax.inject.Inject;
import javax.inject.Named;

import com.rlsp.pedidovenda.filter.ProdutoFilter;
import com.rlsp.pedidovenda.model.Produto;
import com.rlsp.pedidovenda.repository.ProdutosRepository;
import com.rlsp.pedidovenda.util.jsf.FacesUtil;

@Named
//@ManagedBean
//@RequestScoped
@ViewScoped
public class PesquisaProdutosBean implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private ProdutoFilter filtro;
	
	private List<Produto> produtosFiltrados;
	
	private Produto produtoSelecionado;
	
	@Inject
	private ProdutosRepository produtoRepository;
	
	public void pesquisaProduto() {
		
		produtosFiltrados = produtoRepository.produtosFiltrados(filtro);
	}
	
	public void excluir() {
		produtoRepository.remover(produtoSelecionado); //Chamda o Repository para excluir o Produto Selecionado
		
		produtosFiltrados.remove(produtoSelecionado); //Exclui da tela o objeto exlcuido
		
		FacesUtil.addInfoMessage("Produto " + produtoSelecionado.getSku() 
				+ " excluído com sucesso.");
	}

	public PesquisaProdutosBean() {
		super();
		filtro = new ProdutoFilter();
	}

	public List<Produto> getProdutosFiltrados() {
		return produtosFiltrados;
	}

	public ProdutoFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(ProdutoFilter filtro) {
		this.filtro = filtro;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}
	
	
	
	
}
