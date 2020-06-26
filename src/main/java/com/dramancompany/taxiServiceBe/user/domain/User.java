package com.dramancompany.taxiServiceBe.user.domain;

import com.dramancompany.taxiServiceBe.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.stream.Stream;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, length = 255)
    private String username;
    @Column(nullable = false, length = 255)
    private String password;
    @Column(nullable = false, length = 1)
    private UserType userType;

    @NoArgsConstructor
    public enum UserType {
        Passenger,
        Driver;

        @JsonValue
        public int toValue(){
            return ordinal();
        }

        public static UserType of(int num) {
            return Stream.of(values())
                    .filter(userType -> userType.ordinal() == num)
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown enum type"));
        }
    }

    @Builder
    public User(Long id, String username, String password, UserType userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public boolean isPassenger() {
        return this.userType == UserType.Passenger;
    }

    public boolean isDriver() {
        return this.userType == UserType.Driver;
    }
}
