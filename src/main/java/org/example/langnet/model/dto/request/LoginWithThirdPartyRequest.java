package org.example.langnet.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginWithThirdPartyRequest {
    private String email;
    private String username;
    private String password;
    private String image;
}
