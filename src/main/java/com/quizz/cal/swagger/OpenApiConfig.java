package com.quizz.cal.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Bernardo",
                        email = "bernardogomesrib@gmail.com",
                        url = "https://naotenhosite.com/"
                ),
                description = "OpenApi documentation for Spring Security",
                title = "OpenApi specification for Spring Security",
                version = "1.0",
                license = @License(
                        name = "Licence name",
                        url = "https://some-url.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080/"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "https://naoexiste.com/"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                // clientCredentials =
                // @OAuthFlow(
                //         tokenUrl =  "http://localhost:9080/realms/quiz/protocol/openid-connect/token",
                //         authorizationUrl = "http://localhost:9080/realms/quiz/protocol/openid-connect/auth"
                // )
                // ,
                // password = @OAuthFlow(
                //         tokenUrl = "http://localhost:9080/realms/quiz/protocol/openid-connect/token",
                //         authorizationUrl = "http://localhost:9080/realms/quiz/protocol/openid-connect/auth"
                // )
                // ,
                // authorizationCode = @OAuthFlow(
                //         tokenUrl = "http://localhost:9080/realms/quiz/protocol/openid-connect/token",
                //         authorizationUrl = "http://localhost:9080/realms/quiz/protocol/openid-connect/auth"
                // )
                // ,

                implicit = @OAuthFlow(
                        authorizationUrl = "http://localhost:9080/realms/quiz/protocol/openid-connect/auth",
                        tokenUrl = "http://localhost:9080/realms/quiz/protocol/openid-connect/token"
                )
        ),
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}