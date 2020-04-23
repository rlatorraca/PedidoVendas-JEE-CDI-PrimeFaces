package com.rlsp.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.rlsp.pedidovenda.model.Pedido;
import com.rlsp.pedidovenda.repository.PedidosRepository;
import com.rlsp.pedidovenda.util.cdi.CDIServiceLocator;

/**
 * @FacesConverter(forClass = Categoria.class)
 *  - @FacesConverter ==> diz que eh um conversor
 *  - forClass = Categoria.class ==> para a Entidade Categoria
 */
@FacesConverter(forClass = Pedido.class)
public class PedidoConverter implements Converter<Object> {

	@Inject
	private PedidosRepository pedidoRepository;
	
//	public PedidoConverter() {
//		pedidoRepository = CDIServiceLocator.getBean(PedidosRepository.class);
//	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Pedido retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = Long.parseLong(value);
			retorno = pedidoRepository.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Pedido pedido = (Pedido) value;
			return pedido.getId() == null ? null : pedido.getId().toString();
		}
		
		return "";
	}

}