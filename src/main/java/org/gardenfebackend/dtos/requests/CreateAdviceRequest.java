package org.gardenfebackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateAdviceRequest {

    @NotBlank(message = "Заголовок обязателен")
    private String title;

    @NotNull(message = "Фотография обязательна")
    private MultipartFile photo;

    @NotBlank(message = "Описание обязательно")
    private String description;
}

