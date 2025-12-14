package org.gardenfebackend.config;

import lombok.RequiredArgsConstructor;
import org.gardenfebackend.config.PlantTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final PlantTypeConverter plantTypeConverter;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Отдаём файлы из папки "uploads" по URL /files/**
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:uploads/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(plantTypeConverter);
    }
}



