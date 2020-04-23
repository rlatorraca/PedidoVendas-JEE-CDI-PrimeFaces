package com.rlsp.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.rlsp.pedidovenda.model.Endereco;
import com.rlsp.pedidovenda.repository.EnderecosRepository;

/**
 * @FacesConverter(forClass = Categoria.class)
 *  - @FacesConverter ==> diz que eh um conversor
 *  - forClass = Categoria.class ==> para a Entidade Categoria
 */
@FacesConverter(forClass = Endereco.class)
public class EnderecoConverter implements Converter<Object> {

	/**
	 * IMPORTANTE
	 *  - @Inject ==> NAO FUNCIONA DENTRO DO CONVERTER/CONVERSOR
	 */
	@Inject
	private EnderecosRepository enderecoRepository;
	
//	public EnderecoConverter() {
//		/**
//		 * Como a @Inject nao funciona temos que chamar o metodos getBean() na Classe CDIServiceLocator passando uma CLASSE para RETORNAR um INSTANCIA DA CLASSE no contexto da CDI
//		 */
//		enderecoRepository = CDIServiceLocator.getBean(EnderecosRepository.class); 
//		
//	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		
		Endereco retorno = null;
		
		if(StringUtils.isNotEmpty(value)) {
			Long id = Long.parseLong(value);
			retorno = enderecoRepository.porId(id);
			
			
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if(value != null && !"".equals(value)) {
			Endereco endereco = (Endereco) value;
			return endereco.getId() == null ? null : endereco.getId().toString();
		}
		return "";
	}

}
