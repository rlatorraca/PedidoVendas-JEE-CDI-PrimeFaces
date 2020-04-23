package com.rlsp.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.rlsp.pedidovenda.model.Usuario;
import com.rlsp.pedidovenda.repository.UsuariosRepository;

/**
 * @FacesConverter(forClass = Categoria.class)
 *  - @FacesConverter ==> diz que eh um conversor
 *  - forClass = Categoria.class ==> para a Entidade Categoria
 */
@FacesConverter(forClass = Usuario.class)
public class UsuarioConverter implements Converter<Object> {

	/**
	 * IMPORTANTE
	 *  - @Inject ==> NAO FUNCIONA DENTRO DO CONVERTER/CONVERSOR
	 */
	@Inject
	private UsuariosRepository usuarioRepository;
	
//	public UsuarioConverter() {
//		/**
//		 * Como a @Inject nao funciona temos que chamar o metodos getBean() na Classe CDIServiceLocator passando uma CLASSE para RETORNAR um INSTANCIA DA CLASSE no contexto da CDI
//		 */
//		usuarioRepository = CDIServiceLocator.getBean(UsuariosRepository.class); 
//		
//	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		
		Usuario retorno = null;
		
		if(StringUtils.isNotEmpty(value)) {
			Long id = Long.parseLong(value);
			retorno = usuarioRepository.porId(id);
			
			
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if(value != null) {
			Usuario usuario = (Usuario) value;
			return usuario.getId() == null ? null : usuario.getId().toString();
		}
		return "";
	}

}
