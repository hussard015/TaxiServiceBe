package com.dramancompany.taxiServiceBe.user.controller;

import com.dramancompany.taxiServiceBe.user.domain.User;
import com.dramancompany.taxiServiceBe.user.dto.UserDto;
import com.dramancompany.taxiServiceBe.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/pub")
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public UserDto.SignUpRes signUp(
            @RequestBody @Valid UserDto.SignUpReq req
    ) {
        return userService.singUp(req);
    }

    @PostMapping("/signIn")
    public UserDto.SignInRes signIn(
            @RequestBody @Valid UserDto.SignInReq req
    ) {
        return userService.signIn(req);
    }

}
