package org.example.langnet.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    private UUID feedbackId;
    private AppUser commentBy;
    private String comment;
    private Boolean active;
    private LocalDateTime createdDate;
    private LocalDateTime removedDate;
    private LocalDateTime editDate;
}
