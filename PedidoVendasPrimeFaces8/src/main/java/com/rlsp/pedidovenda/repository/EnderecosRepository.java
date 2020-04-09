package com.rlsp.pedidovenda.repository;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.rlsp.pedidovenda.model.Endereco;
import com.rlsp.pedidovenda.util.jpa.Transactional;

public class EnderecosRepository implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager gerenciador;

	
	@Transactional
	public Endereco salvarAlterar(Endereco endereco) {
		
		return endereco = gerenciador.merge(endereco); //Inserir ou Alterar;;
	}
}