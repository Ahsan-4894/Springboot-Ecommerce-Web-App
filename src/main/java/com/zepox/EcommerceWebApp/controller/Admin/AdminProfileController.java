package com.zepox.EcommerceWebApp.controller.Admin;

import com.zepox.EcommerceWebApp.dto.request.AdminEditMySelfRequestDto;
import com.zepox.EcommerceWebApp.dto.response.AdminGetMySelfResponseDto;
import com.zepox.EcommerceWebApp.dto.response.AdminLogoutResponseDto;
import com.zepox.EcommerceWebApp.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/profile")
@RequiredArgsConstructor
public class AdminProfileController {
    private final AdminService adminService;

    @GetMapping("/")
    public ResponseEntity<AdminGetMySelfResponseDto> getMyself(){
        return ResponseEntity.ok(adminService.getMySelf());
    }

    @GetMapping("/logout")
    public ResponseEntity<AdminLogoutResponseDto> logout(HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok().body(adminService.logout(response));
    }

//    Will implement it later.
    @Deprecated
    @PutMapping("/editMyself")
    public ResponseEntity<AdminGetMySelfResponseDto> editMyself(@Valid @RequestBody AdminEditMySelfRequestDto dto){
        return ResponseEntity.ok().body(adminService.editMyself(dto));
    }
}
