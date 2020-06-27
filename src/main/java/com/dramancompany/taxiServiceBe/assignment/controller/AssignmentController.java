package com.dramancompany.taxiServiceBe.assignment.controller;

import com.dramancompany.taxiServiceBe.assignment.dto.AssignmentDto;
import com.dramancompany.taxiServiceBe.assignment.service.AssignmentService;
import com.dramancompany.taxiServiceBe.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/assignment")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping
    public List<AssignmentDto.Res> list() {
        return assignmentService.getAll();
    }

    @PostMapping
    public AssignmentDto.Res requestAssignment(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid AssignmentDto.Req req) {
        return assignmentService.requestAssignment(user, req);
    }

    @PostMapping("/driver/{assignmentId}")
    public AssignmentDto.Res catchAssignment(
            @AuthenticationPrincipal User user,
            @PathVariable Long assignmentId
    ) {
        return assignmentService.catchAssignment(user, assignmentId);
    }
}
