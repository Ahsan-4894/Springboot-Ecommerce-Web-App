package com.zepox.EcommerceWebApp.repository;

import com.zepox.EcommerceWebApp.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepo extends JpaRepository<Feedback, String> {
    Feedback findFeedbackByUserIdAndItemId(String userId, String itemId);

    @Query("""
    SELECT F FROM Feedback F
    WHERE F.item.id = :itemId
    """)
    Optional<List<Feedback>> findFeedbackByItemId(@Param("itemId") String itemId);
}
