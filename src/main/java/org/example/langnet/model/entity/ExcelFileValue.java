package org.example.langnet.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelFileValue {
    private Long id;
    private String key;
    private String value;
}
