package com.rlsp.pedidovenda.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.rlsp.pedidovenda.model.Usuario;

/**
 * Classe que servepara representar um USUARIO / LOGIN do Sistema
 *  - Criado para criar um OBJETO de USER (usuario) para poder ser usado com o SPRING SECURITY / AUTENTICACAO
 *  - O objeto é importante para pegar mais informacoes do usuario
 *
 */
public class UsuarioSistema extends User {

	private static final long serialVersionUID = 1L;
	
	private Usuario usuario;
	
	public UsuarioSistema(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getEmail(), usuario.getSenha(), authorities);
		this.usuario = usuario;
	}


	public Usuario getUsuario() {
		return usuario;
	}

}
