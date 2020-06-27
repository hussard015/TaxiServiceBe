package com.dramancompany.taxiServiceBe.assignment.domain;

import com.dramancompany.taxiServiceBe.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Assignment extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    public Long passengerId;

    public Long driverId;
    @Column(nullable = false, length = 100)
    public String address;
    public Status status;

    public LocalDateTime requestDt;
    public LocalDateTime completeDt;

    public enum Status {
        WAITING,
        COMPLETE;

        @JsonValue
        public int toValue() {
            return ordinal();
        }

        public static String toText() {
            return Stream.of(values())
                    .map(status -> status.ordinal() + " = " + status.name())
                    .collect(Collectors.joining(", "));
        }
    }

    public boolean isCompleted() {
        return driverId != null || status == Status.COMPLETE;
    }

    public Assignment complete(Long driverId) {
        this.driverId = driverId;
        this.status = Status.COMPLETE;
        this.completeDt = LocalDateTime.now();
        return this;
    }

    @Builder
    public Assignment(Long passengerId, Long driverId, String address, Status status, LocalDateTime requestDt, LocalDateTime completeDt) {
        this.passengerId = passengerId;
        this.driverId = driverId;
        this.address = address;
        this.status = status;
        this.requestDt = requestDt;
        this.completeDt = completeDt;
    }
}
