package com.rlsp.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.rlsp.pedidovenda.filter.ClienteFilter;
import com.rlsp.pedidovenda.filter.UsuarioFilter;
import com.rlsp.pedidovenda.model.Cliente;
import com.rlsp.pedidovenda.model.Usuario;
import com.rlsp.pedidovenda.service.NegocioException;
import com.rlsp.pedidovenda.util.jpa.Transactional;

public class ClientesRepository implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager gerenciador;

	public Cliente produtoPorCPForCNPJ(String documentoReceitaFederal) {
		try {
			String jpql = "from Cliente where documentoReceitaFederal = : cpfCnpj";
			return gerenciador.createQuery(jpql, Cliente.class).setParameter("cpfCnpj", documentoReceitaFederal)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
			
		}
		
	}

	@Transactional
	public Cliente salvarAlterar(Cliente cliente) {
		
		return cliente = gerenciador.merge(cliente); //Inserir ou Alterar;;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Cliente> filtrados(ClienteFilter filtro) {
				
		/**
		 * Usando Criteria do HIBERNATE
		 *  ** A implementacao do JPA deve ser outra
		 */
		Session session = ((Session)gerenciador).unwrap(Session.class);// desempacota um SESSAO do Hibernate atraves do EntityManager
		
		/**
		 * Serve para incluir restricoes na pesquisa
		 */

		Criteria criteria = session.createCriteria(Cliente.class);
		
		if(StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}
		
		/**
		 *  ilike ==> insensitive (nao importa se Maiuscula ou Minuscula
		 *  MatchMode.ANYWHERE ==> é % do sql (Serve como coringa)
		 */
		if(StringUtils.isNotBlank(filtro.getDocumentoReceitaFederal())) {
			criteria.add(Restrictions.ilike("documentoReceitaFederal", filtro.getDocumentoReceitaFederal(), MatchMode.EXACT));
		}
		
		return criteria.addOrder(Order.asc("nome")).list();
	
	
	}
	
	
	@Transactional
	public void remover(Cliente cliente) {

		try {
			cliente = porId(cliente.getId());
			gerenciador.remove(cliente); // marca para exclusão
			gerenciador.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Cliente não pode ser excluído!");				
		}
	}
	
	public Cliente porId(Long id) {
		
		return gerenciador.find(Cliente.class, id);
	}
	
}
