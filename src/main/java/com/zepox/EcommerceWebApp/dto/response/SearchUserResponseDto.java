package com.zepox.EcommerceWebApp.dto.response;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchUserResponseDto {
    private boolean success;
    List<UsersResponseDto> message;
}
