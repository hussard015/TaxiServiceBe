package com.dramancompany.taxiServiceBe.assignment.dto;

import com.dramancompany.taxiServiceBe.assignment.domain.Assignment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignmentDto {
    public static class Req {

        public String address;

        public Assignment toEntity(Long passengerId) {
            return Assignment.builder()
                    .passengerId(passengerId)
                    .address(this.address)
                    .progress(Assignment.Progress.WAITING)
                    .requestTime(LocalDateTime.now())
                    .build();
        }
    }

    public static class Res {

        public static Res of(Assignment assignment) {
            return null;
        }
    }
}
