package org.example.langnet.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private UUID notificationId;
    private AppUser sender;
    private AppUser receiver;
    private String type;
    private String title;
    private String description;
    private Boolean isRead;
    private LocalDateTime createDate;
}
