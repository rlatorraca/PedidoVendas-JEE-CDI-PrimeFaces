package com.rlsp.pedidovenda.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.convert.ClientConverter;

import com.rlsp.pedidovenda.model.Categoria;
import com.rlsp.pedidovenda.model.Produto;
import com.rlsp.pedidovenda.repository.ProdutosRepository;
import com.rlsp.pedidovenda.util.cdi.CDIServiceLocator;

/**
 * @FacesConverter(forClass = Categoria.class)
 *  - @FacesConverter ==> diz que eh um conversor
 *  - forClass = Categoria.class ==> para a Entidade Categoria
 */
@FacesConverter(forClass = Produto.class)
public class ProdutoConverter implements Converter<Object>, ClientConverter {

	/**
	 * IMPORTANTE
	 *  - @Inject ==> NAO FUNCIONA DENTRO DO CONVERTER/CONVERSOR
	 */
	@Inject
	private ProdutosRepository produtoRepository;
	
//	public ProdutoConverter() {
//		/**
//		 * Como a @Inject nao funciona temos que chamar o metodos getBean() na Classe CDIServiceLocator passando uma CLASSE para RETORNAR um INSTANCIA DA CLASSE no contexto da CDI
//		 */
//		produtoRepository = CDIServiceLocator.getBean(ProdutosRepository.class); 
//		
//	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
				
		Produto retorno = null;
		
		/**
		 * StringUtils.isEmpty ==> verifica se e NULO ou VAZIO
		 */
		if(StringUtils.isNotEmpty(value)) {
			Long id = Long.parseLong(value);
			retorno = produtoRepository.porId(id);
			
			
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
//		if(value != null) {
//			return ((Produto) value).getId().toString();
//		}
		
		if(value != null) {
			Produto produto = (Produto) value;
			return produto.getId() == null ? null : produto.getId().toString();
		}
		return "";
	}

	/**
	 * Serve para converter os dados de "" para "null" para validar "Client side" para Produto
	 */
	@Override
	public String getConverterId() {
		
		return "com.rlsp.Produto";
	}

	@Override
	public Map<String, Object> getMetadata() {
		return null;
	}


}
