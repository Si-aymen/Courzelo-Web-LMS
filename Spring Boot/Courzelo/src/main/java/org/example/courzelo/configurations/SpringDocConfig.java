package org.example.courzelo.configurations;
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

    public Info infoAPI() {
        return new Info().title("IPACTproject")
                .description("étude de cas")
                .contact(contactAPI())
                .version("1");
    }

    public Contact contactAPI() {
        return new Contact().name("Equipe ASI II");
    }
}