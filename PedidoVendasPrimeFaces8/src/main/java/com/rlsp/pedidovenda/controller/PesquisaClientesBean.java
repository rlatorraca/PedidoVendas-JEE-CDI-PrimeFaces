package com.rlsp.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.rlsp.pedidovenda.filter.ClienteFilter;
import com.rlsp.pedidovenda.model.Cliente;
import com.rlsp.pedidovenda.model.TipoPessoa;
import com.rlsp.pedidovenda.repository.ClientesRepository;
import com.rlsp.pedidovenda.util.jsf.FacesUtil;



@Named
@ViewScoped
public class PesquisaClientesBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ClientesRepository clienteRepository;
	
	private ClienteFilter filtro;
	
	private List<Cliente> clientesFiltrados;
	
	private Cliente clienteSelecionado;
	
	
	public PesquisaClientesBean() {
		limpar();
	}
	
	public void inicializar() {
		limpar();
	}
	
	private void limpar() {
		filtro = new ClienteFilter();
		clientesFiltrados = new ArrayList<>();
		clienteSelecionado = new Cliente();
		clienteSelecionado.setTipo(TipoPessoa.FISICA);
	}
	
	public void pesquisarFiltrados() {
		clientesFiltrados = clienteRepository.filtrados(filtro);

	}
	
	public void excluir() {
		clienteRepository.remover(clienteSelecionado);
		clientesFiltrados.remove(clienteSelecionado);
		
		FacesUtil.addInfoMessage("O cliente '" + clienteSelecionado.getNome()+ "' (" + clienteSelecionado.getEmail() + ") foi excluído com sucesso!");		
		
		
	}
	
	//Confirma se foi escolhido PF, logo CPF
	public boolean isPessoaFisica() {
		
		return TipoPessoa.FISICA.equals(this.clienteSelecionado.getTipo());
		
	}
	
	//Confirma se foi escolhido PJ, logo 
	public boolean isPessoaJuridica(){
		return TipoPessoa.JURIDICA.equals(this.clienteSelecionado.getTipo());
	}

	public ClientesRepository getClienteRepository() {
		return clienteRepository;
	}

	public void setClienteRepository(ClientesRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	public ClienteFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(ClienteFilter filtro) {
		this.filtro = filtro;
	}

	public List<Cliente> getClientesFiltrados() {
		return clientesFiltrados;
	}

	public void setClientesFiltrados(List<Cliente> clientesFiltrados) {
		this.clientesFiltrados = clientesFiltrados;
	}

	public Cliente getClienteSelecionado() {
		
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	
	
}
