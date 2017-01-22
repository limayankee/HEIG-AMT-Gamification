package ch.heigvd;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;



@Configuration
public class SwaggerConfig {

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("GameCat")
                .description("The cat's choice for a gamification platform")
                .license("All the money in your pocket")
                .version("0.0.1")
                .contact(new Contact("", "", ""))
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ch.heigvd.api"))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo());
    }
}