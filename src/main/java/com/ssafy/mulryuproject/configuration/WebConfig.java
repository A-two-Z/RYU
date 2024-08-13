package com.ssafy.mulryuproject.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Spring 서버 전역적으로 CORS 설정
@Configuration
public class WebConfig implements WebMvcConfigurer {
 @Override
 public void addCorsMappings(CorsRegistry registry) {
     registry.addMapping("/**")
             .allowedOriginPatterns("https://be-proto.vercel.app/", "https://ssafymulryu.kro.kr/") // “*“같은 와일드카드를 사용
             .allowedMethods("GET", "POST", "OPTIONS") // 허용할 HTTP method
             .allowedHeaders("*") // 허용할 헤더를 설정합니다.
             .allowCredentials(true); // 쿠키 인증 요청 허용
 }
}