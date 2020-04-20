package com.rlsp.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.rlsp.pedidovenda.model.Produto;
import com.rlsp.pedidovenda.repository.ProdutosRepository;
import com.rlsp.pedidovenda.util.jpa.Transactional;

public class CadastroProdutoService implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProdutosRepository produtoRepository;
	
	@Transactional
	public Produto salvar(Produto produto) throws NegocioException {
		Produto produtoExistente = produtoRepository.produtoPorSKU(produto.getSku());
		
		if( produtoExistente != null && produtoExistente.getSku().equals(produto.getSku())) {
			throw new NegocioException("SKU/Produto já foi cadastrado.");
		} 
		
		return produtoRepository.salvarAlterar(produto);
	}
	

}
