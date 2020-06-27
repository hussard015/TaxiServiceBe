package com.dramancompany.taxiServiceBe.user.service;

import com.dramancompany.taxiServiceBe.common.TokenProvider;
import com.dramancompany.taxiServiceBe.user.domain.User;
import com.dramancompany.taxiServiceBe.user.dto.UserDto;
import com.dramancompany.taxiServiceBe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public UserDto.SignInRes signIn(UserDto.SignInReq req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("패스워드가 일지하지 않습니다.");
        }

        String token = tokenProvider.createToken(user);

        return UserDto.SignInRes.of(user).addToken(token);

    }

    public UserDto.SignUpRes signUp(UserDto.SignUpReq req) {
        if (verifyDupUsername(req.getUsername())) {
            throw new IllegalArgumentException("중복된 유저입니다.");
        }

        req.changeEncodedPassword(passwordEncoder.encode(req.getPassword()));
        return UserDto.SignUpRes.of(userRepository.save(req.toEntity()));
    }


    private boolean verifyDupUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
