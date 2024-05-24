package org.example.langnet.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRegisterRequest {
    @NotBlank(message = "Firstname cannot be blank")
    private String firstName;

    @NotBlank(message = "Lastname cannot be blank")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Pattern(regexp = "(?i)^(female|male)$", message = "Gender must be Female or Male")
    private String gender;

    @NotNull(message = "BirthDate cannot be null")
    private LocalDate birthDate;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must be 8 character long and mix between number and character also match with confirm password")
    @NotBlank(message = "Password cannot be blank")
    private String password;
}