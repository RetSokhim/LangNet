package org.example.langnet.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.langnet.model.dto.respond.ExcelFileValue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {
    private UUID attachmentId;
    private Language language;
    private AppUser postBy;
    private List<Feedback> feedback;
    private String attachmentName;
    private ExcelFileValue data;
    private List<Hint> hints;
    private String status;
    private LocalDateTime postedDate;
    private LocalDateTime expireDate;

}
