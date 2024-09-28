package com.quizz.cal.swagger;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
@OpenAPIDefinition(info = @Info(title = "Quiz", version = "1", description = "API desenvolvida para desenvolvimento de conhecimento"),servers = {
        @Server(
                url = "http://179.73.180.94:4200/api/",
                description = "Remote server via Angular proxy"
        ),
        @Server(
        url = "http://localhost:8080",
        description = "Local server"
        ),
        @Server(
                url = "http://localhost:4200/api/",
                description = "Local server via Angular proxy"
        ),
        @Server(
                url = "http://179.73.180.94:8080",
                description = "Remote server"
        ),
},
security = {
        @SecurityRequirement(
                name = "bearerAuth"
        ),
        @SecurityRequirement(
                name = "bearerAuth2"
        )
})
// @OpenAPIDefinition(
//         info = @Info(
//                 contact = @Contact(
//                         name = "Bernardo",
//                         email = "bernardogomesrib@gmail.com",
//                         url = "https://naotenhosite.com/"
//                 ),
//                 description = "OpenApi documentation for Spring Security",
//                 title = "OpenApi specification for Spring Security",
//                 version = "1.0",
//                 license = @License(
//                         name = "Licence name",
//                         url = "https://some-url.com"
//                 ),
//                 termsOfService = "Terms of service"
//         ),
        
// )

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
@SecurityScheme(
        name = "bearerAuth2",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                // clientCredentials =
                // @OAuthFlow(
                //         tokenUrl =  "http://179.73.180.94:9080/realms/quiz/protocol/openid-connect/token",
                //         authorizationUrl = "http://179.73.180.94:9080/realms/quiz/protocol/openid-connect/auth"
                // )
                // ,
                // password = @OAuthFlow(
                //         tokenUrl = "http://179.73.180.94:9080/realms/quiz/protocol/openid-connect/token",
                //         authorizationUrl = "http://179.73.180.94:9080/realms/quiz/protocol/openid-connect/auth"
                // )
                // ,
                // authorizationCode = @OAuthFlow(
                //         tokenUrl = "http://179.73.180.94:9080/realms/quiz/protocol/openid-connect/token",
                //         authorizationUrl = "http://179.73.180.94:9080/realms/quiz/protocol/openid-connect/auth"
                // )
                // ,

                implicit = @OAuthFlow(
                        authorizationUrl = "http://179.73.180.94:9080/realms/quiz/protocol/openid-connect/auth",
                        tokenUrl = "http://179.73.180.94:9080/realms/quiz/protocol/openid-connect/token"
                )

        ),
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfig {
}