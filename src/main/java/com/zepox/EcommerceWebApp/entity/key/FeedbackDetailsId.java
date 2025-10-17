package com.zepox.EcommerceWebApp.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class FeedbackDetailsId implements Serializable {
    @Column(name = "user_id")
    private String userId;

    @Column(name = "item_id")
    private String itemId;
}
