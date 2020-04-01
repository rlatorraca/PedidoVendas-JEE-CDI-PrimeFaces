package com.rlsp.pedidovenda.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="usuario")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nome_usuario", nullable = false, length = 150)
	private String nome;
	
	/**
	 * "unique" = true
	 *  - Isso representa um índice único para a coluna, que será criado automaticamente pelo Hibernate na tabela do banco de dados.
	 */
	@Column(name = "email_usuario", nullable = false, length = 200, unique = true)
	private String email;
	
	@Column(name = "senha_usuario", nullable = false, length = 30)
	private String senha;
	/**
	 * O @ManyToMany é um tipo de mapeamento muitos-para-muitos, ou seja, muitos usuários podem ter muitos grupos, e muitos grupos podem ter muitos usuários.
	 * Uma relação muitos-para-muitos possui uma tabela de relacionamento. Essa tabela é mapeada pela anotação @JoinTable. 
	 * 	- O nome da tabela é especificada no atributo "name".
	 *  - O atributo "joinColumns" define o nome da coluna na tabela de relacionamento que faz referência com a tabela atual (da entidade Usuario). 
	 *     ** Neste caso, "usuario_id".
	 *  - O atributo "inverseJoinColumns" define o nome da coluna na tabela de relacionamento que faz referência com a tabela inversa (da entidade Grupo). 
	 *     ** Neste caso, grupo_id.
	 *  - No atributo cascade da anotação @ManyToMany, colocamos CascadeType.ALL, para salvar automaticamente os grupos associados ao usuário.
	 *  - 
	 */
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "usuario_grupo", 
	           joinColumns = @JoinColumn(name = "usuario_id"),
			   inverseJoinColumns = @JoinColumn(name = "grupo_id"))
	private List<Grupo> grupos = new ArrayList<>();
	
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
	
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public List<Grupo> getGrupos() {
		return grupos;
	}
	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
