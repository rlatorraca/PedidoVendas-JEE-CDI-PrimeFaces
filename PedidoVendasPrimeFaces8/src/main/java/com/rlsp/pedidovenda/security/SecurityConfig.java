package com.rlsp.pedidovenda.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration		//Configuracao do Spring Security
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	 public SecurityConfig() {
	      super();
	 }
	 
	@Autowired
	private AppUserDetailsService userDetailsService;
	
	@Bean
	public AppUserDetailsService userDetailsService() {
		return new AppUserDetailsService();
	}
	
	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
	     auth.authenticationProvider(authProvider());
	}
	
	@Bean
	public DaoAuthenticationProvider authProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(encoder());
	    return authProvider;
	}
	 
	@Bean
	public PasswordEncoder encoder() {	
		//System.out.println("Dentro de passwordEncoder()");
	    return new BCryptPasswordEncoder(12);
		//return NoOpPasswordEncoder.getInstance();
	}
	    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		JsfLoginUrlAuthenticationEntryPoint jsfLoginEntry = new JsfLoginUrlAuthenticationEntryPoint();
		jsfLoginEntry.setLoginFormUrl("/Login.xhtml");
		jsfLoginEntry.setRedirectStrategy(new JsfRedirectStrategy());
		
		JsfAccessDeniedHandler jsfDeniedEntry = new JsfAccessDeniedHandler();
		jsfDeniedEntry.setLoginPath("/AcessoNegado.xhtml");
		jsfDeniedEntry.setContextRelative(true);
		System.out.println("Inicio configure(HttpSecurity http)");
				
		http
			.csrf().disable() // desabilta essa protecao do SPRING (csrf)
			.headers().frameOptions().sameOrigin() // Permite a abertura de Frames se for da mesma origem (do mesmo domninio / pagina)
			.and()
			
		.authorizeRequests()
			.antMatchers("/Login.xhtml", "/Erro.xhtml", "/javax.faces.resource/**").permitAll() 	// Paginas Autoridazadas a TODOS
			.antMatchers("/Home.xhtml", "/AcessoNegado.xhtml", "/dialogos/**").authenticated()		// Precisa estar Autenticados para ACESSAR
			.antMatchers("/pedidos/**").hasAnyRole("VENDEDORES", "AUXILIARES", "ADMINISTRADORES") 	// Pode ser acesadas por essas roles
			.antMatchers("/produtos/**", "/relatorios/**").hasRole("ADMINISTRADORES")				// Apenas acessado por ADMINs
			.antMatchers("/clientes/**").hasAnyRole("VENDEDORES", "AUXILIARES", "ADMINISTRADORES")
			.and()
		
		.formLogin()
			.loginPage("/Login.xhtml")					// Pagina de Login
			.failureUrl("/Login.xhtml?invalid=true")	// URL em caso de falha no login
			.and()
		
		.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // Serve para Deslogar do sisema
			.and()
		
		.exceptionHandling()
			.accessDeniedPage("/AcessoNegado.xhtml")	// em caso do acesso NEGADO
			.authenticationEntryPoint(jsfLoginEntry)	//
			.accessDeniedHandler(jsfDeniedEntry);		//
		
		System.out.println("Fim configure(HttpSecurity http)");
		
	}
	
}
