package com.smartvalor.hl.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@EnableWebMvc
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = false)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Value("${svuser.name}")	
	protected String name;
	
	@Value("${svuser.login}")	
	protected String login;

	// Define permissions for zones.
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().ignoringAntMatchers("/api/*").csrfTokenRepository(csrfTokenRepository());
			
		http
				.authorizeRequests()			
					.antMatchers("/css/**", "/api/**", "/user/login", "/user/sorry").permitAll()
				    .antMatchers("/user/**").hasRole("USER")					
					.and()
				.formLogin()
				    .loginPage("/user/login").failureUrl("/user/sorry")
				    .loginProcessingUrl("/user/j_spring_security_check")
				    .passwordParameter("j_password").usernameParameter("j_username").defaultSuccessUrl("/user/", false);
				    
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// TODO We need to implement this much better in the finished version.
		auth
		  .inMemoryAuthentication()
				.withUser(name).password(login).roles("USER");
	}
	
	private CsrfTokenRepository csrfTokenRepository() { 
	    HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository(); 
	    repository.setSessionAttributeName("_csrf");
	    return repository; 
	}	
}
