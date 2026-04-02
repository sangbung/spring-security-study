package com.example.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecuritiyConfig{

    //해당 메서드의 리턴되는 오브젝트를 IoC로 등록한다
    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())


            //권한
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/user/**").authenticated()   // /user/** 접근시 인증 필요 인증만 되면 들어갈수 있는 주소
            .requestMatchers("/manager/**").hasAnyRole("ADMIN","MANAGER")
            .requestMatchers("/admin/**").hasAnyRole("ADMIN")
            .anyRequest().permitAll()
        )
        .formLogin(form -> form
                .loginPage("/loginForm")// 커스텀 로그인 페이지
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")       // 로그인 성공 후 이동
                //.usernameParameter("username")
                .permitAll()

        )
        .exceptionHandling(ex -> ex
                .accessDeniedPage("/loginForm") // 권한 없는 사용자 접근 시 이동
        );



        return http.build();
    }
}
