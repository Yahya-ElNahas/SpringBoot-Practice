//package com.practice.test.Service;
//
//import com.practice.test.Entities.User;
//import com.practice.test.Repositories.UserRepository;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public @NonNull UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
//        Optional<User> fetchedUser = userRepository.findByEmail(email);
//
//        if(fetchedUser.isEmpty()) {
//            throw new UsernameNotFoundException("User not found");
//        }
//
//        User user = fetchedUser.get();
//
//        return org.springframework.security.core.userdetails.User
//                .withUsername(user.getEmail())
//                .password(user.getPassword())
//                .authorities("ROLE_" + user.getRole().name())
//                .build();
//    }
//}