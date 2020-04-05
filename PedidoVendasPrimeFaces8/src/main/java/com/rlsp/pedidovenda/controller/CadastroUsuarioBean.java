package com.rlsp.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.rlsp.pedidovenda.model.Grupo;
import com.rlsp.pedidovenda.model.Usuario;
import com.rlsp.pedidovenda.repository.GruposRepository;
import com.rlsp.pedidovenda.repository.UsuariosRepository;
import com.rlsp.pedidovenda.service.CadastroUsuarioService;
import com.rlsp.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroUsuarioBean implements Serializable{


	private static final long serialVersionUID = 1L;
	

	/**
	 * Faz buscas no DB para prencher a tela e/ou geral
	 */
	@Inject
	private UsuariosRepository usuarioRepository;
	@Inject
	private GruposRepository gruposRepository;
	
	/**
	 * Usado para o CRUD
	 */
	@Inject
	private CadastroUsuarioService cadastroUsuarioService;

	public CadastroUsuarioBean(){
		limpar();
	}
	
	/**
	 * Atributo para trabalhar o Objeto do BEAN
	 */
	private Usuario usuario;
	
	private List<Grupo> grupos; 

	private void limpar() {
		usuario = new Usuario();
		
	}
	
	public void salvar() {
		this.usuario = cadastroUsuarioService.salvar(this.usuario);
		limpar();
		
		FacesUtil.addInfoMessage("Produto salvo com sucesso!");
	}
	
	public void inicializar() {
		
		System.out.println("Inicializando .... GRUPO em CadastroUsuarioBean");

		if(FacesUtil.isNotPostBack()) {
			// Se nao for PostBack (nao for a primeira vez)
			grupos = gruposRepository.buscarGrupos();// Pega dentro de CategoriaRespository
			
			
		}
				
		System.out.println("Inicializando .... GRUPO em CadastroUsuarioBean");
	}

	public UsuariosRepository getUsuarioRepository() {
		return usuarioRepository;
	}

	public void setUsuarioRepository(UsuariosRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	public GruposRepository getGruposRepository() {
		return gruposRepository;
	}

	public void setGruposRepository(GruposRepository gruposRepository) {
		this.gruposRepository = gruposRepository;
	}

	public CadastroUsuarioService getCadastroUsuarioService() {
		return cadastroUsuarioService;
	}

	public void setCadastroUsuarioService(CadastroUsuarioService cadastroUsuarioService) {
		this.cadastroUsuarioService = cadastroUsuarioService;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}
	
	
}
