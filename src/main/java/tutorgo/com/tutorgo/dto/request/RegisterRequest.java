package tutorgo.com.tutorgo.dto.request;

import jakarta.validation.constraints.*;
import org.springframework.boot.context.properties.bind.Name;
import tutorgo.com.tutorgo.model.enums.AccountRoles;

public record RegisterRequest(
        @Email String email,
        @Name("surename") @NotBlank String surename,
        @Name("name") @NotBlank String name,
        @NotBlank String university,
        @NotBlank String password,
        @NotNull AccountRoles role,
        @NotBlank(message = "Phone number cannot be blank / El número de celular no puede estar vacío")
        @Size(min = 9, max = 15, message = "Phone number must be between 9 and 15 digits / El número de celular debe tener entre 9 y 15 dígitos")
        @Pattern(regexp = "^\\+?[0-9]+$", message = "Phone number must contain only digits and optionally start with '+' / El número de celular debe contener solo dígitos y opcionalmente empezar con '+'")
        String phoneNumber
) {}
