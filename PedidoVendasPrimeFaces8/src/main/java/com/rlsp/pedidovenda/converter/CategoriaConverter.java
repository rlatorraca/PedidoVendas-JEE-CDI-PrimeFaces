package com.rlsp.pedidovenda.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.convert.ClientConverter;

import com.rlsp.pedidovenda.model.Categoria;
import com.rlsp.pedidovenda.repository.CategoriasRepository;
import com.rlsp.pedidovenda.util.cdi.CDIServiceLocator;

/**
 * @FacesConverter(forClass = Categoria.class)
 *  - @FacesConverter ==> diz que eh um conversor
 *  - forClass = Categoria.class ==> para a Entidade Categoria
 */
@FacesConverter(forClass = Categoria.class)
public class CategoriaConverter implements Converter<Object> , ClientConverter{

	/**
	 * IMPORTANTE
	 *  - @Inject ==> NAO FUNCIONA DENTRO DO CONVERTER/CONVERSOR
	 */
	//@Inject
	private CategoriasRepository categoriasRepository;
	
	public CategoriaConverter() {
		/**
		 * Como a @Inject nao funciona temos que chamar o metodos getBean() na Classe CDIServiceLocator passando uma CLASSE para RETORNAR um INSTANCIA DA CLASSE no contexto da CDI
		 */
		categoriasRepository = CDIServiceLocator.getBean(CategoriasRepository.class); 
		
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		
		Categoria retorno = null;
		
		if(value != null) {
			Long id = Long.parseLong(value);
			retorno = categoriasRepository.porId(id);
			
			
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if(value != null) {
			return ((Categoria) value).getId().toString();
		}
		
		return "";
	}

	/**
	 * Serve para converter os dados de "" para "null" para validar "Client side" apra Categorias
	 */
	@Override
	public String getConverterId() {
		
		return "com.rlsp.Categoria";
	}

	@Override
	public Map<String, Object> getMetadata() {
		return null;
	}
}
