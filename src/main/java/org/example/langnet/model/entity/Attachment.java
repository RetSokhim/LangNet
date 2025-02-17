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
public class Attachment {
    private UUID attachmentId;
    private Project project;
    private String baseLanguage;
    private Language language;
    private AppUser postBy;
    private List<Feedback> feedback;
    private String attachmentName;
    private String data;
    private List<Hint> hints;
    private Status status;
    private LocalDateTime postedDate;
    private LocalDateTime expireDate;

}
