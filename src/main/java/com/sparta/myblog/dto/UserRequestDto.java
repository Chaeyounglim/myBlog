package com.sparta.myblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    // login and sing up Request DTO

    @Pattern(regexp = "^[a-z0-9]{4,10}$" , message = "정규식에 맞지 않습니다.")
    @NotBlank
    private String username;


    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$", message = "정규식에 맞지 않습니다.")
    @NotBlank
    private String password;
}
