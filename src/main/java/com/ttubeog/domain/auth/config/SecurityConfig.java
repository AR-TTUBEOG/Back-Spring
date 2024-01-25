package com.ttubeog.domain.auth.config;

import com.ttubeog.domain.auth.exception.ExceptionHandlerFilter;
import com.ttubeog.domain.auth.service.JwtTokenService;
import com.ttubeog.domain.auth.filter.JwtFilter;
import com.ttubeog.domain.member.application.MemberService;
import com.ttubeog.domain.member.domain.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final JwtTokenService jwtTokenService;
    private final MemberService memberService;

    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration authenticationConfiguration
            ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.cors(withDefaults())
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/auth/login/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs", "/swagger-resources/**").permitAll()
                        .requestMatchers("/user/**").hasAuthority(MemberRole.USER.getRole())
                        .anyRequest().authenticated())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable())    // 기본 로그인 폼 미사용
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable())    // 기본 http 미사용
                .addFilterBefore(new JwtFilter(jwtTokenService, memberService), UsernamePasswordAuthenticationFilter.class) // JWT 필터 추가
                .addFilterBefore(new ExceptionHandlerFilter(), JwtFilter.class) // Security Filter 에서 CustomException 사용하기 위해 추가
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web ->
                web.ignoring()
                        .requestMatchers("/auth/login/**")
                        .requestMatchers("/swagger-ui/**",
                                "/v2/api-docs",
                                "/swagger-resources/**",
                                "/h2-console/**",
                                "/error",
                                "/v3/api-docs/**");
    }
}
