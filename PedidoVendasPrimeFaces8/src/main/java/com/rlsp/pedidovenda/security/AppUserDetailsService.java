package com.rlsp.pedidovenda.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.rlsp.pedidovenda.model.Grupo;
import com.rlsp.pedidovenda.model.Usuario;
import com.rlsp.pedidovenda.repository.UsuariosRepository;
import com.rlsp.pedidovenda.util.cdi.CDIServiceLocator;

/**
 * UserDetailsService  ==> prove os detalhes dos usuarios. 
 *  - Interface do Spring Security
 *
 */
public class AppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		/**
		 * Como nao eh possivel injetar o "UsuarioRepositorio" pois este um BEAN do SPRING SECURITY e ano do CDI.
		 *  -Assim usareoms o "CDIServiceLocator" (mesma classe usada nos conversores) para buscar os BEANS de UsuarioRepository
		 */
		UsuariosRepository usuarioRepository = CDIServiceLocator.getBean(UsuariosRepository.class);
		Usuario usuario = usuarioRepository.porEmail(email);
		
		UsuarioSistema user = null;
		
		if (usuario != null) {
			user = new UsuarioSistema(usuario, getGrupos(usuario));
		} else {
			throw new UsernameNotFoundException("Usuário não encontrado.");
		}
		
		return user;
	}

	private Collection<? extends GrantedAuthority> getGrupos(Usuario usuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for (Grupo grupoUsuario : usuario.getGrupos()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + grupoUsuario.getNome().toUpperCase()));
		}
		
		return authorities;
	}

}
