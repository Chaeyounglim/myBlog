package com.sparta.myblog.config;


import com.sparta.myblog.filter.JwtAuthenticationFilter;
import com.sparta.myblog.filter.JwtAuthorizationFilter;
import com.sparta.myblog.jwt.JwtUtil;
import com.sparta.myblog.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf(AbstractHttpConfigurer::disable);
        //http.httpBasic(AbstractHttpConfigurer::disable);

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );


        // 아래 접근 제어는 Filter 를 거쳐서 권한을 부여받고
        // 그 권한을 Http.authorizeRequest() 코드에서
        // 해당 url 에 따른 권한을 매칭시켜 확인 하는 것이다.
        // 그러므로 FilterChain 을 타고 난 후에 requestMatchers 를 해주는 것이다.
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/").permitAll() // 메인 페이지 요청 허가
                        .requestMatchers(HttpMethod.POST,"/api/user/**").permitAll() // 로그인, 회원가입 누구나 가능.
                        .requestMatchers( HttpMethod.GET,"/api/posts/**" ).permitAll() // '/api/posts/'로 시작하는 요청 모두 접근 허가 (전체,선택 게시글 조회)
                        .requestMatchers("/api/post").permitAll() // 게시글 작성, 수정, 삭제, 게시글 좋아요 url 허용
                        .requestMatchers("/api/comment").permitAll() // 댓글 작성, 수정, 삭제, 댓글 좋아요 url 허용
                //.requestMatchers("/api/post/**").authenticated() // '/api/posts/'로 시작하는 요청은 인증된 사용자 모두 접근 허가 (게시글,댓글 수정삭제)
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        http.formLogin(AbstractHttpConfigurer::disable);

        // 필터 관리
        // 순서 보장을 위해 UsernamePasswordAuthenticationFilter 앞에 배치를 시켰습니다.
        // exception filter -> Authorization filter -> Authentication filter
//        http.addFilterBefore(jwtExceptionFilter(), UsernamePasswordAuthenticationFilter.class)
        http.addFilterAfter(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 인가 - 인증 - UsernamePasswordAuthenticationFilter
        return http.build();
    }
}
