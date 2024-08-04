package com.todoseventos.todos_eventos.security;

import com.todoseventos.todos_eventos.security.jwt.AuthEntryPointJwt;
import com.todoseventos.todos_eventos.security.jwt.AuthFilterToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    /**
     * Define o bean para o codificador de senha (BCrypt).
     * @return Uma instância de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define o bean para o gerenciador de autenticação.
     * @param authenticationConfiguration A configuração de autenticação.
     * @return Uma instância de AuthenticationManager.
     * @throws Exception se ocorrer um erro ao obter o AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Define o bean para o filtro de autenticação JWT.
     * @return Uma instância de AuthFilterToken.
     */
    @Bean
    public AuthFilterToken authFilterToken() {
        return new AuthFilterToken();
    }

    /**
     * Configura a cadeia de filtros de segurança.
     * @param http O objeto HttpSecurity para configurar a segurança da aplicação.
     * @return Uma instância de SecurityFilterChain.
     * @throws Exception se ocorrer um erro durante a configuração da segurança.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        // Configura CORS com as configurações padrão
        http.cors(Customizer.withDefaults());
        // Desabilita CSRF e configura o gerenciamento de sessão
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configura as autorizações de requisições
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui/index.html**").permitAll() // Permite o acesso ao Swagger sem autenticação
                        .anyRequest().authenticated());

        // Adiciona o filtro de autenticação JWT antes do filtro de autenticação de nome de usuário e senha
        http.addFilterBefore(authFilterToken(), UsernamePasswordAuthenticationFilter.class);

        // Constrói e retorna a cadeia de filtros de segurança
        return http.build();
    }
}
