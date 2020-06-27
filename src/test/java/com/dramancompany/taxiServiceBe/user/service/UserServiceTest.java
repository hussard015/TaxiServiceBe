package com.dramancompany.taxiServiceBe.user.service;

import com.dramancompany.taxiServiceBe.ServiceTest;
import com.dramancompany.taxiServiceBe.common.DuplicateUsernameException;
import com.dramancompany.taxiServiceBe.common.TokenProvider;
import com.dramancompany.taxiServiceBe.user.domain.User;
import com.dramancompany.taxiServiceBe.user.dto.UserDto;
import com.dramancompany.taxiServiceBe.user.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class UserServiceTest extends ServiceTest {

    @Autowired
    private UserRepository userRepository;

    private TokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    private User user;

    @Before
    public void setUp() {
        userRepository.deleteAll();

        tokenProvider = new TokenProvider();
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, tokenProvider, passwordEncoder);

        user = userRepository.save(User.builder()
                .username("je.chang@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .userType(User.UserType.Passenger)
                .build());
    }

    @Test(expected = IllegalArgumentException.class)
    public void 로그인_이메일이_존재하지_않음() {
        // given
        UserDto.SignInReq req = UserDto.SignInReq.builder()
                .username("je.chang123@gmail.com")
                .password("1234")
                .build();

        // when
        userService.signIn(req);
    }

    @Test(expected = IllegalArgumentException.class)
    public void 로그인_패스워드가_일치하지_않음() {
        // when
        UserDto.SignInReq req = UserDto.SignInReq.builder()
                .username(user.getUsername())
                .password("asjdhf")
                .build();
        userService.signIn(req);
    }


    @Test
    public void 로그인_정상동작() {
        // when
        UserDto.SignInReq req = UserDto.SignInReq.builder()
                .username(user.getUsername())
                .password("1234")
                .build();

        UserDto.SignInRes res = userService.signIn(req);

        // then
        assertThat(res.getUsername()).isEqualTo(user.getUsername());
        assertThat(res.getUserType()).isEqualTo(user.getUserType());
    }

    @Test(expected = DuplicateUsernameException.class)
    public void 회원가입_중복된유저() {
        // given
        UserDto.SignUpReq req = UserDto.SignUpReq.builder()
                .username(user.getUsername())
                .password("1234")
                .userType(User.UserType.Passenger)
                .build();

        userService.signUp(req);
    }

    @Test
    public void 회원가입_정상동작확인() {
        // given
        UserDto.SignUpReq req = UserDto.SignUpReq.builder()
                .username("je.chang555@gmail.com")
                .password("1234")
                .userType(User.UserType.Passenger)
                .build();
        // when
        UserDto.SignUpRes res = userService.signUp(req);
        User user = userRepository.findById(res.getId()).get();

        // then
        assertThat(user.getId()).isNotNull();
        assertThat(user.getUsername()).isEqualTo(req.getUsername());
        assertThat(user.getPassword()).isNotNull();
        assertThat(user.getUserType()).isEqualTo(req.getUserType());
    }
}