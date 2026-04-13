package com.example.security1.controller;

import com.example.security1.config.auth.PrincipalDetail;
import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String loginTest(Authentication authentication
    , @AuthenticationPrincipal PrincipalDetail userDetails) // 의존성 주입
    {



        System.out.println("/test/login ================");
        PrincipalDetail principalDetail = (PrincipalDetail) authentication.getPrincipal();
        System.out.println("authentication : " + principalDetail.getUser());

        System.out.println("userDetails : " + userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication
     ,@AuthenticationPrincipal OAuth2User oauth       ) // 의존성 주입
    {



        System.out.println("/test/oauth/login ================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication : " + oAuth2User.getAttributes());
        System.out.println("oauth2User : " + oauth.getAttributes());


        return "OAuth 세션 정보";
    }

    @GetMapping({"","/"})
    public String index(){
        return "index";
    }

    //OAuth 로그인을 해도 principaldetial
    //일반 로그인을 해도 pricipaldetial
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetail principalDetail){
        System.out.println("principalDetail : " + principalDetail.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    //spring security가 채간다 -> securityconfig 등록후 채가지 않는다.
    @GetMapping("/loginForm")
    public String login(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        System.out.println(user);
        user.setRole("ROLE_USER");

        //패스워드 암호화
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        System.out.println("save 전");
        userRepository.save(user); // security로 로그인 할수없다 -> 패스워드가 암호화가 안돼있다
        System.out.println("save 후");
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // 특정 한개만 권한을 걸고 싶으면 사용
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인 정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터 정보";
    }


}
