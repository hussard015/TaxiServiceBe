package com.dramancompany.taxiServiceBe.assignment.domain;

import com.dramancompany.taxiServiceBe.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
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
    public Progress progress;

    public LocalDateTime requestTime;
    public LocalDateTime completeTime;

    public enum Progress {
        WAITING,
        COMPLETE
    }

    public boolean isCompleted() {
        return driverId != null || progress == Progress.COMPLETE;
    }

    public Assignment complete(Long driverId) {
        this.driverId = driverId;
        this.progress = Progress.COMPLETE;
        this.completeTime = LocalDateTime.now();
        return this;
    }

    @Builder
    public Assignment(Long passengerId, Long driverId, String address, Progress progress, LocalDateTime requestTime, LocalDateTime completeTime) {
        this.passengerId = passengerId;
        this.driverId = driverId;
        this.address = address;
        this.progress = progress;
        this.requestTime = requestTime;
        this.completeTime = completeTime;
    }
}
