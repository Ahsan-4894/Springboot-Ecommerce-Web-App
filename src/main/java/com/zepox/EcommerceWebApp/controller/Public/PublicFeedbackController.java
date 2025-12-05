package com.zepox.EcommerceWebApp.controller.Public;

import com.zepox.EcommerceWebApp.dto.response.GetFeedbacksResponseDto;
import com.zepox.EcommerceWebApp.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/feedback")
public class PublicFeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping("/getFeedbacks")
    public ResponseEntity<GetFeedbacksResponseDto> getFeedbacks(@Validated @RequestParam(value = "productId") String productId){
        return ResponseEntity.ok().body(feedbackService.getFeedbacks(productId));
    }
}
