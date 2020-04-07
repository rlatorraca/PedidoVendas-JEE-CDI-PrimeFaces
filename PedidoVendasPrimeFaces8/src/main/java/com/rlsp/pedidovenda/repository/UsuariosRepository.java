package com.rlsp.pedidovenda.repository;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.rlsp.pedidovenda.model.Usuario;

public class UsuariosRepository	implements Serializable{

		
		private static final long serialVersionUID = 1L;
		
		@Inject
		private EntityManager gerenciador;
		
		public Usuario produtoPorEmail(String email) {
			try {
				String jpql = "from Usuario where upper(email) = : email";
				return gerenciador.createQuery(jpql, Usuario.class).setParameter("email", email.toUpperCase())
						.getSingleResult();
			} catch (NoResultException e) {
				return null;
				
			}
		}

		public Usuario porId(Long id) {
			
			return gerenciador.find(Usuario.class, id);
		}
		
		public Usuario salvarAlterar(Usuario usuario) {
			
			return usuario = gerenciador.merge(usuario); //Inserir ou Alterar;;
		}
		
//		public List<Categoria> incluirGruposUsuario(Grupo grupoEscolhido){
//			String jpql = "from usuario_grupo where categoriaPai = :raiz";
//			return gerenciador.createQuery(jpql, Categoria.class)
//					.setParameter("raiz", categoriaPai)
//					.getResultList();
//			
//			return usuarioGrupo = gerenciador.merge(usuario); //Inserir ou Alterar;;
//		}
}
