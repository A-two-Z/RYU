package com.ssafy.mulryuproject.configuration;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//@EnableWebSecurity
//@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/product").permitAll() // /product 경로는 인증 없이 접근 허용
                                .requestMatchers("/productSector/*").permitAll() // /order 경로는 인증 필요 (임시로 인증 없이 접근 허용)
                                .anyRequest().permitAll() // 나머지 모든 경로는 인증 필요
                )
                .formLogin(formLogin ->
                        formLogin.permitAll() // 로그인 페이지는 누구나 접근 가능
                                .successHandler((request, response, authentication) -> {
                                    response.setContentType("application/json");
                                    response.setStatus(HttpServletResponse.SC_OK);
                                    authentication.isAuthenticated();
                                })
                                .failureHandler((request, response, exception) -> {
                                    response.setStatus(502);
                                })
                )
                .logout(logout ->
                        logout.permitAll() // 로그아웃 페이지는 누구나 접근 가능
                );

        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.builder()
//                .username(username)
//                .password(passwordEncoder().encode(password)) // 환경 변수로부터 읽은 비밀번호를 인코딩하여 설정
//                .roles("admin")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder를 빈으로 설정
//    }

}
