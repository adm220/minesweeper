package com.challenge.deviget.mines.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.bind.annotation.RequestAttribute;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.challenge.deviget.mines.controller"))
                .build()
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .useDefaultResponseMessages(false)
                .ignoredParameterTypes(RequestAttribute.class);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "MineSweeper",
                "Deviget Challenge",
                "API V1",
                null,
                null,
                null, null, Collections.emptyList());
    }
    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }
    private ApiKey apiKey() {
        return new ApiKey("ApiKeyAuth", "x-api-key", "header");
    }

    private List<SecurityReference> defaultAuth() {
        return Arrays.asList(new SecurityReference("ApiKeyAuth",  new AuthorizationScope[0]));
    }
}

