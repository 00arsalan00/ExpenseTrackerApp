package com.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {
	private static final String SECURITY_SCHEME_NAME = "bearerAuth";
	
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				//Enable JWT Authorize Button
				.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
				.components(new Components()
						.addSecuritySchemes(SECURITY_SCHEME_NAME, 
								new SecurityScheme()
								.name(SECURITY_SCHEME_NAME)
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")
								)
						)
				//Api info section
				.info(new Info()
						.title("Expense Tracker App")
						.version("1.0.0")
						.description("""
                                REST API for Expense Management System.
                                
                                Features:
                                - JWT Authentication
                                - Expense CRUD
                                - Monthly & Category Reports
                                - Secure Stateless Architecture
                                """)
						.contact(new Contact()
								.name("Mohammad Arsalan Rayeen")
								.email("2k23.psitaiml2311749@gmail.com"))
						.license(new License()
                                .name("Private Backend Project")));
								
						
		
	}
	

}
