package org.example.langnet.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDetailRequest {

    @NotBlank(message = "Username cannot be blank")
    private String username;
    private String firstName;
    private String lastName;
    @Pattern(regexp = "(?i)^(female|male)$", message = "Gender must be Female or Male")
    private String gender;
    private LocalDate birthDate;
}
