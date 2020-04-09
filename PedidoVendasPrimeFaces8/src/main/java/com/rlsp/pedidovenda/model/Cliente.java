package com.rlsp.pedidovenda.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank @Size(max = 150)
	@Column(name = "nome_cliente", nullable = false, length = 150)
	private String nome;
	
	@NotBlank @Size(max = 200)
	@Column(name = "email_cliente", nullable = false, length = 200)
	private String email;
	
	@NotBlank @Size(max = 18)
	@Column(name = "sin_cliente", nullable = false, length = 18)
	private String documentoReceitaFederal;
	
	@Enumerated(EnumType.STRING) //Ordinal (0,1,3...) String (texto do Enum)
	@Column(name = "tipo_pessoa_cliente", nullable = false, length = 10)
	private TipoPessoa tipo;
	
	/**
	 *
	 * @Transient ==> JPA ignora os objetos do tipo @Transient
	 *  ** mappedBy = "cliente"  ==> conecta em Endereco no Atritbuto "cliente" na Entdidade Endereço
	 *  ** cascade = CascadeType.ALL ==> toda vez que ouver alguma transacao usando a Entidade Cliente (incluir, deletar, modificar, etc) deve ser feito em cascade na Entidade 
	 *  	"Endereco"
	 */

	@OneToMany(mappedBy = "cliente" ,cascade = CascadeType.ALL) 
	private List<Endereco> enderecos = new ArrayList<>(); //Cliente tem 1+ Enderecos

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDocumentoReceitaFederal() {
		return documentoReceitaFederal;
	}

	public void setDocumentoReceitaFederal(String documentoReceitaFederal) {
		this.documentoReceitaFederal = documentoReceitaFederal;
	}
	
	public TipoPessoa getTipo() {
		return tipo;
	}

	public void setTipo(TipoPessoa tipo) {
		this.tipo = tipo;
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
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
		Cliente other = (Cliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
