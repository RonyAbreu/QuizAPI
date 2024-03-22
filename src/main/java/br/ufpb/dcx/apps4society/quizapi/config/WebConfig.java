package br.ufpb.dcx.apps4society.quizapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Value("${cors.origins}")
    private String origins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String [] allowedOrigins = origins.split(",");
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("GET", "PUT", "DELETE", "POST", "PATCH")
                .allowedOrigins(allowedOrigins)
                .allowCredentials(true);
    }
}
