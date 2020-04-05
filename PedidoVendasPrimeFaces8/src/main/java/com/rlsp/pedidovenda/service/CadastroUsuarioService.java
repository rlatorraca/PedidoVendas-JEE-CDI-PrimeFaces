package com.rlsp.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.rlsp.pedidovenda.model.Usuario;
import com.rlsp.pedidovenda.repository.UsuariosRepository;
import com.rlsp.pedidovenda.util.jpa.Transactional;

public class CadastroUsuarioService implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuariosRepository usuarioRepository;
	
	@SuppressWarnings("unlikely-arg-type")
	@Transactional
	public Usuario salvar(Usuario usuario) {
		Usuario usuarioExistente = usuarioRepository.produtoPorEmail(usuario.getEmail());
		
		if( usuarioExistente != null && usuarioExistente.equals(usuario.getEmail())) {
			throw new NegocioException("Email/Usuário já foi cadastrado.");
		} 
		
		return usuarioRepository.salvarAlterar(usuario);
	}
	

}
