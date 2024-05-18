package org.example.langnet.model.dto.respond;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.langnet.model.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
    private UUID projectId;
    private String projectName;
    private Status status;
    private Boolean active;
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
    private LocalDateTime removedDate;
    private List<UserInProjectResponse> users;
    private List<Attachment> attachments;
    private List<Notification> notifications;
    private List<Language> languages;
}
