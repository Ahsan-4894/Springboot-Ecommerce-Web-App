package com.zepox.EcommerceWebApp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponseDto implements Serializable {
    private String itemId;
    private String userId;
    private String username;
    private String feedback;
    private Integer rating;
}
