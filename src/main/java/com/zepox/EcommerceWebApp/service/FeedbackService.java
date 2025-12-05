package com.zepox.EcommerceWebApp.service;

import com.zepox.EcommerceWebApp.dto.request.GiveFeedbackDto;
import com.zepox.EcommerceWebApp.dto.response.FeedbackResponseDto;
import com.zepox.EcommerceWebApp.dto.response.GetFeedbacksResponseDto;
import com.zepox.EcommerceWebApp.dto.response.GiveFeedbackResponseDto;
import com.zepox.EcommerceWebApp.entity.Feedback;
import com.zepox.EcommerceWebApp.entity.Item;
import com.zepox.EcommerceWebApp.entity.Order;
import com.zepox.EcommerceWebApp.entity.User;
import com.zepox.EcommerceWebApp.entity.key.FeedbackDetailsId;
import com.zepox.EcommerceWebApp.exception.custom.FeedbackAlreadyGivenException;
import com.zepox.EcommerceWebApp.exception.custom.FeedbackDoesNotExistException;
import com.zepox.EcommerceWebApp.exception.custom.ItemNotPurchasedException;
import com.zepox.EcommerceWebApp.mapper.FeedbackMapper;
import com.zepox.EcommerceWebApp.repository.FeedbackRepo;
import com.zepox.EcommerceWebApp.util.AuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final UserService userService;
    private final AuthContext authContext;
    private final ProductService productService;
    private final FeedbackRepo  feedbackRepo;
    private final OrderItemService orderItemService;
    private final FeedbackMapper feedbackMapper;
    private final OrderService orderService;

    public GiveFeedbackResponseDto giveFeedback(GiveFeedbackDto req) {
        String userId = authContext.getIdOfCurrentLoggedInUser();


//        Check userId and productId is valid or not;
        User user = userService.findUserById(userId);
        Item product = productService.findProductById(req.productId());


//        Check if user has bought this product ever or not before submitting his/her feedback?
        List<String> orderIdsOfAUser = orderService.getConfirmedOrderIdsOfAUser(user.getId());

        boolean havePurchasedBefore = orderItemService.checkItemExistInTheseOrders(orderIdsOfAUser, product.getId());

        if(!havePurchasedBefore) throw new ItemNotPurchasedException("You haven't purchased this product yet");

//        Check what if user is giving multiple times feedback of same thing, NOT ALLOWED!
        Feedback isFeedbackAlreadyGiven = feedbackRepo.findFeedbackByUserIdAndItemId(userId, product.getId());

        if(isFeedbackAlreadyGiven != null) throw new FeedbackAlreadyGivenException("You have already given feedback for this product");

//        Now record customer's feedback for this item
        FeedbackDetailsId feedbackDetailsId = new FeedbackDetailsId(user.getId(), product.getId());
        Feedback newFeedback = Feedback.builder()
                .name(req.name())
                .username(user.getUsername())
                .rating(Integer.parseInt(req.rating()))
                .feedback(req.feedback())
                .id(feedbackDetailsId)
                .user(user)
                .item(product)
                .build();

        feedbackRepo.save(newFeedback);
        System.out.println("Im heading towards enD!!!");
        return GiveFeedbackResponseDto.builder()
                .success(true)
                .message("Thanks for your feedback!")
                .build();
    }

    public GetFeedbacksResponseDto getFeedbacks(String productId) {
        List<Feedback> feedbacksOfAProduct = feedbackRepo.findFeedbackByItemId(productId).orElseThrow(()-> new FeedbackDoesNotExistException("No Feedbacks for this product!"));
        if(feedbacksOfAProduct.isEmpty()) throw new FeedbackDoesNotExistException("No Feedbacks for this product!");
        List<FeedbackResponseDto> transformedFeedbacks = feedbackMapper.toDtos(feedbacksOfAProduct);
        return GetFeedbacksResponseDto.builder()
                .success(true)
                .message(transformedFeedbacks)
                .build();
    }
}
