package com.zepox.EcommerceWebApp.dto.response;

import com.zepox.EcommerceWebApp.entity.Feedback;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GetFeedbacksResponseDto {
    private boolean success;
    private List<FeedbackResponseDto> message;
}
