package com.rlsp.pedidovenda.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;


import com.rlsp.pedidovenda.model.Cliente;
import com.rlsp.pedidovenda.repository.ClientesRepository;;

@Named
@ViewScoped
public class SelecaoClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ClientesRepository clienteRepository;
	
	private String nome;
	
	private List<Cliente> clientesFiltrados;
	
	public void pesquisar() {
		clientesFiltrados = clienteRepository.porNome(nome);
	}
	public void selecionar(Cliente cliente) {
		PrimeFaces.current().dialog().closeDynamic(cliente); // Fecha a Caixa dde Dialogo apos a selecao do usuario/cliente
	}

	public void abrirDialogo() {
		Map<String, Object> opcoes = new HashMap<>(); //Configuracoes da Caixa de Dialogo
		opcoes.put("modal", true);
		opcoes.put("resizable", false);
		opcoes.put("contentHeight", 470);
		
		PrimeFaces.current().dialog().openDynamic("/dialogos/SelecaoCliente", opcoes, null);
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Cliente> getClientesFiltrados() {
		return clientesFiltrados;
	}

}