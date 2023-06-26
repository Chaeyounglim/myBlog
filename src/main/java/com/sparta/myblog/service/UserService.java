package com.sparta.myblog.service;

import com.sparta.myblog.dto.ResponseDto;
import com.sparta.myblog.dto.UserRequestDto;
import com.sparta.myblog.entity.User;
import com.sparta.myblog.entity.UserRoleEnum;
import com.sparta.myblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 메서드
    public ResponseDto signUp(UserRequestDto requestDto) {
        log.info("service");
        ResponseDto responseDto = new ResponseDto();

        String username = requestDto.getUsername();
        log.info("username" + username);
        log.info("pw" + requestDto.getPassword());

        String password = passwordEncoder.encode(requestDto.getPassword());// 암호화

        // 1. 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if( checkUsername.isPresent() ) { // 중복된 회원이 있을 경우
            responseDto.setMsg("중복된 사용자 존재");
            responseDto.setStatus(401);
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 2. 사용자 ROLE 부여
        UserRoleEnum role = UserRoleEnum.USER;

        User user = new User(username,password,role);
        userRepository.save(user); // DB 에 해당 정보 회원가입

        responseDto.setMsg("회원가입 성공");
        responseDto.setStatus(200);
        return responseDto;
    }
}
