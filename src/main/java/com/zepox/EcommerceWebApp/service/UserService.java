package com.zepox.EcommerceWebApp.service;

import com.zepox.EcommerceWebApp.dto.request.SearchUsersRequestDto;
import com.zepox.EcommerceWebApp.dto.request.UserEditMySelfRequestDto;
import com.zepox.EcommerceWebApp.dto.request.UserLoginRequestDto;
import com.zepox.EcommerceWebApp.dto.request.UserSignupRequestDto;
import com.zepox.EcommerceWebApp.dto.response.*;
import com.zepox.EcommerceWebApp.entity.User;
import com.zepox.EcommerceWebApp.entity.UserPrincipal;
import com.zepox.EcommerceWebApp.exception.custom.UserAlreadyExistsException;
import com.zepox.EcommerceWebApp.exception.custom.UserDoesNotExistException;
import com.zepox.EcommerceWebApp.mapper.UserMapper;
import com.zepox.EcommerceWebApp.repository.UserRepo;
import com.zepox.EcommerceWebApp.util.AuthContext;
import com.zepox.EcommerceWebApp.util.JwtAuthUtil;
import com.zepox.EcommerceWebApp.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtAuthUtil jwtAuthUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final CookieUtil cookieUtil;
    private final UserMapper userMapper;
    private final AuthContext authContext;

    public User findUserById(String userId){
        User user = userRepo.findById(userId).orElseThrow(()-> new UserDoesNotExistException("User not found with this Id: "+userId));
        return user;
    }


    public UserLoginResponseDto login(UserLoginRequestDto loginRequest, HttpServletResponse response) throws BadCredentialsException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        String token = jwtAuthUtil.generateAccessToken(user);

        cookieUtil.setCookieInTheBrowser(response, "token", token, 15*60);

        return UserLoginResponseDto.builder()
                .userId(user.getId())
                .message("User login success")
                .username(user.getUsername())
                .success(true)
                .build();
    }

    public UserSignupResponseDto signup(UserSignupRequestDto signupRequest, HttpServletResponse response) {
        User user = userRepo.findByUsername(signupRequest.username()).orElse(null);
        if(user!=null) throw new UserAlreadyExistsException("This username is already taken");

        user = User.builder()
                .username(signupRequest.username())
                .password(passwordEncoder.encode(signupRequest.password()))
                .phoneNumber(signupRequest.phoneNumber())
                .role("ROLE_USER")
                .build();

        userRepo.save(user);

        String token = jwtAuthUtil.generateAccessToken(user);

        cookieUtil.setCookieInTheBrowser(response, "token", token, 15*60);
        return UserSignupResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .success(true)
                .message("User registered success")
                .build();
    }


    @Cacheable(value = "USER_MYSELF", key = "#userId", condition = "#userId!=null")
    public UserGetMySelfResponseDto getMySelf(String userId) {
        User user = userRepo.findById(userId).orElseThrow(()-> new UserDoesNotExistException("User doesn't exist"));

        return UserGetMySelfResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .success(true)
                .message("User fetched successfully")
                .build();

    }

    public UserLogoutResponseDto logout(HttpServletResponse response) {
        cookieUtil.deleteCookieFromBrowser(response, "token");
        return UserLogoutResponseDto.builder()
                .success(true)
                .message("User successfully logged out")
                .build();
    }

    public UserGetMySelfResponseDto editMyself(UserEditMySelfRequestDto dto) {
        String userId = authContext.getIdOfCurrentLoggedInUser();
        User user = userRepo.findById(userId).orElse(null);
        if(user == null) throw new UserDoesNotExistException("User doesn't exist");

        String hashedPassword = null;
        if(dto.password()!=null && !dto.password().isEmpty()){
            hashedPassword = passwordEncoder.encode(dto.password());
        }
        String newPassword = hashedPassword!=null ? hashedPassword : user.getPassword();
        String newPhoneNumber = dto.phoneNumber()!=null ? dto.phoneNumber() : user.getPhoneNumber();
        user.setPassword(newPassword);
        user.setPhoneNumber(newPhoneNumber);
        userRepo.save(user);

        return UserGetMySelfResponseDto.builder()
                .success(true)
                .message("User updated successfully")
                .userId(user.getId())
                .role(user.getRole())
                .username(user.getUsername())
                .build();
    }

    @Cacheable(value = "ALL_USERS", key = "#page + '_' + #size")
    public GetAllUsersResponseDto getAllUsers(int pageNo) {
        int usersPerPage = 20;
        Pageable pageable = PageRequest.of(pageNo, usersPerPage);
        Page<User> userPage = userRepo.findAll(pageable);

        if(userPage.isEmpty()) throw new UserDoesNotExistException("Users doesn't exist");
        List<UsersResponseDto> transformedUsers = userMapper.toDtos(userPage.getContent());

        return GetAllUsersResponseDto.builder()
                .success(true)
                .message(transformedUsers)
                .currentPage(pageNo)
                .totalPages(userPage.getTotalPages())
                .build();
    }

    @Cacheable(value = "USER_BY_ID", key="#id", condition = "#id!=null")
    public UsersResponseDto getUserById(String id){
        User user = userRepo.findById(id).orElse(null);
        if(user == null) throw new UserDoesNotExistException("User doesn't exist");
        return UsersResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    @Cacheable(value = "USER_SEARCH_BY_ID", key = "#userId", condition = "#userId!=null")
    public SearchUserResponseDto searchUserById(String userId){
        UsersResponseDto user = this.getUserById(userId);
        return SearchUserResponseDto.builder()
                .success(true)
                .message(List.of(user))
                .build();
    }

    @Cacheable(
            value = "USER_SEARCH_BY_NAME",
            key = "#userName",
            condition = "#userName!=null AND #userName.length() > 0"
    )
    public SearchUserResponseDto searchUserByName(String userName){
        List<User> users =  userRepo.searchUsersByUsername("%"+userName+"%");
        if(users.isEmpty()) throw new UserDoesNotExistException("Users doesn't exist");
        List<UsersResponseDto> transformedUsers = userMapper.toDtos(users);
        return SearchUserResponseDto.builder()
                .success(true)
                .message(transformedUsers)
                .build();
    }

    public SearchUserResponseDto searchUsers(SearchUsersRequestDto req) {
        if(req.id() != null){
            return this.searchUserById(req.id());
        }
        return this.searchUserByName(req.userName());
    }
}
