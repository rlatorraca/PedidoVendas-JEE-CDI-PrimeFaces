package com.rlsp.pedidovenda.util.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class EntityManagerProducer {

	private EntityManagerFactory gerenciador;
	
	/**
	 * Construtor
	 */
	public EntityManagerProducer() {
		gerenciador = Persistence.createEntityManagerFactory("PedidoVendaPU");
	}
	
	/**
	 * @Produces ==> produtor de Entity Manager
	 * @RequestScoped ==> o Entity Manager produzido por esse metodo tem o ESCOPO DE REQUISICAO
	 * 
	 */
	@Produces @RequestScoped
	public EntityManager createEntityManager() {
		return gerenciador.createEntityManager();
	}
	
	/**
	 * @Disposes ==> gatilho para finalizar algo 
	 */
	public void closeEntityManager(@Disposes EntityManager manager) {
		manager.close();
	}
}
