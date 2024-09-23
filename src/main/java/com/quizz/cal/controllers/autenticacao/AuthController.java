package com.quizz.cal.controllers.autenticacao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Value("${keycloak.client}")
    private String clientId;
    @Value("${keycloak.granttype}")
    private String grantType;
    @Value("${keycloak.username}")
    private String username;
    @Value("${keycloak.password}")
    private String password;
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @PostMapping()
    public ResponseEntity<String> postLogin(@RequestBody LoginRequest loginRequest) {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate rt = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String,String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("grant_type", grantType);
        formData.add("username",loginRequest.getUsername());
        formData.add("password",loginRequest.getPassword());
        HttpEntity<MultiValueMap<String,String>>entity = new HttpEntity<>(formData,headers);
        var result = rt.postForEntity(issuerUri+"/protocol/openid-connect/token", entity, String.class);
        return result;
    }
    
}
