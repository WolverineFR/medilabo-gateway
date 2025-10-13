package com.openclassrooms.medilabo.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Configuration de la sécurité Spring WebFlux pour la gateway Medilabo.
 */
@Configuration
@EnableWebFluxSecurity
public class SpringSecurityConfig {

	/**
	 * Configure la chaîne de filtres de sécurité (SecurityWebFilterChain) pour
	 * WebFlux.
	 *
	 * @param http l’objet {@link ServerHttpSecurity} fourni par Spring
	 * @return une configuration de sécurité réactive
	 */
	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http.csrf(csrf -> csrf.disable()).authorizeExchange(exchanges -> exchanges.anyExchange().authenticated())
				.httpBasic(Customizer.withDefaults()).formLogin(form -> form.disable());
		return http.build();
	}

	/**
	 * Définit un service d’utilisateurs en mémoire pour la démonstration.
	 * 
	 * @param passwordEncoder encodeur de mots de passe injecté
	 * @return instance de {@link MapReactiveUserDetailsService} contenant les
	 *         utilisateurs
	 */
	@Bean
	public MapReactiveUserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
		UserDetails user = User.withUsername("user").password(passwordEncoder.encode("user")).roles("USER").build();

		UserDetails admin = User.withUsername("admin").password(passwordEncoder.encode("admin")).roles("USER", "ADMIN")
				.build();

		return new MapReactiveUserDetailsService(user, admin);
	}

	/**
	 * Fournit un encodeur de mots de passe basé sur BCrypt.
	 *
	 * @return une instance de {@link BCryptPasswordEncoder}
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
