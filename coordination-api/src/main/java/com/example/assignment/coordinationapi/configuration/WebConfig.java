package com.example.assignment.coordinationapi.configuration;

import com.example.assignment.coordinationapi.application.model.converter.StringToClothTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToClothTypeConverter());
    }
}
