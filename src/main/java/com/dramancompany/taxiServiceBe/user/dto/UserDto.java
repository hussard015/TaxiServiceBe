package com.dramancompany.taxiServiceBe.user.dto;
import com.dramancompany.taxiServiceBe.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    @Getter
    public static class SignUpReq {

        @Email(message = "이메일 형식이 아닙니다.")
        private String username;
        @NotNull
        private String password;
        @NotNull
        private User.UserType userType;

        @Builder
        public SignUpReq(String username, String password, User.UserType userType) {
            this.username = username;
            this.password = password;
            this.userType = userType;
        }

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .userType(userType)
                    .build();
        }

        public void changeEncodedPassword(String password) {
            this.password = password;
        }
    }

    @Getter
    public static class SignUpRes{
        private Long id;
        private String username;
        private User.UserType userType;

        @Builder
        public SignUpRes(Long id, String username, User.UserType userType) {
            this.id = id;
            this.username = username;
            this.userType = userType;
        }

        public static SignUpRes of(User user) {
            return SignUpRes
                    .builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .userType(user.getUserType())
                    .build();
        }
    }


    @Getter
    public static class SignInReq {
        @Email(message = "이메일 형식이 아닙니다.")
        private String username;
        @NotNull
        private String password;

        @Builder
        public SignInReq(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    @Getter
    public static class SignInRes{
        private String username;
        private User.UserType userType;
        private String token;

        @Builder
        public SignInRes(String username, User.UserType userType, String token) {
            this.username = username;
            this.userType = userType;
            this.token = token;
        }

        public static SignInRes of(User user) {
            return SignInRes
                    .builder()
                    .username(user.getUsername())
                    .userType(user.getUserType())
                    .build();
        }

        public SignInRes addToken(String token) {
            this.token = token;
            return this;
        }
    }
}
