package com.practice.main.user.application.dto;

import com.practice.main.user.domain.User;
import com.practice.main.user.application.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);
}