package org.example.langnet.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private UUID projectId;
    private String projectName;
    private String status;
    private Boolean active;
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
    private LocalDateTime removedDate;
    private List<AppUser> user;
    private List<Attachment> attachments;
    private List<Notification> notifications;
}
