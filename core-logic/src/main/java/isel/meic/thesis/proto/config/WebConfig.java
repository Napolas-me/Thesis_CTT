// src/main/java/isel/meic/thesis/proto/config/WebConfig.java
package isel.meic.thesis.proto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Marks this class as a Spring configuration class
@EnableWebMvc  // Enables Spring MVC features, including CORS configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Configure CORS for all API endpoints
        registry.addMapping("/**") // Apply CORS to all paths in your application
                .allowedOrigins("http://localhost:3000") // Allow requests from your React frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow these HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow sending of cookies/authentication headers
    }
}
