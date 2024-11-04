package com.modsen.software.ratings.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Ratings service Api",
                description = "Ratings service", version = "1.0.0",
                contact = @Contact(
                        name = "Aleksej",
                        email = "example@mail.com"
                )
        )
)
public class OpenApiConfig {

}
