package com.rlsp.pedidovenda.util.jpa;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

/**
 * @InterceptorBinding ==> é um INTERCEPTADOR
 * @Target({ElementType.TYPE, ElementType.METHOD}) ==> sempre que um metodo for chamada esta ANOTACAO vai entrar em acao
 *
 */

@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Transactional {

}
