package com.zepox.EcommerceWebApp.util;

import com.zepox.EcommerceWebApp.entity.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthContext {
    public UserPrincipal getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return (UserPrincipal) authentication.getPrincipal();
        }
        return null;
    }
    public String getIdOfCurrentLoggedInUser(){
        UserPrincipal user = getCurrentLoggedInUser();
        return (user!=null) ? user.getId() : null;
    }
}
