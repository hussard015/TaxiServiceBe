package com.dramancompany.taxiServiceBe.assignment.repository;

import com.dramancompany.taxiServiceBe.assignment.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    Stream<Assignment> findByOrderByCreatedDtDesc();
}
