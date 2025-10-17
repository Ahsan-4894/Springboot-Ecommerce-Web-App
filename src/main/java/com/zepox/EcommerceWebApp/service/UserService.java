package com.zepox.EcommerceWebApp.service;

import com.zepox.EcommerceWebApp.dto.request.UserLoginRequestDto;
import com.zepox.EcommerceWebApp.dto.request.UserSignupRequestDto;
import com.zepox.EcommerceWebApp.dto.response.UserLoginResponseDto;
import com.zepox.EcommerceWebApp.dto.response.UserSignupResponseDto;
import com.zepox.EcommerceWebApp.entity.User;
import com.zepox.EcommerceWebApp.exception.custom.UserAlreadyExistsException;
import com.zepox.EcommerceWebApp.repository.UserRepo;
import com.zepox.EcommerceWebApp.util.AuthUtil;
import com.zepox.EcommerceWebApp.util.SetCookieInBrowser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final SetCookieInBrowser setCookieInBrowser;

    public UserLoginResponseDto login(UserLoginRequestDto loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        String token = authUtil.generateAccessToken(user);

        setCookieInBrowser.setCookieInTheBrowser(response, "token", token, 15*60);

        return UserLoginResponseDto.builder()
                .userId(user.getId())
                .jwt(token)
                .username(user.getUsername())
                .success(true)
                .build();
    }

    public UserSignupResponseDto signup(UserSignupRequestDto dto, HttpServletResponse response) {
        User user = userRepo.findByUsername(dto.getUsername()).orElse(null);
        if(user!=null) throw new UserAlreadyExistsException("This username is already taken");

        user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .role("USER")
                .build();

        userRepo.save(user);

        String token = authUtil.generateAccessToken(user);

        setCookieInBrowser.setCookieInTheBrowser(response, "token", token, 15*60);
        return UserSignupResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .success(true)
                .jwt(token)
                .build();
    }
}
