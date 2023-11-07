package com.gopang.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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

    @Bean  //정적 리소스 파일 시큐리티가 무시할수 있게.
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .mvcMatchers("/node_modules/**")
                .mvcMatchers("/img/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
