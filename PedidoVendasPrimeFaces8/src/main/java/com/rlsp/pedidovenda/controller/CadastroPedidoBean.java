package com.rlsp.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;

import com.rlsp.pedidovenda.events.cdi.PedidoAlteradoEvent;
import com.rlsp.pedidovenda.model.Cliente;
import com.rlsp.pedidovenda.model.EnderecoEntrega;
import com.rlsp.pedidovenda.model.FormaPagamento;
import com.rlsp.pedidovenda.model.ItemPedido;
import com.rlsp.pedidovenda.model.Pedido;
import com.rlsp.pedidovenda.model.Produto;
import com.rlsp.pedidovenda.model.Usuario;
import com.rlsp.pedidovenda.repository.ClientesRepository;
import com.rlsp.pedidovenda.repository.ProdutosRepository;
import com.rlsp.pedidovenda.repository.UsuariosRepository;
import com.rlsp.pedidovenda.service.CadastroPedidoService;
import com.rlsp.pedidovenda.service.NegocioException;
import com.rlsp.pedidovenda.util.jsf.FacesUtil;
import com.rlsp.pedidovenda.validation.SKU;

@Named
@ViewScoped
public class CadastroPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuariosRepository usuarioRepository;
	
	@Inject
	private ClientesRepository clienteRepository;
	
	@Inject
	private ProdutosRepository produtoRepository;
	
	@Inject
	private CadastroPedidoService cadastroPedidoService;
	
	@SKU
	private String sku;
	
	/*
	 * Produz uma instancia de PEDIDO para ser usada em outras Classes atraves do QUALIFICAR ==> @PedidoEdicao
	 */
	@Produces 
	@PedidoEdicao
	private Pedido pedido;
	
	private List<Usuario> vendedores;
	
	private Produto produtoLinhaEditavel;
	
	public CadastroPedidoBean() {
		limpar();
	}
	
	public void inicializar() {
		//if (FacesUtil.isNotPostBack()) {
			
			if(this.pedido == null) {
				limpar();
			}
			this.vendedores = this.usuarioRepository.vendedores();
			this.pedido.adicionarItemVazio(); // Adiciona UM LINHA VAZIA para incluir um espaco editavel (Padrao)
			this.recalcularPedido(); // Chama funcao para recacular os itens caso o Pedido NAO SEJA novo
		//}
	}
	
	private void limpar() {
		pedido = new Pedido();
		pedido.setEnderecoEntrega(new EnderecoEntrega());
	}
	
	public void salvar() {
		this.pedido.removerItemVazio(); // Remove a 1ª linha vazia
		
		try {
			this.pedido = this.cadastroPedidoService.salvar(this.pedido);
			FacesUtil.addInfoMessage("Pedido salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		} finally {
			this.pedido.adicionarItemVazio(); // ReAdiciona a 1º linda apos SALVAR, ou se houver ERRO
		}
		
		
		
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
	
	
	/**
	 * Usado para colocao o Cliente solecionado na tela de pedidos, apos o fechament oda caisa de dialogo  
	 */
	public void clienteSelecionado(@SuppressWarnings("rawtypes") SelectEvent event) {
		pedido.setCliente((Cliente) event.getObject());
	}

	
	/**
	 * Usado no autocomplete para procurar o produto no DB
	 *  ** retorna um List<Produto>
	 * 
	 */
	public List<Produto> completarProduto(String nome) {
		return this.produtoRepository.porNome(nome);
	}
	

	public void carregarProdutoLinhaEditavel() {
		ItemPedido item = this.pedido.getItens().get(0);
		
		if (this.produtoLinhaEditavel != null) {
			if (this.existeItemComProduto(this.produtoLinhaEditavel)) {
				FacesUtil.addErrorMessage("Já existe um item no pedido com o produto informado.");
			} else {
				item.setProduto(this.produtoLinhaEditavel);
				item.setValorUnitario(this.produtoLinhaEditavel.getValorUnitario());
				
				this.pedido.adicionarItemVazio();
				this.produtoLinhaEditavel = null;
				this.sku = null;
				
				this.pedido.recularValorTotalPedido();
			}
		}
	}
	
	/**
	 * Verifique se ja existe o ITEM com o produto (recebido)
	 * 
	 */
	private boolean existeItemComProduto(Produto produto) {
		boolean existeItem = false;
		
		for (ItemPedido item : this.getPedido().getItens()) {
			if (produto.equals(item.getProduto())) {
				existeItem = true;
				break;
			}
		}
		
		return existeItem;
	}
	
	/**
	 * Carrega os items (de produtos) por SKU
	 */
	public void carregarProdutoPorSku() {
		if (StringUtils.isNotEmpty(this.sku)) {
			this.produtoLinhaEditavel = this.produtoRepository.porSku(this.sku);
			this.carregarProdutoLinhaEditavel();
		}
	}
	
	/**
	 * Atualiza a QUANTIDADE de ITENS e recalcula os valores de Subtotal e Total
	 */
	public void atualizarQuantidade(ItemPedido item, int linha) {
		if (item.getQuantidade() < 1) {
			if (linha == 0) {
				item.setQuantidade(1); // Para  1ª linha que serve de ENTRADA dos ITENS
			} else {
				this.getPedido().getItens().remove(linha);
			}
		}
		
		this.pedido.recularValorTotalPedido();
	}
	
	
	/**
	 * OBSERVADOR / OBSERVER
	 *  - fica espeando MODIFICACOES dentro da pagina do  PEDIDO (como em emissao de pedido por exemplo), para entao fazer a ATUALIZACAO da pagina
	 *  @Observes ==> mostra que é um OBSERVER da classe PedidoAlteradoEvent 
	 *   - Logo ele fica esperando mudancas para atualiar o "this.pedido"
	 */
	public void observadorPedidoAlterado(@Observes PedidoAlteradoEvent event) {
		this.pedido = event.getPedido();
	}
	
	/**
	 * GETTERS and SETTERs
	 * 
	 */
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

	
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Produto getProdutoLinhaEditavel() {
		return produtoLinhaEditavel;
	}

	public void setProdutoLinhaEditavel(Produto produtoLinhaEditavel) {
		this.produtoLinhaEditavel = produtoLinhaEditavel;
	}

	public void setVendedores(List<Usuario> vendedores) {
		this.vendedores = vendedores;
	}

	public ProdutosRepository getProdutoRepository() {
		return produtoRepository;
	}

	public void setProdutoRepository(ProdutosRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}
	
	
}
