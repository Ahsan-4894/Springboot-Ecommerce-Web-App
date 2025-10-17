package com.zepox.EcommerceWebApp.entity;

import com.zepox.EcommerceWebApp.entity.key.FeedbackDetailsId;
import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {
    @EmbeddedId
    private FeedbackDetailsId id;

   private int rating;
   private String feedback;

   private String name;
   private String email;

   @ManyToOne
   @MapsId("userId")
   @JoinColumn(name = "userId")
   private User user;

   @ManyToOne
   @MapsId("itemId")
   @JoinColumn(name = "itemId")
   private Item item;

}
