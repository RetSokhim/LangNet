package org.example.langnet.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMemberIntoProjectRequest {
    @NotNull(message = "Project ID cannot be null")
    private UUID projectId;
    @NotNull(message = "User ID cannot be null")
    private UUID userId;
    @NotNull(message = "Role ID cannot be null")
    private UUID roleId;
}