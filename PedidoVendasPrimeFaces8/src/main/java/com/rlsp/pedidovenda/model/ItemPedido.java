package com.rlsp.pedidovenda.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="itemPedido")
public class ItemPedido implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="quantidade_item_pedido", nullable = false)
	private Integer quantidade = 1;
	
	@Column(name="valor_unitario_item_pedido", nullable = false, precision = 10, scale = 2)
	private BigDecimal valorUnitario = BigDecimal.ZERO;
	
	//Muitos ITENS PEDIDO tem **1** PRODUTO (SEMPRE terá 1 produto) ou 1 PRODUTO pode ter 1+ ITENS de PEDIDO
	@ManyToOne
	@JoinColumn(name="produto_id", nullable = false)
	private Produto produto;
	
	//Muitos ITENS PEDIDO tem SOMENTE **1** PEDIDO (SEMPRE terá 1 produto) ou 1 PEDIDO tem 1+ ITENSPEDIDOS
	@ManyToOne
	@JoinColumn(name="pedido_id", nullable = false)
	private Pedido pedido;
	
	
	/**
	 * Serve para calcular o valor do item (item x quantidade)
	 *  ** Usado por ""Pedido.recularValorTotalPedido""
	 *  
	 * @Transient ==> nao sera gravado no DB
	 */
	@Transient
	public BigDecimal getValorTotal() {
		return this.getValorUnitario().multiply(new BigDecimal(this.getQuantidade()));
	}
	
	/**
	 * Verifica se o ITEM tem um PRODUTO associado a ele 
	 */
	@Transient
	public boolean isProdutoAssociado() {
		return this.getProduto() != null && this.getProduto().getId() != null;
	}
	
	@Transient
	public boolean isEstoqueSuficiente() {
		return this.getPedido().isEmitido() 
				|| this.getProduto().getId() == null 
				|| this.getProduto().getQuantidadeEstoque() >= this.getQuantidade(); 
	}
	
	@Transient
	public boolean isEstoqueInsuficiente() {
		return !this.isEstoqueSuficiente();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemPedido other = (ItemPedido) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
