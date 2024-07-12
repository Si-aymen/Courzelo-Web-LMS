package org.example.courzelo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(infoAPI());
    }

    public io.swagger.v3.oas.models.info.Info infoAPI() {
        return new Info().title("Courzelo API")
                .description("étude de cas")
                .contact(contactAPI())
                .version("1");
    }

    public io.swagger.v3.oas.models.info.Contact contactAPI() {
        return new Contact().name("Equipe ASI II");
    }

}
