package com.zepox.EcommerceWebApp.controller.Admin;

import com.zepox.EcommerceWebApp.dto.request.SearchUsersRequestDto;
import com.zepox.EcommerceWebApp.dto.response.GetAllUsersResponseDto;
import com.zepox.EcommerceWebApp.dto.response.SearchUserResponseDto;
import com.zepox.EcommerceWebApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @GetMapping("/getUsers")
    public ResponseEntity<GetAllUsersResponseDto> getUsers(
            @RequestParam(defaultValue = "1") String page
    ) {
        return ResponseEntity.ok().body(userService.getAllUsers(Integer.parseInt(page)));
    }
    @PostMapping("/searchUsers")
    public ResponseEntity<SearchUserResponseDto> searchUsers(@RequestBody SearchUsersRequestDto dto) {
        return ResponseEntity.ok().body(userService.searchUsers(dto));
    }
}
