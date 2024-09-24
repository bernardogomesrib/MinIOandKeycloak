package com.quizz.cal.controllers.autenticacao;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enabled = true;
    private boolean emailVerified = true;
    private List<Credentials> credentials;

    public void setPassword(String password){
        ArrayList<Credentials> temp = new ArrayList<Credentials>();
        Credentials cred = new Credentials(password);
        temp.add(cred);
        this.credentials = temp.subList(0, temp.size());
    }
}
