package com.example.musicat.security;

import com.example.musicat.domain.member.MemberVO;
import lombok.extern.java.Log;
import org.apache.catalina.session.StandardSessionFacade;
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
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
                        MemberVO member = ((MemberAccount) authentication.getPrincipal()).getMemberVo();
                        log.info("principal : " + member.toString());

//                        HttpSession session = request.getSession();
//                        session.setAttribute("loginUser", member);
//                        log.info("인증 성공 - no " + member.getNo() + " email " + member.getEmail() + " grade " +  member.getGrade() + " gradeNo " +  member.getGradeNo() + " pwd " +  member.getPassword());

                        log.info("인증성공 onAuthenticationSuccess");

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
                        log.info("email : " + request.getParameter("email"));

                        //로그인 실패 예외 발생 시 처리
                        //log.info("exception(로그인실패) : " + exception.getMessage());

                        if (exception instanceof UsernameNotFoundException) { //DB에 일치하는 email이 없는 경우
                            request.setAttribute("loginFailMessage", "아이디 또는 비밀번asdfasdfsf호를 잘못 입력하였습니다.");
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
                //.deleteCookies("remember-me", "JSESSIONID") //자동로그인 쿠키, Tomcat이 발금한 세션 유지 쿠키 삭제 (순서 중요) 로그아웃 후 쿠키 삭제 (remeberme 사용 했을 때 필요)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/musicatlogin"); //로그아웃 후 이동할 페이지


        http
                .sessionManagement() //세션 관리
                .sessionFixation().changeSessionId() //세션 고정 보호(뭔말임) 세션 조작을 통한 보안 공격 방지를 위해, 인증이 필요할 때마다 새로운 세션을 만들어 쿠키 조작을 방지 (security가 기본으로 제공해주기 때문에 별도로 설정해줄 필요 없음)
                .maximumSessions(3) //최대 세션 개수
                .expiredUrl("/expiredUrl") //session 만료 시 이동 페이지
                .maxSessionsPreventsLogin(true); //false : 이전에 로그인한 세션 만료, true : 나중에 로그인 시도하는 세션 생성 불가(로그인 불가)

        //이거 에러 메세지 처리 필요
                //.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //스프링 시큐리티가 session을 생성하지도 않고, 존재해도 사용하지 않음 (JWT와 같이 session을 사용하지 않을 경우에 적용)


        //RememberMeAuthenticationFilter가 작동하는 조건
        //1. authentication(인증이 성공한 사용자의 정보를 담은 인증객체)이 null인 경우(security context안에 authentication이 존재하지 않는 경우(즉, session이 끊겼거나 만료된 경우)),
        //2. form 인증 당시 remember Me 기능을 활성화하여 rememberMe 쿠키를 받은 경우에 작동
        //쿠키에 JSESSIONID와 remember-me 토큰이 저장됨. remember-me 체크하지 않을 경우 JSESSIONID삭제하면 다시 로그인해아하나, remember-me 쿠키를 사용할 경우 JSESSIONID 삭제해도 재인증 필요x
        http
                .rememberMe()
                .rememberMeParameter("remember-me") //form에서 rememberMe 기능 사용 여부를 체크할 때 받을 파라미터명과 동일해야 함. 기본 파라미터명은 remember-me
                //.key("uniqeAndSecretRememberMe")
                .tokenValiditySeconds(3600) //rememberMe 토큰 만료 기간. default 14일
                .alwaysRemember(false) //remember Me 기능이 활성화되지 않아도 항상 실행 (false로 하는 것이 좋음 근데 예시는 왜 true)
                .userDetailsService(userDetailsService); //rememberMe 인증 시 인증 계정 조회를 위해 필요


        log.info("SecurityConfig 순회 완--------------------");

    }
}
