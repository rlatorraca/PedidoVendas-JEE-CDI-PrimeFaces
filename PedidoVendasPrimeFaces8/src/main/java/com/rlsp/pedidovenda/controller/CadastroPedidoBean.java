package com.rlsp.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.rlsp.pedidovenda.model.Cliente;
import com.rlsp.pedidovenda.model.EnderecoEntrega;
import com.rlsp.pedidovenda.model.FormaPagamento;
import com.rlsp.pedidovenda.model.Pedido;
import com.rlsp.pedidovenda.model.Usuario;
import com.rlsp.pedidovenda.repository.ClientesRepository;
import com.rlsp.pedidovenda.repository.UsuariosRepository;
import com.rlsp.pedidovenda.service.CadastroPedidoService;
import com.rlsp.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuariosRepository usuarioRepository;
	
	@Inject
	private ClientesRepository clienteRepository;
	
	@Inject
	private CadastroPedidoService cadastroPedidoService;
	
	private Pedido pedido;
	
	private List<Usuario> vendedores;
	
	public CadastroPedidoBean() {
		limpar();
	}
	
	public void inicializar() {
		if (FacesUtil.isNotPostBack()) {
			this.vendedores = this.usuarioRepository.vendedores();
			this.recalcularPedido(); // Chama funcao para recacular os itens caso o Pedido NAO SEJA novo
		}
	}
	
	private void limpar() {
		pedido = new Pedido();
		pedido.setEnderecoEntrega(new EnderecoEntrega());
	}
	
	public void salvar() {
		this.pedido = this.cadastroPedidoService.salvar(this.pedido);
		
		FacesUtil.addInfoMessage("Pedido salvo com sucesso!");
	}
	
	public FormaPagamento[] getFormasPagamento() {
		return FormaPagamento.values();
	}
	
	public List<Cliente> completarCliente(String nome) {
		return this.clienteRepository.porNome(nome);
	}

	public void recalcularPedido() {
		
		/**
		 * Se o pedido EXISTIR, sera recalculados os valores
		 */
		if (this.pedido != null) {
			pedido.recularValorTotalPedido();
		}	
	}
	public Pedido getPedido() {
		return pedido;
	}
		

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public List<Usuario> getVendedores() {
		return vendedores;
	}
	
	public boolean isEditando() {
		return this.pedido.getId() != null;
	}
	
	
}
