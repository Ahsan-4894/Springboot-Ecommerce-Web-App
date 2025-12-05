package com.zepox.EcommerceWebApp.controller.User;

import com.zepox.EcommerceWebApp.dto.request.GiveFeedbackDto;
import com.zepox.EcommerceWebApp.dto.response.GiveFeedbackResponseDto;
import com.zepox.EcommerceWebApp.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/feedback")
@RequiredArgsConstructor
public class UserFeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("/giveFeedback")
    public ResponseEntity<GiveFeedbackResponseDto> giveFeedback(@RequestBody GiveFeedbackDto req) {
        return ResponseEntity.ok().body(feedbackService.giveFeedback(req));
    }
}
