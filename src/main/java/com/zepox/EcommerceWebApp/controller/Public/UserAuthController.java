package com.zepox.EcommerceWebApp.controller.Public;

import com.zepox.EcommerceWebApp.dto.request.UserLoginRequestDto;
import com.zepox.EcommerceWebApp.dto.request.UserSignupRequestDto;
import com.zepox.EcommerceWebApp.dto.response.UserLoginResponseDto;
import com.zepox.EcommerceWebApp.dto.response.UserSignupResponseDto;
import com.zepox.EcommerceWebApp.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/user")
@Slf4j
public class UserAuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto dto, HttpServletResponse response) {
        return ResponseEntity.ok().body(userService.login(dto, response));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponseDto> signup(@RequestBody UserSignupRequestDto dto, HttpServletResponse response) {
        return ResponseEntity.ok().body(userService.signup(dto, response));
    }
}
