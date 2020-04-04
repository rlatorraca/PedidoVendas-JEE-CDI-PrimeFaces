package com.rlsp.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.rlsp.pedidovenda.model.Produto;
import com.rlsp.pedidovenda.repository.ProdutosRepository;

public class CadastroProdutoService implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProdutosRepository produtoRepository;
	
	public Produto salvar(Produto produto) {
		Produto produtoExistente = produtoRepository.produtoPorSKU(produto.getSku());
		
		if( produtoExistente != null) {
			throw new NegocioException("SKU/Produto já foi cadastrado.");
		} 
		
		return produtoRepository.salvarAlterar(produto);
	}

}
