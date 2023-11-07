package com.gopang.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests()
                .mvcMatchers("/", "/login", "/sign-up", "/check-email-token","/login-by-email",
                        "/email-login", "/check-email-login","terms","/company/*", "login-link", "/profile/*").permitAll()
                .mvcMatchers("/boardMain/*","/item/*","/community/*","/community/qna/*","/community/ntc/*")
                .permitAll()
                .mvcMatchers(HttpMethod.GET, "/profile/*").permitAll()
                .mvcMatchers("/images/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/api/qna/*/comments").authenticated() // 해당 엔드포인트를 인증된 사용자만 접근 허용
                .mvcMatchers("/admin/**").hasRole("ADMIN") //  ADMIN 권한 필요
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().logoutSuccessUrl("/")
                .and().build();
    }
}
