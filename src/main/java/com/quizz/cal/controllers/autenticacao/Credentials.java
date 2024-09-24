package com.quizz.cal.controllers.autenticacao;

import lombok.Data;

@Data
public class Credentials {
    private String type = "password";
    private String value;
    private boolean temporary = false;
    private String userLabel = "My password";
    public Credentials(String value) {
        this.value = value;
    }
    public Credentials(){
        this.type = "password";
        this.value = "";
        this.temporary = false;
        this.userLabel = "My password";
    }
}