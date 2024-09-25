package com.quizz.cal.controllers.autenticacao;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Value("${keycloak.clientid}")
    private String clientId;
    @Value("${keycloak.granttype}")
    private String grantType;
    @Value("${keycloak.username}")
    private String loginMaster;
    @Value("${keycloak.password}")
    private String passwordMaster;
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;
    @Value("${keycloak.master.realm}")
    private String realmLinkMaster;
    @Value("${keycloak.master.create.user}")
    private String createUserLinkMaster;
    @Value("${spring.security.oauth2.authorizationserver.endpoint.oidc.logout-uri}")
    private String logoutLinkUsuario;
    @Operation(summary = "EndPoint para fazer login na api")
    @PostMapping("login")
    public ResponseEntity<?> postLogin(@RequestBody LoginRequest loginRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", clientId);
            formData.add("grant_type", grantType);
            formData.add("username", loginRequest.getUsername());
            formData.add("password", loginRequest.getPassword());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(issuerUri+"/protocol/openid-connect/token", request, String.class);

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    @Operation(summary = "EndPoint para criar uma conta na api")
    @PostMapping("create")
    public ResponseEntity<?> createAccount(@RequestBody RegisterRequestFuncionaPorfavor userr) {
        try {
            RegisterRequest user = userr.toRegisterRequest();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", "admin-cli");
            formData.add("grant_type", "password");
            formData.add("username", loginMaster);
            formData.add("password", passwordMaster);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(realmLinkMaster, request, String.class);

            JSONObject jsonObj = new JSONObject(response.getBody());
            String accessToken = jsonObj.getString("access_token");
            String refresh_token = jsonObj.getString("refresh_token");
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(user);

            HttpEntity<String> entity = new HttpEntity<>(json, headers);
        
            restTemplate.postForEntity(createUserLinkMaster, entity, String.class);
            LoginRequest user_temp = new LoginRequest();
            user_temp.setUsername(user.getUsername());
            user_temp.setPassword(user.getCredentials().get(0).getValue());

            logoffAdmin(refresh_token);
            return  ResponseEntity.status(200).body(loginDoCreate(user_temp));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @Operation(summary = "EndPoint para atualizar o token")
    @PostMapping("update")
    public ResponseEntity<?> updateToken(@RequestBody String refresh_token) {
        try{

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", clientId);
            formData.add("refresh_token",refresh_token);
            formData.add("grant_type", "refresh_token");
            HttpEntity<MultiValueMap<String, String>>  request = new HttpEntity<>(formData, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(issuerUri+"/protocol/openid-connect/token", request, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @Operation(summary = "EndPoint para fazer logout")
    @GetMapping("logout")
    public ResponseEntity<?> postLogout(@RequestBody String refresh_token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", clientId);
            formData.add("refresh_token",refresh_token);
            HttpEntity<MultiValueMap<String, String>>  request = new HttpEntity<>(formData, headers);
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForEntity(logoutLinkUsuario, request, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    public void logoffAdmin(String refresh_token){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", "admin-cli");
        formData.add("refresh_token",refresh_token);
        HttpEntity<MultiValueMap<String, String>>  request = new HttpEntity<>(formData, headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(realmLinkMaster.replace("token", "logout"), request, String.class);
    }


    public ResponseEntity<?> loginDoCreate(LoginRequest loginRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", clientId);
            formData.add("grant_type", grantType);
            formData.add("username", loginRequest.getUsername());
            formData.add("password", loginRequest.getPassword());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:9080/realms/quiz/protocol/openid-connect/token", request, String.class);

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
