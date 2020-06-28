package com.dramancompany.taxiServiceBe.assignment.dto;

import com.dramancompany.taxiServiceBe.assignment.domain.Assignment;
import com.dramancompany.taxiServiceBe.user.domain.User;
import com.dramancompany.taxiServiceBe.user.dto.UserSimpleDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class AssignmentDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req {

        @NotEmpty
        @Size(max = 100)
        public String address;

        @Builder
        public Req(String address) {
            this.address = address;
        }

        public Assignment toEntity(User passenger) {
            return Assignment.builder()
                    .passenger(passenger)
                    .address(this.address)
                    .status(Assignment.Status.WAITING)
                    .requestDt(LocalDateTime.now())
                    .build();
        }
    }

    @Getter
    public static class Res {

        private Long id;
        public UserSimpleDto.Res passenger;
        public UserSimpleDto.Res driver;
        public String address;
        public Assignment.Status status;
        public LocalDateTime requestDt;
        public LocalDateTime completeDt;

        @Builder
        public Res(Long id, UserSimpleDto.Res passenger, UserSimpleDto.Res driver, String address, Assignment.Status status, LocalDateTime requestDt, LocalDateTime completeDt) {
            this.id = id;
            this.passenger = passenger;
            this.driver = driver;
            this.address = address;
            this.status = status;
            this.requestDt = requestDt;
            this.completeDt = completeDt;
        }

        public static Res of(Assignment assignment) {
            return Res.builder()
                    .id(assignment.getId())
                    .passenger(UserSimpleDto.Res.of(assignment.getPassenger()))
                    .driver(assignment.getDriver() != null ? UserSimpleDto.Res.of(assignment.getDriver())  : null)
                    .address(assignment.getAddress())
                    .status(assignment.getStatus())
                    .requestDt(assignment.getRequestDt())
                    .completeDt(assignment.getCompleteDt())
                    .build();
        }
    }
}
