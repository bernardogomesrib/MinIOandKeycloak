package com.quizz.cal.controllers.autenticacao;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;

}
