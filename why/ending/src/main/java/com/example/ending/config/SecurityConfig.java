package com.example.ending.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()  // 启用 CORS
                .csrf().disable() // 如果您使用前后端分离架构，通常会禁用 CSRF 保护
                .authorizeRequests()
                .antMatchers("/user/login").permitAll()  // 允许所有人访问登录端点
                .antMatchers("/echarts/**").permitAll() // 允许请求到达 JwtInterceptor
                .antMatchers("/user/**").permitAll() // 允许请求到达 JwtInterceptor
                .antMatchers("/role/**").permitAll() // 允许请求到达 JwtInterceptor
                .antMatchers("/menu/**").permitAll() // 允许请求到达 JwtInterceptor
                .antMatchers("/file/**").permitAll() // 允许请求到达 JwtInterceptor
                .antMatchers("/person").permitAll() // 允许请求到达 JwtInterceptor
                //
                .antMatchers("/doctor/**").permitAll() // 允许请求到达 JwtInterceptor
                .antMatchers("/doctorlocations/**").permitAll() // 允许请求到达 JwtInterceptor
                .antMatchers("/drugsstore/**").permitAll() // 允许请求到达 JwtInterceptor
                .antMatchers("/departments/**").permitAll() // 允许请求到达 JwtInterceptor
                .antMatchers("/patients/**").permitAll() // 允许请求到达 JwtInterceptor
                .antMatchers("/treatments/**").permitAll() // 允许请求到达 JwtInterceptor
                .antMatchers("/payments/**").permitAll() // 允许请求到达 JwtInterceptor
                .antMatchers("/appointments/**").permitAll() // 允许请求到达 JwtInterceptor
                .antMatchers("/medicalRecords/**").permitAll() // 允许请求到达 JwtInterceptor
                .antMatchers("/treatmentHistory/**").permitAll() // 允许请求到达 JwtInterceptor
                .antMatchers("/procedures/**").permitAll() // 允许请求到达 JwtInterceptor
                //
                .anyRequest().authenticated() // 其他所有请求都需要认证
                .and()
                .httpBasic(); // 这里可以根据您的需求选择认证方式
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://localhost:8080")); // 允许的前端服务器地址
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

