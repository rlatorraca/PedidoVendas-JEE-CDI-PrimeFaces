package com.rlsp.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import com.rlsp.pedidovenda.model.Cliente;
import com.rlsp.pedidovenda.model.Endereco;
import com.rlsp.pedidovenda.model.TipoPessoa;
import com.rlsp.pedidovenda.repository.ClientesRepository;
import com.rlsp.pedidovenda.repository.EnderecosRepository;
import com.rlsp.pedidovenda.service.CadastroClienteService;
import com.rlsp.pedidovenda.service.NegocioException;
import com.rlsp.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ClientesRepository clienteRepository;
	
	@Inject
	private EnderecosRepository enderecoRepository;
	
	@Inject
	private CadastroClienteService clienteService;
	
	@Produces
	@ClienteEdicao
	private Cliente cliente;
	
	private Endereco endereco;
	private Endereco enderecoSelecionado;
	private boolean editandoEndereco;
	
	private List<Cliente> clientes = new ArrayList<>(); 
	private List<Endereco> enderecos  = new ArrayList<>(); 
	
	public CadastroClienteBean() {				
			limpar();		
	}
	
	public boolean isEditando() {
		if (cliente != null && cliente.getId() != null) {
			enderecos = cliente.getEnderecos();			
			
			return true;
		}
			return  false;
		
	}

	public boolean isEditandoEndereco() {
		return editandoEndereco;
	}
	
	public void adicionarEndereco() {
		if(!cliente.getEnderecos().contains(this.endereco)) {
			this.endereco.setCliente(this.cliente);
			this.cliente.getEnderecos().add(endereco);		
			limparEndereco();
		}	
	}

	private void limpar() {
		cliente = new Cliente();
		cliente.setTipo(TipoPessoa.FISICA);
		limparEndereco();
	}
	
	public void removerEndereco() {
		this.cliente.getEnderecos().remove(enderecoSelecionado);
		FacesUtil.addInfoMessage("Endereco excluído com sucesso!");
		limparEndereco();
	}
	
	//Confirma se foi escolhido PF, logo CPF
	public boolean isPessoaFisica() {
		return TipoPessoa.FISICA.equals(this.cliente.getTipo());
	}
	
	//Confirma se foi escolhido PJ, logo 
	public boolean isPessoaJuridica(){
		return TipoPessoa.JURIDICA.equals(this.cliente.getTipo());
	}
	
		
	public void salvar() throws NegocioException {
		if(!cliente.getEnderecos().isEmpty()) {
			clienteService.salvar(cliente);
			this.cliente.isNovo();
			limpar();

			FacesUtil.addInfoMessage("Cliente salvo com sucesso!");
		
		} else {
			FacesUtil.addErrorMessage("Endereço não cadastrado");
		}
	
	}
	
	public void limparEndereco() {
		endereco = new Endereco();
		this.editandoEndereco = false;
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

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public void setEditandoEndereco(boolean editandoEndereco) {
		this.editandoEndereco = editandoEndereco;
	}

	public Endereco getEnderecoSelecionado() {
		return enderecoSelecionado;
	}

	public void setEnderecoSelecionado(Endereco enderecoSelecionado) {
		this.enderecoSelecionado = enderecoSelecionado;
	}
	
	

	
	
	
	
}
