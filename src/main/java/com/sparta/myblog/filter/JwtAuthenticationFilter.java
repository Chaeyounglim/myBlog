package com.sparta.myblog.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.dto.UserRequestDto;
import com.sparta.myblog.entity.UserRoleEnum;
import com.sparta.myblog.jwt.JwtUtil;
import com.sparta.myblog.security.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "JwtAuthenticationFilter : 로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {   
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals(HttpMethod.POST.name()) ) {
            // 해당 url 로 들어온 요청의 Method 가 POST 가 아니라면
            try {
                responseResult(response,HttpStatus.BAD_REQUEST,"Not Supported HTTP Method");
                return null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            UserRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), UserRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        log.info("successfulAuthentication() : 로그인 성공 및 사용자 정보 가져오기");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(username, role);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        // 응답 상태 코드와 메시지 설정
        responseResult(response, HttpStatus.OK, "로그인 성공");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.error("로그인 실패");
        responseResult(response, HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다.");
    }

    // Client 에 반환할 msg, status 세팅 메서드
    private void responseResult(HttpServletResponse res, HttpStatus status, String message) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        RestApiResponseDto dto = new RestApiResponseDto(status.value(), message);
        ObjectMapper objectMapper = new ObjectMapper();
        res.getWriter().write(objectMapper.writeValueAsString(dto));
    }

}
