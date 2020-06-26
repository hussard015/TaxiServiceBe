package com.dramancompany.taxiServiceBe.assignment.service;

import com.dramancompany.taxiServiceBe.assignment.domain.Assignment;
import com.dramancompany.taxiServiceBe.assignment.dto.AssignmentDto;
import com.dramancompany.taxiServiceBe.assignment.repository.AssignmentRepository;
import com.dramancompany.taxiServiceBe.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    public List<AssignmentDto.Res> getAll() {
        return assignmentRepository.findByOrderByCreatedDtDesc()
                .map(AssignmentDto.Res::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public AssignmentDto.Res requestAssignment(User user, AssignmentDto.Req req) {
        if (!user.isPassenger()) {
            throw new IllegalArgumentException("해당 유저는 승객이 아닙니다.");
        }
        return AssignmentDto.Res.of(assignmentRepository
                .save(req.toEntity(user.getId())));
    }

    @Transactional
    public AssignmentDto.Res catchAssignment(User user, Long assignmentId) {
        if (!user.isDriver()) {
            throw new IllegalArgumentException("해당 유저는 기사가 아닙니다.");
        }

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 배차가 존재하지 않습니다."));

        if (assignment.isCompleted()) {
            throw new IllegalStateException("이미 배차가 완료되었습니다.");
        }

        assignment.complete(user.getId());

        return AssignmentDto.Res.of(assignment);
    }
}
