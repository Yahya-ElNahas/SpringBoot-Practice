package com.practice.test.user.application.dto;

import com.practice.test.user.domain.User;
import com.practice.test.user.application.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);
}