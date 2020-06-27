package com.dramancompany.taxiServiceBe.assignment.dto;

import com.dramancompany.taxiServiceBe.assignment.domain.Assignment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignmentDto {

    @Getter
    public static class Req {

        @NotNull
        @Size(max = 100)
        public String address;

        @Builder
        public Req(@NotNull @Size(max = 100) String address) {
            this.address = address;
        }

        public Assignment toEntity(Long passengerId) {
            return Assignment.builder()
                    .passengerId(passengerId)
                    .address(this.address)
                    .status(Assignment.Status.WAITING)
                    .requestDt(LocalDateTime.now())
                    .build();
        }
    }

    @Getter
    public static class Res {

        private Long id;
        public Long passengerId;
        public Long driverId;
        public String address;
        public Assignment.Status status;
        public LocalDateTime requestDt;
        public LocalDateTime completeDt;

        @Builder
        public Res(Long id, Long passengerId, Long driverId, String address, Assignment.Status status, LocalDateTime requestDt, LocalDateTime completeDt) {
            this.id = id;
            this.passengerId = passengerId;
            this.driverId = driverId;
            this.address = address;
            this.status = status;
            this.requestDt = requestDt;
            this.completeDt = completeDt;
        }

        public static Res of(Assignment assignment) {
            return Res.builder()
                    .id(assignment.getId())
                    .passengerId(assignment.getPassengerId())
                    .driverId(assignment.getDriverId())
                    .address(assignment.getAddress())
                    .status(assignment.getStatus())
                    .requestDt(assignment.getRequestDt())
                    .completeDt(assignment.getCompleteDt())
                    .build();
        }
    }
}
