package com.zepox.EcommerceWebApp.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGetMySelfResponseDto implements Serializable {
    private boolean success;
    private String message;
    private String userId;
    private String username;
    private String role;
}
