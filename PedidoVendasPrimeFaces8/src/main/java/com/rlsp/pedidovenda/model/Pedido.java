package com.rlsp.pedidovenda.model;

import java.beans.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="pedido")
public class Pedido implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 *  - TemporalType.Date = data (sem hora);
	 *  - TemporalType.TIME = apenas a hora (sem a data) ; 
	 *  - TemporalType.TIMESTAMP =  data + hora
	 */
	@Temporal(TemporalType.TIMESTAMP) 
	@NotNull
	@Column(name = "data_criacao", nullable = false)
	private Date dataCriacao;
	
	/**
	 * TEXT (aceita mais caracateres)
	 *  - Criacao um definicao do ATRIBUTO no Banco de DAdos, no caso cria "text" e nao "varchar"
	 */
	@Column(name = "observacao_pedido", columnDefinition = "text")
	private String observacao;
	
	/**
	 *  - TemporalType.Date = data (sem hora);
	 *  - TemporalType.TIME = apenas a hora (sem a data) ; 
	 *  - TemporalType.TIMESTAMP =  data + hora
	 */
	@Temporal(TemporalType.DATE)
	@NotNull
	@Column(name = "data_entrega", nullable = false)
	private Date dataEntrega;
	
	@NotNull
	@Column(name = "valor_frete_pedido", nullable = false, precision = 10, scale = 2)
	private BigDecimal valorFrete = BigDecimal.ZERO;
	
	@NotNull
	@Column(name = "valor_desconto_pedido", nullable = false, precision = 10, scale = 2)
	private BigDecimal valorDesconto = BigDecimal.ZERO;
	
	@NotNull
	@Column(name = "valor_total_pedido", nullable = false, precision = 15, scale = 2)
	private BigDecimal valorTotal = BigDecimal.ZERO;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name="status_pedido", nullable = false, length = 20)
	private StatusPedido status;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "forma_pagamento", nullable = false, length = 20)
	private FormaPagamento formaPagamento;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "vendedor_id", nullable = false)
	private Usuario vendedor;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = false)
	private Cliente cliente;
	
	/**
	 * Sera EMBUTIDA a Classe EnderecoEntrega
	 */
	@Embedded
	private EnderecoEntrega enderecoEntrega;
	
	/**
	 * NAO PERMITE ORFAOS
	 * 	- Se um item pedido NAO estive em um pedido sera EXCLUIDO
	 */
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)	
	private List<ItemPedido> itens = new ArrayList<>();
	
	/**
	 *Metdoos Transientes (nao persistidos no DB) 
	 * @return
	 */
	@Transient
	public boolean isNovo() {
		return getId() == null;
	}
	
	@Transient
	public boolean isExistente() {
		return !isNovo();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public StatusPedido getStatus() {
		return status;
	}

	public void setStatus(StatusPedido status) {
		this.status = status;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Usuario getVendedor() {
		return vendedor;
	}

	public void setVendedor(Usuario vendedor) {
		this.vendedor = vendedor;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public EnderecoEntrega getEnderecoEntrega() {
		return enderecoEntrega;
	}

	public void setEnderecoEntrega(EnderecoEntrega enderecoEntrega) {
		this.enderecoEntrega = enderecoEntrega;
	}

	public List<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedido> itens) {
		this.itens = itens;
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
		Pedido other = (Pedido) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
