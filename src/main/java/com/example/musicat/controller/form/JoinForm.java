package com.example.musicat.controller.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinForm {
    
    private String email;
    private String password;
    private String confirmPassword;
    private String nickname;
}
