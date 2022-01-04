//package com.example.musicat.config;
//
//import java.util.Properties;
//
//import javax.servlet.Filter;
//
//import com.example.musicat.filter.LoginFilter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import com.example.musicat.filter.LoginFilter;
//
//@Configuration
//public class FilterConfig {
//
//	@Bean
//	public FilterRegistrationBean<Filter> loginFilter() {
//		FilterRegistrationBean<Filter> filterbean = new FilterRegistrationBean<Filter>();
//		filterbean.setFilter(new LoginFilter());
//		filterbean.setOrder(1); // 필터 체인할 때 가장 먼저 실행
//		filterbean.addUrlPatterns("/*"); // 모든 url
//
//		return filterbean;
//	}
//
//}
