package org.example.langnet.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCreationRequest {
    @NotBlank(message = "Project's name cannot be blank")
    private String projectName;
}
