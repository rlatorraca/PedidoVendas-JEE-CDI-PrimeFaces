package com.rlsp.pedidovenda.model;

import java.io.Serializable;

import javax.persistence.Column;

public class EnderecoEntrega implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="logradouro_entrega", nullable = false, length = 150)
	private String logradouro;
	
	@Column(name="numero_entrega", nullable = false, length = 10)
	private String numero;
	
	@Column(name="complemento_entrega", length = 150)
	private String complemento;
	
	@Column(name="cidade_entrega", nullable = false, length = 120)
	private String cidade;
	
	@Column(name="uf_entrega", nullable = false, length = 60)
	private String uf;
	
	@Column(name="cep_entrega", nullable = false, length = 9)
	private String cep;

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}
	
	

}
