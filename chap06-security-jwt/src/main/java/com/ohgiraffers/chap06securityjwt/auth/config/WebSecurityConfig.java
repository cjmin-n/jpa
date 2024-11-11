package com.ohgiraffers.chap06securityjwt.auth.config;

import com.ohgiraffers.chap06securityjwt.auth.filter.CustomAuthenticationFilter;
import com.ohgiraffers.chap06securityjwt.auth.handler.CustomAuthenticationProvider;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 컨트롤러 단으로 설정 (컨트롤러에서 특정권한 있는지 확인) // Deprecated
public class WebSecurityConfig implements {
    // 한군데에 모아서 Bean 으로 등록
    // 용이하게 관리하기 위해

    /**
     * 1. 정적 자원에 대한 인증된 사용자의 접근을 설정하는 메소드
     * @return WebSecurityCustomizer
     * */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * 2. 비밀번호 암호화 인코더
     * @return BCryptPasswordEncoder
     * */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 3. 사용자의 아이디와 패스워드를 DB 검증하는 핸들러
     * @return CustomAuthenticationProvider
     * */
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(){
        return new CustomAuthenticationProvider();
    }

    /**
     * 4. Authentication 의 인증 메소드를 제공하는 매니저로 Provider 의 인터페이스
     * @return AuthenticationManager
     * */
    @Bean
    public AuthenticationManager authenticationManager(){
        // 시큐리티에서 사용자 인증을 관리하는 핵심 객체
        return new ProviderManager(customAuthenticationProvider());
        // providerManager 는 AuthenticationManager 를 구현한 클래스.
        // 여러 AuthenticationProvider 를 이용해 인증을 처리함.
    }

    /**
     * 5. 사용자의 인증 요청을 가로채서 로그인 로직을 수행하는 필터
     * @return CustomAuthenticationFilter
     * */
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(){
        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(authenticationManager()); // authenticationManager() 가 검증 로직을 수행한다.

    }


    /**
     *
     * */

}
