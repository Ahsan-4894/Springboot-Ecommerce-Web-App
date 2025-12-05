package com.zepox.EcommerceWebApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zepox.EcommerceWebApp.entity.key.FeedbackDetailsId;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "feedback")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Feedback extends BaseEntity implements Serializable {
    @EmbeddedId
    private FeedbackDetailsId id;

    private int rating;
    private String feedback;

    private String name;
    private String username;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    @JsonIgnore
    @ToString.Exclude
    private Item item;
}
