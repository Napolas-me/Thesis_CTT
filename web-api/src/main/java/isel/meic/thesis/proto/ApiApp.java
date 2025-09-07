package isel.meic.thesis.proto;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class ApiApp {

    public static void main(String[] args) {
        SpringApplication.run(ApiApp.class, args);
    }

    // Define the desired ISO 8601 format for LocalDateTime
    private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            // Register JavaTimeModule to handle Java 8 Date and Time API types
            builder.modules(new JavaTimeModule());

            // Disable writing dates as timestamps (arrays of numbers)
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // Explicitly add serializers/deserializers for LocalDateTime
            // This ensures LocalDateTime is formatted as a string using the defined pattern
            builder.serializerByType(java.time.LocalDateTime.class, new LocalDateTimeSerializer(FORMATTER));
            builder.deserializerByType(java.time.LocalDateTime.class, new LocalDateTimeDeserializer(FORMATTER));

            // Optional: If you want to pretty print JSON in development (for readability)
            // builder.featuresToEnable(SerializationFeature.INDENT_OUTPUT);
        };
    }


}