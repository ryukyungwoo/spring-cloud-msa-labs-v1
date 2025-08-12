package com.sesac.orderservice.facade;

import com.sesac.orderservice.client.UserServiceClient;
import com.sesac.orderservice.client.dto.UserDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceFacade {

    private final UserServiceClient userServiceClient;

    @CircuitBreaker(name = "user-service", fallbackMethod = "getUserFallback")
    public UserDTO getUserWithFallback(Long userId) {
        log.info("User service call, userId = {}",userId);
        return userServiceClient.getUserById(userId);
    }

    public UserDTO getUserFallback(Long userId, Throwable ex) {

        log.warn("User Service fallback detect - userId = {}, error = {}", userId, ex.getMessage());

        UserDTO defaultUser = new UserDTO();
        defaultUser.setId(userId);
        defaultUser.setName("tempUser");
        defaultUser.setEmail("temp@temp.com");
        return defaultUser;
    }

}
