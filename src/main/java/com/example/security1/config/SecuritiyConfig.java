package com.example.security1.config;

import com.example.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


// 1.코드 받기(인증), 2.엑세스토큰(권한),
// 3.사용자프로필 정보 가져오고. 4-1. 그 정보를 토대로 외원가입을 자동으로 진행
// 4-2. (이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> (집주소), 백화점몰 ->(vip등급, 일반등급)

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록된
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
public class SecuritiyConfig{

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;



    //해당 메서드의 리턴되는 오브젝트를 IoC로 등록한다


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())


            //권한
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/user/**").authenticated()   // /user/** 접근시 인증 필요 인증만 되면 들어갈수 있는 주
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
        .oauth2Login(oauth ->oauth.
                loginPage("/loginForm")
                .userInfoEndpoint(user -> user.userService(principalOauth2UserService))
        ) // 구글 로그인 이 완료된 뒤의 후처리가 필요하다
                // tip.코드x, (엑세스토큰 + 사용자프로필 정보 o)

        .exceptionHandling(ex -> ex.accessDeniedPage("/loginForm") // 권한 없는 사용자 접근 시 이동
        );



        return http.build();
    }
}
