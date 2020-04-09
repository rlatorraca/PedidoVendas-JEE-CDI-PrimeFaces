package com.rlsp.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.rlsp.pedidovenda.filter.UsuarioFilter;
import com.rlsp.pedidovenda.model.Produto;
import com.rlsp.pedidovenda.model.Usuario;
import com.rlsp.pedidovenda.repository.UsuariosRepository;
import com.rlsp.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaUsuariosBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuariosRepository usuarioRepository;
	
	private UsuarioFilter filtro;
	
	private List<Usuario> usuariosFiltrados;
	
	private Usuario usuarioSelecionado;
	
	public PesquisaUsuariosBean() {
		limpar();
	}
	
	private void limpar() {
		filtro = new UsuarioFilter();
		usuariosFiltrados = new ArrayList<>();
		usuarioSelecionado = new Usuario();
	}
	
	public void pesquisarFiltrados() {
		usuariosFiltrados = usuarioRepository.filtrados(filtro);

	}
	
	public void excluir() {
		usuarioRepository.remover(usuarioSelecionado);
		usuariosFiltrados.remove(usuarioSelecionado);
		
		FacesUtil.addInfoMessage("Usuário " + usuarioSelecionado.getNome() + " (" + usuarioSelecionado.getEmail() + ") foi excluído com sucesso!");
	}

	public List<Usuario> getUsuariosFiltrados() {
		return usuariosFiltrados;
	}

	public UsuarioFilter getFiltro() {
		return filtro;
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public UsuariosRepository getUsuarioRepository() {
		return usuarioRepository;
	}

	public void setUsuarioRepository(UsuariosRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	public void setFiltro(UsuarioFilter filtro) {
		this.filtro = filtro;
	}

	public void setUsuariosFiltrados(List<Usuario> usuariosFiltrados) {
		this.usuariosFiltrados = usuariosFiltrados;
	}
	
	
}
