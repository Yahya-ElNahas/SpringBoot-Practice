package com.practice.main.user.application;

import com.practice.main.user.application.dto.UserMapper;
import com.practice.main.user.Infrastructure.UserRepository;
import com.practice.main.user.domain.User;
import com.practice.main.user.application.dto.response.AllUsersResponse;
import com.practice.main.user.application.dto.response.UserResponse;
import com.practice.main.user.application.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
        Set<String> ALLOWED_SORT_FIELDS = Set.of("name", "email");
        if (!ALLOWED_SORT_FIELDS.contains(sortBy.toLowerCase())) {
            sortBy = "name";
        }
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> usersPage = userRepository.findAll(pageable);

        List<UserResponse> userResponses = usersPage.getContent()
                .stream().map(userMapper::toUserResponse).toList();

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