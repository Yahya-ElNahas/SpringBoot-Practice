package com.practice.test.user.application;

import com.practice.test.user.application.dto.UserMapper;
import com.practice.test.user.Infrastructure.UserRepository;
import com.practice.test.user.domain.User;
import com.practice.test.user.application.dto.response.AllUsersResponse;
import com.practice.test.user.application.dto.response.UserResponse;
import com.practice.test.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return userMapper.toUserResponse(user);
    }

    public AllUsersResponse getAllUsers(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection
    ) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> usersPage = userRepository.findAll(pageable);

        List<UserResponse> userResponses = usersPage.getContent()
                .stream().map(userMapper::toUserResponse
                ).toList();

        return new AllUsersResponse(
                userResponses,
                usersPage.getNumber(),
                usersPage.getSize(),
                usersPage.getTotalElements(),
                usersPage.getTotalPages()
        );
    }
}

//    private final UserAddressRepository userAddressRepository;

//    @Transactional
//    public UserResponse updateUserAddress(UpdateUserAddressRequest body) {
//        if(userAddressRepository.existsById(new UserAddressCK(body.mobile, body.street))) {
//            throw new AddressExistsException();
//        }
//
//        UserAddress address = new UserAddress(
//                new UserAddressCK(body.mobile, body.street),
//                body.city,
//                body.state
//        );
//        UserAddress savedAddress = userAddressRepository.save(address);
//
//        User user = userRepository.findById(body.userId)
//                .orElseThrow(UserNotFoundException::new);
//        user.setAddress(savedAddress);
//        User savedUser = userRepository.save(user);
//
//        return new UserResponse(
//                savedUser.getId(),
//                savedUser.getName(),
//                savedUser.getEmail(),
//                savedUser.getAddress()
//        );
//    }

//    public String createProductsTable() {
//        productRepository.createProductsTable();
//        return "Success";
//    }