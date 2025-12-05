package com.zepox.EcommerceWebApp.service;

import com.zepox.EcommerceWebApp.dto.request.AdminEditMySelfRequestDto;
import com.zepox.EcommerceWebApp.dto.request.AdminLoginRequestDto;
import com.zepox.EcommerceWebApp.dto.request.AdminSignupRequestDto;
import com.zepox.EcommerceWebApp.dto.response.*;
import com.zepox.EcommerceWebApp.entity.Order;
import com.zepox.EcommerceWebApp.entity.User;
import com.zepox.EcommerceWebApp.entity.UserPrincipal;
import com.zepox.EcommerceWebApp.exception.custom.OrderDoesNotExistException;
import com.zepox.EcommerceWebApp.exception.custom.UserAlreadyExistsException;
import com.zepox.EcommerceWebApp.exception.custom.UserDoesNotExistException;
import com.zepox.EcommerceWebApp.repository.UserRepo;
import com.zepox.EcommerceWebApp.util.AuthContext;
import com.zepox.EcommerceWebApp.util.JwtAuthUtil;
import com.zepox.EcommerceWebApp.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final AuthenticationManager authenticationManager;
    private final JwtAuthUtil jwtAuthUtil;
    private final CookieUtil cookieUtil;
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthContext authContext;
    private final OrderService orderService;

    public AdminLoginResponseDto login(AdminLoginRequestDto loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        String token = jwtAuthUtil.generateAccessToken(user);

        cookieUtil.setCookieInTheBrowser(response, "token", token, 10*60);

        return AdminLoginResponseDto.builder()
                .userId(user.getId())
                .jwt(token)
                .username(user.getUsername())
                .success(true)
                .build();
    }

    public AdminLogoutResponseDto logout(HttpServletResponse response) {
//        Remove JWT cookie
        cookieUtil.deleteCookieFromBrowser(response, "token");
        return AdminLogoutResponseDto.builder()
                .success(true)
                .message("Admin logged out successfully")
                .build();
    }

    public AdminSignupResponseDto signup(AdminSignupRequestDto signupRequest, HttpServletResponse response) {
        User user = userRepo.findByUsername(signupRequest.username()).orElse(null);
        if(user!=null) throw new UserAlreadyExistsException("This username is already taken");
        user = User.builder()
                .username(signupRequest.username())
                .password(passwordEncoder.encode(signupRequest.password()))
                .phoneNumber(signupRequest.phoneNumber())
                .role("ROLE_ADMIN")
                .build();
        userRepo.save(user);
        String token = jwtAuthUtil.generateAccessToken(user);
        cookieUtil.setCookieInTheBrowser(response, "token", token, 15*60);
        return AdminSignupResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .success(true)
                .jwt(token)
                .build();
    }

    public AdminGetMySelfResponseDto getMySelf() {
        String adminId = authContext.getIdOfCurrentLoggedInUser();
        User admin = userRepo.findById(adminId).orElseThrow(()-> new UserDoesNotExistException("Access Denied"));

        return AdminGetMySelfResponseDto.builder()
                .success(true)
                .message("Admin fetched successfully")
                .userId(adminId)
                .role(admin.getRole())
                .username(admin.getUsername())
                .build();

    }

    public AdminGetMySelfResponseDto editMyself(AdminEditMySelfRequestDto dto) {
        return null;
    }

    public AdminDashboardStatsReponse getDashboardStats() {
        return null;
    }
//        Double totalRevenue = 0.0;
//
//        List<Last7DaysOrdersResponseDto> orders = orderService.getLast7DaysOrderPlaced();
//        if(orders.isEmpty()) throw new OrderDoesNotExistException("No orders placed yet!");
//
//        List<Double> recent7DaysRevenue = new ArrayList<>();
//
//        List<Last7DaysOrdersResponseDto> transformedRecentOrderRows = new ArrayList<>();
//
//        for(Last7DaysOrdersResponseDto order : orders){
//            totalRevenue = totalRevenue + order.getTotalRevenue();
//            recent7DaysRevenue.add(totalRevenue);
//            transformedRecentOrderRows.add(
//                    Last7DaysOrdersResponseDto.builder()
//                            .orderDate(order.getOrderDate())
//                            .totalOrders(order.getTotalOrders())
//                            .totalRevenue(order.getTotalRevenue())
//                            .build()
//            );
//        }
//
//        return AdminDashboardStatsReponse.builder()
//                .success(true)
//                .message(transformedRecentOrderRows)
//                .totalRevnue(totalRevenue)
//                .recent7DaysRevenue(recent7DaysRevenue)
//                .build();
//    }
}
