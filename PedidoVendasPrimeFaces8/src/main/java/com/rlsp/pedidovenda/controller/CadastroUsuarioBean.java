package com.rlsp.pedidovenda.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.rlsp.pedidovenda.model.Grupo;
import com.rlsp.pedidovenda.model.Usuario;
import com.rlsp.pedidovenda.repository.GruposRepository;
import com.rlsp.pedidovenda.repository.UsuariosRepository;
import com.rlsp.pedidovenda.service.CadastroUsuarioService;
import com.rlsp.pedidovenda.service.NegocioException;
import com.rlsp.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroUsuarioBean implements Serializable{


	private static final long serialVersionUID = 1L;
	

	/**
	 * Faz buscas no DB para prencher a tela e/ou geral
	 */
	@Inject
	private UsuariosRepository usuarioRepository;
	@Inject
	private GruposRepository gruposRepository;
	
	/**
	 * Usado para o CRUD
	 */
	@Inject
	private CadastroUsuarioService cadastroUsuarioService;

	
	/**
	 * Atributo para trabalhar o Objeto do BEAN
	 */
	private Usuario usuario;
	
	private boolean editandoGrupo;
	
	
	private Grupo grupoEscolhido;
	private Grupo grupo;
	
	private List<Grupo> grupos;
	private List<Grupo> gruposFiltrados;

	private void limpar() {
		
		usuario = new Usuario();
		grupoEscolhido = new Grupo();		
		gruposFiltrados = new ArrayList<>();		
		editandoGrupo = false;
		
	}
	
	public CadastroUsuarioBean(){
		limpar();
		
	}
	
	
	public void salvar() throws NegocioException {
			
		usuario.setGrupos(gruposFiltrados);
		DateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
		Date date = new Date();
		try {
			System.out.println(ft.parse(ft.format(date)));
			usuario.setDataCriacao(ft.parse(ft.format(date)));
		} catch (ParseException e) {
			throw new NegocioException("Date Inexistente");
		}
		this.usuario = cadastroUsuarioService.salvar(this.usuario);
		FacesUtil.addInfoMessage("Usuário salvo com sucesso!");
		limpar();
		
		FacesUtil.addInfoMessage("Usuario salvo com sucesso!");
	}
	
	public void inicializar() {
		

		//if(FacesUtil.isNotPostBack()) {
			// Se nao for PostBack (nao for a primeira vez)
			grupos = gruposRepository.buscarGrupos();// Pega dentro de CategoriaRespository	
			if (usuario.getId() != null ) {
				editarGrupo();
			}

			
		//}

	}
	
	public void carregarGruposIncluidosTabela(){
		
		if( grupoEscolhido != null) {
			gruposFiltrados.add(this.grupoEscolhido);
			
		} else {
			System.out.println("gruposFiltrados is NULL");
		}		
	}
	
	public void imprimir() {
		System.out.println(grupoEscolhido.getNome());
	}
	
	public void salvarGrupo() {
		
		if(editandoGrupo) {
			gruposFiltrados.set(gruposFiltrados.indexOf(grupo),grupo);
			grupo = new Grupo();
		} else {
			gruposFiltrados.add(grupo);
			grupo = new Grupo();
		}
	}

	public void excluirGrupo() {
		gruposFiltrados.remove(grupoEscolhido);
	}
	
	public void editarGrupo() {
		editandoGrupo = true;		
		gruposFiltrados = usuario.getGrupos();
	}

	public UsuariosRepository getUsuarioRepository() {
		return usuarioRepository;
	}

	public void setUsuarioRepository(UsuariosRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	public GruposRepository getGruposRepository() {
		return gruposRepository;
	}

	public void setGruposRepository(GruposRepository gruposRepository) {
		this.gruposRepository = gruposRepository;
	}

	public CadastroUsuarioService getCadastroUsuarioService() {
		return cadastroUsuarioService;
	}

	public void setCadastroUsuarioService(CadastroUsuarioService cadastroUsuarioService) {
		this.cadastroUsuarioService = cadastroUsuarioService;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	public Grupo getGrupoEscolhido() {
		return grupoEscolhido;
	}

	public void setGrupoEscolhido(Grupo grupoEscolhido) {
		this.grupoEscolhido = grupoEscolhido;
	}

	public List<Grupo> getGruposFiltrados() {
		return gruposFiltrados;
	}

	public void setGruposFiltrados(List<Grupo> gruposFiltrados) {
		this.gruposFiltrados = gruposFiltrados;
	}
	
	public boolean isEditando() {
		return this.usuario.getId() != null;
	}
	
	public boolean isEditandoGrupo() {
		return editandoGrupo;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	
}
