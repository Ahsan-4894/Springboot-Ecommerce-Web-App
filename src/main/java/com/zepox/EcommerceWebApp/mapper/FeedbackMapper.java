package com.zepox.EcommerceWebApp.mapper;

import com.zepox.EcommerceWebApp.dto.response.FeedbackResponseDto;
import com.zepox.EcommerceWebApp.entity.Feedback;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeedbackMapper {
    public FeedbackResponseDto toDto(Feedback feedback) {
        return FeedbackResponseDto.builder()
                .itemId(feedback.getId().getItemId())
                .userId(feedback.getId().getUserId())
                .username(feedback.getUsername())
                .feedback(feedback.getFeedback())
                .rating(feedback.getRating())
                .build();
    }

    public List<FeedbackResponseDto> toDtos(List<Feedback> feedbacks) {
        return feedbacks.stream()
                .map(this::toDto)
                .toList();
    }
}
