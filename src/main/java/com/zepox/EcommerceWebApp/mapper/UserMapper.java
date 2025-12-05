package com.zepox.EcommerceWebApp.mapper;

import com.zepox.EcommerceWebApp.dto.response.UsersResponseDto;
import com.zepox.EcommerceWebApp.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public UsersResponseDto toDto(User user){
        return UsersResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
    public List<UsersResponseDto> toDtos(List<User> users){
        return users
                .stream()
                .map(this::toDto)
                .toList();
    }
}
