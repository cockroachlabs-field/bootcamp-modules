package io.cockroachdb.bootcamp;

import java.time.format.DateTimeFormatter;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;

@Configuration
@EnableKafka
public class Chapter4Configuration {
    public static LocalDateTimeSerializer ISO_DATETIME_SERIALIZER
            = new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    @Bean
    public JsonMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder.enable(SerializationFeature.INDENT_OUTPUT);
    }

//    @Bean
//    @Primary
//    public ObjectMapper objectMapper() {
//        return new ObjectMapper()
//                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
//                .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // strict
//                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
//                .enable(SerializationFeature.INDENT_OUTPUT)
//                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
//                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
//    }

}
