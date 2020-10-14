package com.dong.restaurant.controller;

import com.dong.restaurant.domain.User;
import com.dong.restaurant.dto.SessionRequestDto;
import com.dong.restaurant.dto.SessionResponseDto;
import com.dong.restaurant.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
public class SessionController {

    private final UserService userService;

    @PostMapping("/session")
    public ResponseEntity<SessionResponseDto> create(@RequestBody SessionRequestDto sessionRequestDto) throws URISyntaxException {
        User user = userService.authenticate(sessionRequestDto.getEmail(), sessionRequestDto.getPassword());

        String accessToken = user.getAccessToken();

        SessionResponseDto sessionResponseDto = SessionResponseDto.builder().accessToken(accessToken).build();

        return ResponseEntity.created(new URI("/session")).body(sessionResponseDto);
    }
}