package com.rlsp.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.rlsp.pedidovenda.model.Grupo;
import com.rlsp.pedidovenda.model.Produto;
import com.rlsp.pedidovenda.model.Usuario;
import com.rlsp.pedidovenda.repository.GruposRepository;
import com.rlsp.pedidovenda.util.cdi.CDIServiceLocator;

/**
 * @FacesConverter(forClass = Categoria.class)
 *  - @FacesConverter ==> diz que eh um conversor
 *  - forClass = Categoria.class ==> para a Entidade Categoria
 */
@FacesConverter(forClass = Grupo.class)
public class GrupoConverter implements Converter<Object> {

	/**
	 * IMPORTANTE
	 *  - @Inject ==> NAO FUNCIONA DENTRO DO CONVERTER/CONVERSOR
	 */
	//@Inject
	private GruposRepository grupoRepository;
	
	public GrupoConverter() {
		/**
		 * Como a @Inject nao funciona temos que chamar o metodos getBean() na Classe CDIServiceLocator passando uma CLASSE para RETORNAR um INSTANCIA DA CLASSE no contexto da CDI
		 */
		grupoRepository = CDIServiceLocator.getBean(GruposRepository.class); 
		
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
				
		Grupo retorno = null;
		
		if(value != null) {
			Long id = Long.parseLong(value);
			retorno = grupoRepository.porId(id);			
			
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
//		if(value != null) {
//			return ((Produto) value).getId().toString();
//		}
		
		if(value != null) {
			Grupo grupo = (Grupo) value;
			return grupo.getId() == null ? null : grupo.getId().toString();
		}
		return "";
	}

}
