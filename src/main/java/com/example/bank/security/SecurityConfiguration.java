package com.example.bank.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private BankDetailService bankDetailService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(getPasswordEncoder());
        provider.setUserDetailsService(bankDetailService);
        return provider;
    }

    @Bean
    public SecurityFilterChain getChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers(antMatcher(HttpMethod.GET, "/accounts")).hasAnyRole("BRANCH_MANAGER", "REGIONAL_MANAGER")
                .requestMatchers(antMatcher(HttpMethod.GET, "/accounts/**")).authenticated()
                .requestMatchers(antMatcher(HttpMethod.DELETE, "/accounts/**")).hasAnyRole("BRANCH_MANAGER", "REGIONAL_MANAGER", "ASSISTANT_MANAGER", "OPERATIONS_MANAGER")
                //.requestMatchers(antMatcher(HttpMethod.GET, "/accounts/2")).hasRole("ASSISTANT_MANAGER")
                .anyRequest().authenticated()
        ).formLogin()
                .and()
                .csrf()
                .disable()
                .headers().frameOptions().disable()
                .and()
                .httpBasic(Customizer.withDefaults())
                .logout().invalidateHttpSession(true).deleteCookies("JSESSIONID");

        return httpSecurity.build();

    }
}
