package org.gardenfebackend.dtos.requests;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    @Email(message = "Введите корректный email")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-zа-я])(?=.*[A-ZА-Я]).{8,}$",
            message = "Пароль должен содержать минимум 8 символов, " +
                    "строчную и заглавную букву, и хотя бы одну цифру (только буквы/цифры)"
    )
    private String password;

    @NotBlank(message = "Подтверждение пароля обязательно")
    private String confirmPassword;

    @AssertTrue(message = "Пароли не совпадают")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private boolean isPasswordsMatch() {
        return password != null && password.equals(confirmPassword);
    }
}
