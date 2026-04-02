package com.example.security1.config.auth;

//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
//로그인을 진행이 완료가 되면 session을 만들어 준다 (Security contextHolder)
//오브젝트 타입 => Authentication 타입 객체
//Authentication 안에 user정보가 있어야 한다.
//user 오브젝트 타입 -> userdetails 타입 객체

//security session -> authentication -> userDetails  user 객체를 접근 가능


import com.example.security1.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetail implements UserDetails {

    private User user;

    public PrincipalDetail(User user) {
        this.user = user;
    }

    //해당 user의 권한 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority (){
            @Override
            public String getAuthority(){
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //사이트에서 1년 동안 로그인 안하면, 휴면 계정으로 변경할떄
    @Override
    public boolean isEnabled() {
        return true;
    }
}
