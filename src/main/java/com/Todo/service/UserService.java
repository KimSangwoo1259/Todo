package com.Todo.service;

import com.Todo.domain.User;
import com.Todo.dto.response.UserJoinResponse;
import com.Todo.dto.response.UserLoginResponse;
import com.Todo.exception.ErrorCode;
import com.Todo.exception.TodoException;
import com.Todo.repository.UserRepository;
import com.Todo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;


    @Value("${jwt.secret-key}")
    private String key;

    @Value("${jwt.token.expired-time-ms}")
    private long expiredTimeMs;

    @Transactional
    public UserJoinResponse join(String username, String password, String email){

        log.info("[UserService join] username: {}, email: {}, password: {}", username, email, password);
        userRepository.findByUsername(username).ifPresent(it -> {
            throw new TodoException(ErrorCode.USERNAME_DUPLICATED, String.format("%s is duplicated", username));
        });

        User savedUser = userRepository.save(User.of(username, encoder.encode(password),  email));

        return UserJoinResponse.fromEntity(savedUser);
    }

    @Transactional
    public UserLoginResponse login(String username, String password){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new TodoException(ErrorCode.USER_NOTFOUND, String.format("%s not found", username)));

        if (!encoder.matches(password, user.getPassword())) {
            throw new TodoException(ErrorCode.INVALID_PASSWORD, String.format("Invalid password: %s", password));
        }

        log.info("[UserService login]  login success  username: {}, password: {}", user.getUsername(), user.getPassword());

        String token = JwtUtil.generateToken(username, key, expiredTimeMs);
        return new UserLoginResponse(token);
    }

}
