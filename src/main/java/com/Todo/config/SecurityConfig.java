package com.Todo.config;

import com.Todo.config.filter.JwtTokenFilter;
import com.Todo.service.CustomUserDetailService;
import com.Todo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${jwt.secret-key}")
    private String key;
    private final CustomUserDetailService userDetailService;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        RequestMatcher swaggerMatcher = request ->
                request.getRequestURI().startsWith("/swagger") ||
                        request.getRequestURI().startsWith("/v3/api-docs") ||
                        request.getRequestURI().startsWith("/swagger-ui");

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement((sm) -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((auth) ->
                                auth
                                        .requestMatchers(swaggerMatcher).permitAll()
                                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                        .requestMatchers("/api/user/**").permitAll()
                                        .requestMatchers("/api/**").authenticated()
                        )
                .addFilterBefore(new JwtTokenFilter(key,userDetailService), UsernamePasswordAuthenticationFilter.class)
                .build();

    }

}
