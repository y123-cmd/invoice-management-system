package com.imbank.payments.corporate.corporateinvoicesystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI corporateInvoiceSystemOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Development Server");

        Contact contact = new Contact();
        contact.setEmail("bryan@imbank.com");
        contact.setName("I&M Bank Development Team");

        Info info = new Info()
                .title("Corporate Invoice System API")
                .version("1.0.0")
                .contact(contact)
                .description("REST API for managing corporate clients, accounts, and signatories for I&M Bank. " +
                        "This API provides endpoints for creating, updating, and retrieving corporate banking data.");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}