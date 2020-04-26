package com.rlsp.pedidovenda.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.rlsp.pedidovenda.filter.UsuarioFilter;
import com.rlsp.pedidovenda.model.Usuario;
import com.rlsp.pedidovenda.service.NegocioException;
import com.rlsp.pedidovenda.util.jpa.Transactional;

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
		
		public List<Usuario> vendedores() {
			// TODO Filtrar apenas vendedores (por um grupo específico)
			return this.gerenciador.createQuery("from Usuario", Usuario.class)
					.getResultList();
		}
		
		@Transactional
		public Usuario salvarAlterar(Usuario usuario) {
			
			return usuario = gerenciador.merge(usuario); //Inserir ou Alterar;;
		}
		
		@Transactional
		public void remover(Usuario usuario) throws NegocioException{

			try {
				usuario = porId(usuario.getId());
				gerenciador.remove(usuario); // marca para exclusão
				gerenciador.flush();
			} catch (PersistenceException e) {
				throw new NegocioException("Usuário não pode ser excluído!");				
			}
		}
		@SuppressWarnings("unchecked")
		public List<Usuario> filtrados(UsuarioFilter filtro) {
			
			CriteriaBuilder builder = gerenciador.getCriteriaBuilder();								
			CriteriaQuery<Usuario> criteriaQuery = builder.createQuery(Usuario.class);				
			List<Predicate> predicates = new ArrayList<>();	
		
			Root<Usuario> produtoRoot = criteriaQuery.from(Usuario.class);	
			
			if (StringUtils.isNotBlank(filtro.getEmail())) {
				predicates.add(builder.like(builder.lower(produtoRoot.get("email")), 
						"%" + filtro.getEmail().toLowerCase() + "%"));					
			}
			
			if (StringUtils.isNotBlank(filtro.getNome())) {
				predicates.add(builder.like(builder.lower(produtoRoot.get("nome")), 
						"%" + filtro.getNome().toLowerCase() + "%"));								
			}
			
			criteriaQuery.select(produtoRoot);															
			criteriaQuery.where(predicates.toArray(new Predicate[0]));									// Passa a lista das RESTRICOES ( Predicates )
			criteriaQuery.orderBy(builder.asc(produtoRoot.get("nome"))); 								// Ordena por NBOME de forma ASCENDENTE
			
			TypedQuery<Usuario> query = gerenciador.createQuery(criteriaQuery);							// Consulta Final criada
			return query.getResultList();
			
			
		
		}
		
//		@SuppressWarnings("unchecked")
//		public List<Usuario> filtradosOLD(UsuarioFilter filtro) {
//					
//			/**
//			 * Usando Criteria do HIBERNATE
//			 *  ** A implementacao do JPA deve ser outra
//			 */
//			//Session session = ((Session)gerenciador).unwrap(Session.class);// desempacota um SESSAO do Hibernate atraves do EntityManager
//
//			Session session = (Session) gerenciador;
//			/**
//			 * Serve para incluir restricoes na pesquisa
//			 */
//
//			Criteria criteria = session.createCriteria(Usuario.class);
//			
//			if(StringUtils.isNotBlank(filtro.getEmail())) {
//				criteria.add(Restrictions.ilike("email", filtro.getEmail(), MatchMode.ANYWHERE));
//			}
//			
//			/**
//			 *  ilike ==> insensitive (nao importa se Maiuscula ou Minuscula
//			 *  MatchMode.ANYWHERE ==> é % do sql (Serve como coringa)
//			 */
//			if(StringUtils.isNotBlank(filtro.getNome())) {
//				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
//			}
//			
//			return criteria.addOrder(Order.asc("nome")).list();
//		
//		
//		}
		
		public Usuario porEmail(String email) {

			Usuario usuario = null;
			try {
				usuario =  gerenciador.createQuery("from Usuario where upper(email) = :email", Usuario.class)
						.setParameter("email", email.toUpperCase()).getSingleResult();
			} catch (NoResultException e) {
				// nenhum usuario encontrado com o email
			}
			
			return usuario;
		}

		
		
}
