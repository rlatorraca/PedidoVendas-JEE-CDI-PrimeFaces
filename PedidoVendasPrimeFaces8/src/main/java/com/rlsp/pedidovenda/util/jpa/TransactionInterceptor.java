package com.rlsp.pedidovenda.util.jpa;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * INTERCEPTADOR PARA GERENCIAR AS CONEXOES COM O DB 
 * @Interceptor ==> mostra que eh um interceptador
 * @Transactional
 */

@Interceptor
@Transactional // Chama a ANOTACAO
public class TransactionInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;

	private @Inject EntityManager manager;
	
	/**
	 * Chamado ANTES De um METODO @Transactional
	 */
	 
	@AroundInvoke
	public Object invoke(InvocationContext context) throws Exception {
		EntityTransaction gerenciadorDeTransacoes = manager.getTransaction();
		boolean criador = false;

		try {
			if (!gerenciadorDeTransacoes.isActive()) {
				// truque para fazer rollback no que já passou
				// (senão, um futuro commit, confirmaria até mesmo operações sem transação)
				gerenciadorDeTransacoes.begin();
				gerenciadorDeTransacoes.rollback();
				
				// agora sim inicia a transação
				gerenciadorDeTransacoes.begin();
				
				criador = true;
			}

			return context.proceed(); // ==> SE TUDO ESTIVER OK, Chamara o METODO de TRANSACOES
		} catch (Exception e) {
			if (gerenciadorDeTransacoes != null && criador) {
				gerenciadorDeTransacoes.rollback();
			}

			throw e;
		} finally {
			if (gerenciadorDeTransacoes != null && gerenciadorDeTransacoes.isActive() && criador) {
				gerenciadorDeTransacoes.commit();
			}
		}
	}
	
}