package rw.gov.payroll.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rw.gov.payroll.security.SecurityConstants;

/**
 * Configuration class for OpenAPI/Swagger documentation.
 * This class configures the OpenAPI documentation with JWT authentication support.
 */
@Configuration
public class OpenAPIConfig {

    /**
     * Configures the OpenAPI documentation with JWT authentication.
     * This adds a security scheme for JWT tokens and a global security requirement.
     * 
     * @return the OpenAPI configuration
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Payroll Management System API")
                        .version("1.0")
                        .description("API documentation for the Payroll Management System")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                // Remove global security requirement to make authentication optional
                // .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(SecurityConstants.HEADER_STRING)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter JWT token with Bearer prefix: " + SecurityConstants.TOKEN_PREFIX + "YOUR_JWT_TOKEN")));
    }
}
