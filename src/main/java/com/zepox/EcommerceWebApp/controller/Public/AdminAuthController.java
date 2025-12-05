package com.zepox.EcommerceWebApp.controller.Public;

import com.zepox.EcommerceWebApp.dto.request.AdminLoginRequestDto;
import com.zepox.EcommerceWebApp.dto.request.AdminSignupRequestDto;
import com.zepox.EcommerceWebApp.dto.response.AdminLoginResponseDto;
import com.zepox.EcommerceWebApp.dto.response.AdminSignupResponseDto;
import com.zepox.EcommerceWebApp.service.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/public/admin")
public class AdminAuthController {
    private final AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponseDto> login(@RequestBody AdminLoginRequestDto dto, HttpServletResponse response) {
        return ResponseEntity.ok().body(adminService.login(dto, response));
    }

    @PostMapping("/signup")
    public ResponseEntity<AdminSignupResponseDto> signup(@RequestBody AdminSignupRequestDto dto, HttpServletResponse response) {
        return ResponseEntity.ok().body(adminService.signup(dto, response));
    }
}
