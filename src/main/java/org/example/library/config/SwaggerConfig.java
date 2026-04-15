package org.example.library.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/test", "/swagger-ui/index.html");
    }
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components( new Components()
                        .addSecuritySchemes( "Bearer Token", apiKeySecuritySchema() ) )
                .info( new Info().title( "DASTANCHIIK" ).description( "Written by: dastanchiik" ) )
                .security( List.of( new SecurityRequirement().addList( "Bearer Token" ) ) );
    }

    private SecurityScheme apiKeySecuritySchema() {
        return new SecurityScheme()
                .name( "Authorization" )
                .description( "put your jwt token here!" )
                .in( SecurityScheme.In.HEADER )
                .type( SecurityScheme.Type.HTTP )
                .scheme( "Bearer" );
    }

}
