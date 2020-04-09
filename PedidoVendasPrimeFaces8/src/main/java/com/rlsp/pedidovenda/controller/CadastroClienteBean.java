package com.rlsp.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.rlsp.pedidovenda.filter.ClienteFilter;
import com.rlsp.pedidovenda.model.Cliente;
import com.rlsp.pedidovenda.model.Endereco;
import com.rlsp.pedidovenda.model.TipoPessoa;
import com.rlsp.pedidovenda.repository.ClientesRepository;
import com.rlsp.pedidovenda.service.CadastroClienteService;
import com.rlsp.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ClientesRepository clienteRepository;
	
	@Inject
	private CadastroClienteService clienteService;
	
	private Cliente cliente;

	
	private List<Cliente> clientes; 
	private List<Endereco> enderecos; 
	
	public CadastroClienteBean() {		
		limpar();	
	}

	private void limpar() {
		cliente = new Cliente();
		cliente.setTipo(TipoPessoa.FISICA);
	}
	
	//Confirma se foi escolhido PF, logo CPF
	public boolean isPessoaFisica() {
		return TipoPessoa.FISICA.equals(this.cliente.getTipo());
	}
	
	//Confirma se foi escolhido PJ, logo 
	public boolean isPessoaJuridica(){
		return TipoPessoa.JURIDICA.equals(this.cliente.getTipo());
	}
	
	public boolean isEditando() {
		return this.cliente.getId() != null;
	}
	
	public void salvar() {
		this.cliente = clienteService.salvar(this.cliente);
		limpar();
		FacesUtil.addInfoMessage("Cliente salvo com sucesso");
	}
	
	public TipoPessoa[] getTipoPessoa() {
		return TipoPessoa.values();
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}
	
	

	
	
	
	
}
