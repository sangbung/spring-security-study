package com.example.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecuritiyConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())


            //권한
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/user/**").authenticated()   // /user/** 접근시 인증 필요
            .requestMatchers("/manager/**").hasAnyRole("ADMIN","MANAGER")
            .requestMatchers("/admin/**").hasAnyRole("ADMIN")
            .anyRequest().permitAll()
        )
        .formLogin(form -> form
                .loginPage("/login")          // 커스텀 로그인 페이지
                .defaultSuccessUrl("/")       // 로그인 성공 후 이동
                .permitAll()
        )
        .exceptionHandling(ex -> ex
                .accessDeniedPage("/login") // 권한 없는 사용자 접근 시 이동
        );



        return http.build();
    }
}
