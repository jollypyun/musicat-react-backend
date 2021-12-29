package com.example.musicat.config;

import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Log
@Configuration
@EnableWebSecurity //스프링 시큐리티 필터(SecurityConfig)를 스프링 필터 체인에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    //비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        log.info("BCryptPassEncoder--------------------------");
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/user").authenticated() //인증된 사용자 접근 허용
                .antMatchers("manager").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ROOT')" ) //매니저 권한 부여(manager, root)
                .antMatchers("/admin").access("hasRole('ROLE_ROOT')") //root 권한 부여(root)
                .anyRequest().permitAll(); //그 외 요청은 모두 허용
        log.info("SecurityConfig 1--------------------");
        http
                .formLogin()
                .loginPage("petopialogin")
                .loginProcessingUrl("/login");
        log.info("SecurityConfig 2--------------------");
    }
}
