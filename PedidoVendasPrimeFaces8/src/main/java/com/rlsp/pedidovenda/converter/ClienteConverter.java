package com.rlsp.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.rlsp.pedidovenda.model.Cliente;
import com.rlsp.pedidovenda.repository.ClientesRepository;

/**
 * @FacesConverter(forClass = Categoria.class)
 *  - @FacesConverter ==> diz que eh um conversor
 *  - forClass = Categoria.class ==> para a Entidade Categoria
 */
@FacesConverter(forClass = Cliente.class)
public class ClienteConverter implements Converter<Object> {

	/**
	 * IMPORTANTE
	 *  - @Inject ==> NAO FUNCIONA DENTRO DO CONVERTER/CONVERSOR
	 */
	@Inject
	private ClientesRepository clienteRepository;
	
//	public ClienteConverter() {
//		/**
//		 * Como a @Inject nao funciona temos que chamar o metodos getBean() na Classe CDIServiceLocator passando uma CLASSE para RETORNAR um INSTANCIA DA CLASSE no contexto da CDI
//		 */
//		clienteRepository = CDIServiceLocator.getBean(ClientesRepository.class); 
//		
//	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		
		Cliente retorno = null;
		
		if(StringUtils.isNotEmpty(value)) {
			Long id = Long.parseLong(value);
			retorno = clienteRepository.porId(id);
			return retorno;
			
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if(value != null) {
			Cliente cliente = (Cliente) value;
			return cliente.getId() == null ? null : cliente.getId().toString();
		}
		return "";
	}

}
