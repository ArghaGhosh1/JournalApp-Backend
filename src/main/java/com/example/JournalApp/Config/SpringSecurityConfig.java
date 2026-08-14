package com.example.JournalApp.Config;

import com.example.JournalApp.Service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    // 1. Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }





    // 3. Security Filter Chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .authorizeHttpRequests(request -> request

                        // These endpoints require authentication
                        .requestMatchers("/journal/**", "/users/**")
                        .authenticated()

                        // Everything else is public
                        .anyRequest()
                        .permitAll()
                )

                // Enable HTTP Basic Authentication
                .httpBasic(Customizer.withDefaults())

                // Disable CSRF for your REST API
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}