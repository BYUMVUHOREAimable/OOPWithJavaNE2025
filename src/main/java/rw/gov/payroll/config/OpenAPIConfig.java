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
                        .description("API documentation for the Payroll Management System\n\n" +
                                "## Authentication\n" +
                                "Most endpoints require authentication. Follow these steps to authenticate:\n" +
                                "1. Use the `/auth/login` endpoint to get a JWT token\n" +
                                "2. Click the 'Authorize' button at the top of this page\n" +
                                "3. Enter your JWT token with the Bearer prefix: `Bearer your_token_here`\n" +
                                "4. Click 'Authorize' and close the dialog\n" +
                                "5. Now you can access all secured endpoints\n\n" +
                                "Only `/auth/login` and `/auth/register` endpoints can be accessed without authentication.")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                // Add global security requirement for authenticated endpoints
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(SecurityConstants.HEADER_STRING)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter JWT token with Bearer prefix: " + SecurityConstants.TOKEN_PREFIX + "YOUR_JWT_TOKEN")));
    }
}
