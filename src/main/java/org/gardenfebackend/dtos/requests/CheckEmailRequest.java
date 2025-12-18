package org.gardenfebackend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckEmailRequest {

    @NotBlank
    @Email(message = "Введите корректный email")
    private String email;
}


