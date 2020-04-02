package com.rlsp.pedidovenda.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

/**
 * 
 * @interface SKU ==> ANOTACAO customizada que pode ser usada em todo o Codigo JAVA
 *  - @SKU ==> Nova anotacao
 * 
 * @Target ==> mostra onde eu posso usar a ANOTACAO
 *	- ElementType.FIELD = pode ser usado em CAMPOS/ATRIBUTOS
 *  - ElementType.METHOD = pode ser usar em METODOS
 *  -  ElementType.ANNOTATION_TYPE = pode ser usados em ANOTACOES
 *  
 * @Retention(RetentionPolicy.RUNTIME)
 *  - Pode ser lida em TEMPO DE EXECUCAO
 *  
 * @Constraint(validatedBy = {})
 * 	- recebe uma lista de CLASSES JAVA que implementa a avalidade e nao usa um REGEXP (expressao regular) 
 * 
 * @Pattern(regexp = "([a-zA-Z]{3}\\d{4,18})?")
 *  - Padrao da expressa regular
 *  - toda vez que se quiser u
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Pattern(regexp = "([a-zA-Z]{3}\\d{4,18})?")
public @interface SKU {

	/**
	 * O Codido abaixo È OBRIGATORIO para qualquer anotacao CUSTOMIZADA mesmo que nao va ser usado dentro dos codigo
	 * 
	 */
	
	/**
	 * @OverridesAttribute(constraint = Pattern.class, name = "message")
	 *  - serve para modificar a messagem pega pelo JAVA que seria ==> "([a-zA-Z]{3}\\d{4,18})?"
	 *  - assim, pega a MESSAGE default abaxo ==> "tem formato inválido"
	 *  - IMPORTANTE ==> serve para CUSTOMIZAR A MENSAGEM
	 *    ** è possivel tambem customizar a mensagem na anotacao dentro BEAN (no caso dentro @SKU)
	 *    
	 *  String message() default "{com.rlsp.constraints.SKU.message}"; ==> esta dentro arquivo src/main/resources/ValidationMessages.Properties  
	 */
	@OverridesAttribute(constraint = Pattern.class, name = "message")
	String message() default "{com.rlsp.constraints.SKU.message}";
	//String message() default "tem formato inválido";
	
	Class<?>[] groups() default {}; // É possivel AGRUPAR VALIDACOES
	
	Class<? extends Payload> [] payload() default {}; // O payload carrega objetos com informacoes da validacao 
	
	
}
