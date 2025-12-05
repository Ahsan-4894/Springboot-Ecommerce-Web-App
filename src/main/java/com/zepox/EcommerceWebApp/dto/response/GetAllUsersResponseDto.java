package com.zepox.EcommerceWebApp.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetAllUsersResponseDto {
    private boolean success;
    List<UsersResponseDto> message;
    int totalPages;
    int currentPage;
}
