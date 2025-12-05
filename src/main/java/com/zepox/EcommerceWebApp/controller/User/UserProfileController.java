package com.zepox.EcommerceWebApp.controller.User;

import com.zepox.EcommerceWebApp.dto.response.UserGetMySelfResponseDto;
import com.zepox.EcommerceWebApp.dto.response.UserLogoutResponseDto;
import com.zepox.EcommerceWebApp.service.UserService;
import com.zepox.EcommerceWebApp.util.AuthContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserService userService;
    private final AuthContext authContext;

    @GetMapping("/")
    public ResponseEntity<UserGetMySelfResponseDto> getMyself(){
        String userId = authContext.getIdOfCurrentLoggedInUser();
        return ResponseEntity.ok().body(userService.getMySelf(userId));
    }

    @GetMapping("/logout")
    public ResponseEntity<UserLogoutResponseDto> logout(HttpServletRequest request, HttpServletResponse response){
        return  ResponseEntity.ok().body(userService.logout(response));
    }

}
