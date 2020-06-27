package com.dramancompany.taxiServiceBe.user.dto;
import com.dramancompany.taxiServiceBe.user.domain.User;
import lombok.Builder;
import lombok.Getter;

public class UserSimpleDto {

    @Getter
    public static class Res{
        private Long id;
        private String username;

        @Builder
        public Res(Long id, String username) {
            this.id = id;
            this.username = username;
        }

        public static Res of(User user) {
            return Res
                    .builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .build();
        }
    }
}
