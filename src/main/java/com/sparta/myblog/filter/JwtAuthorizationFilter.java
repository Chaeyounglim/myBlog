package com.sparta.myblog.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.jwt.JwtUtil;
import com.sparta.myblog.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JwtAuthorizationFilter : JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        log.info("doFilterInternal() : ");
        // 헤더에서 substring 해서 토큰 값 가져오기
        String tokenValue = jwtUtil.getJwtFromHeader(req);

        if (StringUtils.hasText(tokenValue)) { // 토큰이 있을 경우
            if (!jwtUtil.validateToken(tokenValue)) { // 토큰 검증 실패 시
                responseResult(res,HttpStatus.BAD_REQUEST,"토큰이 유효하지 않습니다.");
                return;
            }
            try {
                Claims info = jwtUtil.getUserInfoFromToken(tokenValue,res); // 토큰으로 사용자 정보 가져오기
                setAuthentication(info.getSubject());
            } catch (Exception e) { // 만료되지 않은 토큰에 대한 유저 정보가 DB에 없을 경우 인가?
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        log.info("setAuthentication");
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        log.info("createAuthentication");
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
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
