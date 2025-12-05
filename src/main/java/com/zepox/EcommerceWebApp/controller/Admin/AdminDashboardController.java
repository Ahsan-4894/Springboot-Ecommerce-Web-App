package com.zepox.EcommerceWebApp.controller.Admin;

import com.zepox.EcommerceWebApp.dto.response.AdminDashboardStatsReponse;
import com.zepox.EcommerceWebApp.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {
    private final AdminService adminService;

    @GetMapping("/getDashboardStats")
    public ResponseEntity<AdminDashboardStatsReponse> getDashboardStats(){
        return ResponseEntity.ok().body(adminService.getDashboardStats());
    }
}
