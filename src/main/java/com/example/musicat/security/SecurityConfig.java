package com.example.musicat.security;

import com.example.musicat.domain.member.MemberVO;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Log
@Configuration
@EnableWebSecurity //스프링 시큐리티 필터(SecurityConfig)를 스프링 필터 체인에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsService userDetailsService;

    //비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAutheticationProvider();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable();

        http
                .authorizeRequests()
                //인증된 사용자이면 접근 가능한 페이지
                .antMatchers("/user/**", "/ChangePwd/**", "/logout").authenticated()
                //매니저 + root(admin) 부터 접근 가능한 페이지
                .antMatchers("/manager/**", "/members/**", "/petopia-manager/daily").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ROOT')" )
                //root(admin)만 접근 개능한 페이지
                .antMatchers("/admin/**", "/grades/**").access("hasRole('ROLE_ROOT')")
                //그 외 요청은 모두 허용 ex) /main등
                .anyRequest().permitAll();

        http
                .formLogin() //로그인 페이지 설정
                .loginPage("/musicatlogin") //권한 없는 경우 로그인 페이지로 이동
                //.defaultSuccessUrl("/") //로그인 성공 후 이동 페이지
                //.failureUrl("/musicatloginfail") //로그인 실패 후 이동 페이지
                .loginProcessingUrl("/login")
                .usernameParameter("email") //view에서 들어오는 아이디 파라미터명 명시
                .passwordParameter("password") //view에서 들어오는 비밀번호 파라미터명 명시
                .successHandler(new AuthenticationSuccessHandler() { //로그인 성공 시 호출됨
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        //인증 성공 시 인증 결과를 담은 인증 객체를 파라미터로 받음
                        MemberVO member = (MemberVO) authentication.getPrincipal();
                        log.info("인증 성공 - session에 들어갈 회원 정보 : " + member);
                        HttpSession session = request.getSession();
                        session.setAttribute("loginUser", member);
                        response.sendRedirect("/main"); //로그인 성공 후 이동 페이지 -> 원래 가려고 했던 페이지 띄워주는 방법 있을걸

                    }
                })
                // 로그인 실패 시
                // 이메일 일치, 비밀번호 불일치 : 회원 이메일은 유지하고 비밀번호만 재 입력할 수 있도록
                .failureHandler(new AuthenticationFailureHandler() { //로그인 실패 시 호출됨
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        log.info("exception(로그인실패) : " + exception.getMessage());

                        response.sendRedirect("/musicatlogin"); //로그인 실패 시 이동 페이지
                    }
                });
        http
                .exceptionHandling().accessDeniedPage("/accessDenied"); //403 에러 뜨면 이동할 페이지
        
        http
                .logout()
                .logoutUrl("/logout") //로그아웃 처리 url
                .invalidateHttpSession(true) //세션비우기
                //.deleteCookies("JESSIONID", "remember-me") //로그아웃 후 쿠키 삭제
                .logoutSuccessUrl("/musicatlogin"); //로그아웃 후 이동할 페이지

        log.info("SecurityConfig 순회 완--------------------");

    }
}
