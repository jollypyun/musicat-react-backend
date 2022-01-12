package com.example.musicat.security;

import com.example.musicat.domain.member.MemberVO;
import com.example.musicat.websocket.manager.NotifyManager;
import com.example.musicat.websocket.manager.StompHandler;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.RequestDispatcher;
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

    @Autowired
    private NotifyManager notifyManager;

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

    //필요 없는 인증처리 방지를 위한 정적파일 ignoring 처리 : ignoring 처리하지 않으면 permitAll에 포함되므로 static에 있는 파일들도 인증처리를 거치게 됨
    //정적파일 뿐만 아니라 인증이 필요 없는 url은 ignoring 처리 해주는 것이 좋음
    @Override
    public void configure(WebSecurity web) throws Exception {
        //web.ignoring().antMatchers("/static/**");
        //web.ignoring().mvcMatchers("/favicon.ico");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable();

        http    //DB resource 테이블 만들어서 처리하는 게 편할 거라고 하심(url 변경 되어도 따로 처리 안 해줘도 돼서)
                //강사님께서 코드 짜보고 계시고 완성되면 주시겠다고...
                .authorizeRequests()
                //인증된 사용자이면 접근 가능한 페이지
                .antMatchers("/user/**", "/ChangePwd/**", "/logout", "/articles/insert").authenticated() //
                //매니저 + root(admin) 부터 접근 가능한 페이지
                .antMatchers("/manager/**", "/members/**", "/boardManager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')" )
                //root(admin)만 접근 개능한 페이지
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                //그 외 요청은 모두 허용 ex) /main, /musicatlogin 등
                .anyRequest().permitAll();


        http
                .formLogin() //로그인 페이지 설정
                .loginPage("/musicatlogin") //권한 없는 경우 로그인 페이지로 이동
                .loginProcessingUrl("/login")
                .usernameParameter("email") //view에서 들어오는 아이디 파라미터명 명시
                .passwordParameter("password") //view에서 들어오는 비밀번호 파라미터명 명시
                .successHandler(new AuthenticationSuccessHandler() { //로그인 성공
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        //인증 성공 시 인증 결과를 담은 인증 객체를 파라미터로 받음 (인증 요청하지 않은 사용자의 정보는 HomController(/main)에서 처리해줌
                        MemberVO member = (MemberVO) authentication.getPrincipal();
                        log.info("principal : " + member.toString());

                        // 예나 - notify 임시 id set
                        member.setNotifyId(member.getNo() + member.getEmail());
                        notifyManager.addToNotifyList(member.getNo(), member.getNotifyId());
//                        HttpSession session = request.getSession();
//                        session.setAttribute("loginUser", member);
//                        log.info("인증 성공 - no " + member.getNo() + " email " + member.getEmail() + " grade " +  member.getGrade() + " gradeNo " +  member.getGradeNo() + " pwd " +  member.getPassword());

                        //사용자 요청페이지 저장
                        RequestCache requestCache = new HttpSessionRequestCache();
                        SavedRequest savedRequest = requestCache.getRequest(request, response);

                        if(savedRequest == null) { //요청페이지가 없으면 보낼 url
                            log.info("별도 요청 페이지 없음(main에서 로그인)");
                            response.sendRedirect("/main");
                            
                        } else { //요청 페이지가 있으면 보낼 url
                            log.info("요청페이지 savedRequest.getRedirectUrl() : " + savedRequest.getRedirectUrl());
                            response.sendRedirect(savedRequest.getRedirectUrl());
                        }

                    }
                })
                .failureHandler(new AuthenticationFailureHandler() { //로그인 실패
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        //이전에 입력한 이메일
                        //log.info("email : " + request.getParameter("email"));

                        //로그인 실패 예외 발생 시 처리
                        //log.info("exception(로그인실패) : " + exception.getMessage());

                        if (exception instanceof UsernameNotFoundException) { //DB에 일치하는 email이 없는 경우
                            request.setAttribute("loginFailMessage", "아이디 또는 비밀번호를 잘못 입력하였습니다.");
                            log.info(request.getAttribute("loginFailMessage").toString());
                        } else if (exception instanceof BadCredentialsException) { //비밀번호가 틀린 경우
                            request.setAttribute("loginFailMessage", "아이디 또는 비밀번호를 잘못 입력하였습니다.");
                            log.info(request.getAttribute("loginFailMessage").toString());
                        } else if (exception instanceof DisabledException) { //Ban 처리된 회원인 경우
                            request.setAttribute("loginFailMessage", "활동정지 된 계정입니다.");
                            log.info(request.getAttribute("loginFailMessage").toString());
                        } else if (exception instanceof AccountExpiredException) { //isMember 1인 경우
                            request.setAttribute("loginFailMessage", "탈퇴처리 된 계정입니다.");
                            log.info(request.getAttribute("loginFailMessage").toString());
                        }

                        RequestDispatcher dispatcher = request.getRequestDispatcher("/musicatlogin");
                        dispatcher.forward(request, response);
                    }
                });
        http
                .exceptionHandling().accessDeniedPage("/accessDenideGrade"); //403 에러 뜨면 이동할 페이지
        
        http
                .logout()
                .logoutUrl("/logout") //로그아웃 처리 url
                .invalidateHttpSession(true) //세션비우기
                //.deleteCookies("JESSIONID", "remember-me") //로그아웃 후 쿠키 삭제 (remeberme 사용 했을 때 필요)
                .logoutSuccessUrl("/musicatlogin"); //로그아웃 후 이동할 페이지

        log.info("SecurityConfig 순회 완--------------------");

    }
}
