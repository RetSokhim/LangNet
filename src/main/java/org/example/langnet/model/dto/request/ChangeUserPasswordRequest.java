package org.example.langnet.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserPasswordRequest {
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must be 8 character long and mix between number and character also match with confirm password")
    @NotBlank(message = "Password cannot be blank")
    private String oldPassword;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must be 8 character long and mix between number and character also match with confirm password")
    @NotBlank(message = "Password cannot be blank")
    private String newPassword;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must be 8 character long and mix between number and character also match with confirm password")
    @NotBlank(message = "Password cannot be blank")
    private String confirmPassword;
}
