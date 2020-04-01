package com.rlsp.pedidovenda.Testes;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.rlsp.pedidovenda.model.Categoria;
import com.rlsp.pedidovenda.model.Cliente;
import com.rlsp.pedidovenda.model.Endereco;
import com.rlsp.pedidovenda.model.Grupo;
import com.rlsp.pedidovenda.model.Produto;
import com.rlsp.pedidovenda.model.TipoPessoa;
import com.rlsp.pedidovenda.model.Usuario;

public class TesteEntityManager {

	public static void main(String[] args) {
		
		/**
		 *  EntityManager ==> gerencia objetos de ENTIDADES (no DB)
		 */
		EntityManagerFactory fabrica = Persistence.createEntityManagerFactory("PedidoVendaPU");
		EntityManager gerenteDeTransacoes = fabrica.createEntityManager(); // possivel trabalhar com banco de dados usando JPA
		
		
		EntityTransaction transacao = gerenteDeTransacoes.getTransaction();
		
		transacao.begin();
		
		Cliente cliente = new Cliente();
		cliente.setNome("Rodrigo");
		cliente.setEmail("rlatorraca@gmail.com");
		cliente.setTipo(TipoPessoa.FISICA);
		cliente.setDocumentoReceitaFederal("123.456.789-77");
		
		Endereco end = new Endereco();
		end.setCep("78021-456");
		end.setCidade("Pimbobas");
		end.setComplemento("do lado da mangueira central");
		end.setLogradouro("Rua da Praia Principal");
		end.setNumero("sem numero");
		end.setUf("MT");
		end.setCliente(cliente);
		
		gerenteDeTransacoes.persist(cliente);
		
		
		Usuario usuario = new Usuario();
		usuario.setNome("Maria");
		usuario.setEmail("maria@abadia.com");
		usuario.setSenha("123");
				
		gerenteDeTransacoes.persist(usuario);
		
		Grupo grupo = new Grupo();
		grupo.setNome("Vendedores");
		grupo.setDescricao("Vendedores da empresa");
		
		// instanciamos a categoria pai (Bebidas)
		Categoria categoriaPai = new Categoria();
		categoriaPai.setDescricao("Bebidas");
				
		// instanciamos a categoria filha (Refrigerantes)
		Categoria categoriaFilha = new Categoria();
		categoriaFilha.setDescricao("Refrigerantes");
		categoriaFilha.setCategoriaPai(categoriaPai);
				
		// adicionamos a categoria Refrigerantes como filha de Bebidas
		categoriaPai.getSubcategorias().add(categoriaFilha);
				
		// ao persistir a categoria pai (Refrigerantes), a filha (Bebidas) 
		// deve ser persistida também
		gerenteDeTransacoes.persist(categoriaPai);
				
		// instanciamos e persistimos um produto
		Produto produto = new Produto();
		produto.setCategoria(categoriaFilha);
		produto.setNome("Guaraná 2L");
		produto.setQuantidadeEstoque(10);
		produto.setSku("GUA00123");
		produto.setValorUnitario(new BigDecimal(2.21));
				
		gerenteDeTransacoes.persist(produto);
		usuario.getGrupos().add(grupo);
		
		cliente.getEnderecos().add(end);


		gerenteDeTransacoes.persist(produto);

		
		transacao.commit();
		
	}
}
