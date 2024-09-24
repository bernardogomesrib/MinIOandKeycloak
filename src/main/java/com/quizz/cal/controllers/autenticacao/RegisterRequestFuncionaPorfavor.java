package com.quizz.cal.controllers.autenticacao;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class RegisterRequestFuncionaPorfavor {
    
        @Email
        private String email;
        private String firstName;
        private String lastName;
        private String password;

        public  RegisterRequest toRegisterRequest(){
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername(this.email);
            registerRequest.setEmail(this.email);
            registerRequest.setFirstName(this.firstName);
            registerRequest.setLastName(this.lastName);
            registerRequest.setPassword(this.password);
            return registerRequest;
        }
}
