package com.rlsp.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
		String result = encoder.encode(usuario.getSenha());
		usuario.setSenha(result);
		return usuarioRepository.salvarAlterar(usuario);
	}
	

}
