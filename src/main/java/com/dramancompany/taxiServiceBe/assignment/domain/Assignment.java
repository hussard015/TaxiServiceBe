package com.dramancompany.taxiServiceBe.assignment.domain;

import com.dramancompany.taxiServiceBe.common.BaseEntity;
import com.dramancompany.taxiServiceBe.user.domain.User;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
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

    @OneToOne
    @JoinColumn(name= "passenger_id", referencedColumnName = "id", nullable = false)
    public User passenger;

    @OneToOne
    @JoinColumn(name= "driver_id", referencedColumnName = "id")
    public User driver;

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
        return driver != null || status == Status.COMPLETE;
    }

    public Assignment complete(User driver) {
        this.driver = driver;
        this.status = Status.COMPLETE;
        this.completeDt = LocalDateTime.now();
        return this;
    }

    @Builder
    public Assignment(User passenger, User driver, String address, Status status, LocalDateTime requestDt, LocalDateTime completeDt) {
        this.passenger = passenger;
        this.driver = driver;
        this.address = address;
        this.status = status;
        this.requestDt = requestDt;
        this.completeDt = completeDt;
    }
}
