package com.sparta.myblog.service;

import com.sparta.myblog.dto.UserRequestDto;
import com.sparta.myblog.entity.User;
import com.sparta.myblog.entity.UserRoleEnum;
import com.sparta.myblog.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 메서드
    public void signUp(UserRequestDto requestDto, HttpServletResponse res) throws IOException {
        // 1. 요청 받은 회원가입 정보 변수에 저장 및 비밀번호 암호화
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 2. 회원 중복 확인
        // 2-1. DB에 해당 username 에 대한 row 가 있다면 checkUsername 변수에 저장.
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if( checkUsername.isPresent() ) { // 2-2. 중복된 회원이 있을 경우
            responseResult(res,400,"중복된 username 입니다.");
            log.error("중복된 사용자가 존재합니다.");
        }else { // 2-2. 중복된 회원이 없을 경우 가입 시도
            // 3. 사용자 ROLE 부여
            UserRoleEnum role = UserRoleEnum.USER;

            // 4. 해당 정보를 생성자 메서드로 User 객체 생성 후 DB 에 저장
            User user = new User(username, password, role);
            userRepository.save(user);

            // 5. Client 에 반환할 데이터 및 log 출력
            responseResult(res, 200, "회원가입 성공");
            log.info("회원가입에 성공하였습니다.");
        }
    }

    // Client 에 HttpServletResponse 를 통해 반환할 msg, status 세팅 메서드
    private void responseResult(HttpServletResponse response, int statusCode, String message) throws IOException {
        String jsonResponse = "{\"status\": " + statusCode + ", \"message\": \"" + message + "\"}";

        // Content-Type 및 문자 인코딩 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // PrintWriter 를 사용하여 응답 데이터 전송
        PrintWriter writer = response.getWriter();
        writer.write(jsonResponse);
        writer.flush();
    }


}
