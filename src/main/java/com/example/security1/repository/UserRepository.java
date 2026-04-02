package com.example.security1.repository;

import com.example.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


//crud함수를 jpaRepository가 들고있다
//@Repository라는 어노테이션이 없어도 IoC된다. 이유는 JpaRepository를 상속했기 때문
public interface UserRepository extends JpaRepository<User,Integer> {
    //findBy규칙 -> username 문법
    // select * from user where username = 1?;
    public User findByUsername(String username);

    public User findByEmail(String email);
}
