package my.projects.lockersystempackagemicroservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI();

        openAPI.components(
                new Components()
                        .addSecuritySchemes("bearer-token",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ));

        openAPI.setInfo(
                new Info()
                        .description("This is the package microservice.")
                        .title("SmartLockerHub Package API")
                        .version("0.0.1")
                        .contact(
                                new Contact()
                                        .name("Viktor")
                                        .email("voltron773@gmail.com")
                        )
        );

        return openAPI;
    }
}
