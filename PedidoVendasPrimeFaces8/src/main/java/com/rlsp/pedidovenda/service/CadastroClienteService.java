package com.rlsp.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.rlsp.pedidovenda.model.Cliente;
import com.rlsp.pedidovenda.repository.ClientesRepository;
import com.rlsp.pedidovenda.repository.UsuariosRepository;
import com.rlsp.pedidovenda.util.jpa.Transactional;

public class CadastroClienteService implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private ClientesRepository clienteRepository;
	
	@SuppressWarnings("unlikely-arg-type")
	@Transactional
	public Cliente salvar(Cliente cliente) {
		Cliente clienteExistente = clienteRepository.produtoPorCPForCNPJ(cliente.getDocumentoReceitaFederal());
		
		if( clienteExistente != null && clienteExistente.equals(cliente.getDocumentoReceitaFederal())) {
			throw new NegocioException("Email/Usuário já foi cadastrado.");
		} 
		
		return clienteRepository.salvarAlterar(cliente);
	}
	

}
