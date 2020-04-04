package com.rlsp.pedidovenda.converter;

import javax.enterprise.inject.spi.CDI;
import javax.faces.annotation.FacesConfig;
import javax.faces.annotation.FacesConfig.Version;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import com.rlsp.pedidovenda.model.Categoria;
import com.rlsp.pedidovenda.repository.CategoriaRepository;
import com.rlsp.pedidovenda.util.cdi.CDIServiceLocator;

/**
 * @FacesConverter(forClass = Categoria.class)
 *  - @FacesConverter ==> diz que eh um conversor
 *  - forClass = Categoria.class ==> para a Entidade Categoria
 */
@FacesConverter(forClass = Categoria.class)
public class CategoriaConverter implements Converter<Object> {

	/**
	 * IMPORTANTE
	 *  - @Inject ==> NAO FUNCIONA DENTRO DO CONVERTER/CONVERSOR
	 */
	//@Inject
	private CategoriaRepository categoriaRepository;
	
	public CategoriaConverter() {
		/**
		 * Como a @Inject nao funciona temos que chamar o metodos getBean() na Classe CDIServiceLocator passando uma CLASSE para RETORNAR um INSTANCIA DA CLASSE no contexto da CDI
		 */
		categoriaRepository = CDIServiceLocator.getBean(CategoriaRepository.class); 
		
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		
		Categoria retorno = null;
		
		if(value != null) {
			Long id = Long.parseLong(value);
			retorno = categoriaRepository.porId(id);
			return retorno;
			
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if(value != null) {
			return ((Categoria) value).getId().toString();
		}
		
		return null;
	}

}
