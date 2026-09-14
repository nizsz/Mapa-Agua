package com.mapaagua.config;

import com.mapaagua.security.UsuarioDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    private final UsuarioDetailsService usuarioDetailsService;

    public SecurityConfig(UsuarioDetailsService usuarioDetailsService) {
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(usuarioDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationProvider authenticationProvider) throws Exception {
        CsrfTokenRequestAttributeHandler csrfRequestHandler =
            new CsrfTokenRequestAttributeHandler();
        csrfRequestHandler.setCsrfRequestAttributeName(null);

        http
                .authenticationProvider(authenticationProvider)
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(csrfRequestHandler)
                        .ignoringRequestMatchers("/api/usuarios", "/api/auth/**")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usuarios", "/api/auth/**").permitAll()
                        .requestMatchers("/", "/index.html", "/css/**", "/js/**", "/favicon.ico").permitAll()
                    .requestMatchers(HttpMethod.PATCH, "/api/pontos/*/status").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/pontos").hasAnyRole("MORADOR", "ADMINISTRADOR")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );

        return http.build();
    }
}
