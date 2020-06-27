package com.dramancompany.taxiServiceBe.assignment.service;

import com.dramancompany.taxiServiceBe.assignment.domain.Assignment;
import com.dramancompany.taxiServiceBe.assignment.dto.AssignmentDto;
import com.dramancompany.taxiServiceBe.assignment.repository.AssignmentRepository;
import com.dramancompany.taxiServiceBe.user.domain.User;
import com.dramancompany.taxiServiceBe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    public List<AssignmentDto.Res> getAll() {
        return assignmentRepository.findByOrderByRequestDtDesc()
                .stream()
                .map(AssignmentDto.Res::of)
                .collect(Collectors.toList());
    }

    public AssignmentDto.Res requestAssignment(User user, AssignmentDto.Req req) {
        User passenger = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 잘못되었습니다."));

        if (!passenger.isPassenger()) {
            throw new IllegalArgumentException("해당 유저는 승객이 아닙니다.");
        }

        if (hasAssignment(passenger.getId())) {
            throw new IllegalArgumentException("해당 유저는 배차 중입니다.");
        }

        return AssignmentDto.Res.of(assignmentRepository
                .save(req.toEntity(passenger)));
    }

    public AssignmentDto.Res catchAssignment(User user, Long assignmentId) {
        User driver = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 잘못되었습니다."));

        if (!driver.isDriver()) {
            throw new IllegalArgumentException("해당 유저는 기사가 아닙니다.");
        }

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 배차가 존재하지 않습니다."));

        if (assignment.isCompleted()) {
            throw new IllegalArgumentException("이미 배차가 완료되었습니다.");
        }

        assignment.complete(driver);

        return AssignmentDto.Res.of(assignmentRepository.save(assignment));
    }

    private boolean hasAssignment(Long passengerId) {
        return assignmentRepository.findTopByPassengerIdOrderByRequestDtDesc(passengerId)
                .filter(assignment -> assignment.getStatus() == Assignment.Status.WAITING)
                .isPresent();
    }
}
