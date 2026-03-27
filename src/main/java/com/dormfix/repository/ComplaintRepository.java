package com.dormfix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dormfix.entity.Complaint;
import com.dormfix.entity.ComplaintStatus;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    // Student — their own complaints
    List<Complaint> findByStudentIdOrderByCreatedAtDesc(Long studentId);

    // Admin — filter by status
    List<Complaint> findByStatusOrderByCreatedAtDesc(ComplaintStatus status);

    // Admin — filter by hostel block
    List<Complaint> findByStudentHostelBlockOrderByCreatedAtDesc(
            String hostelBlock);

    // Admin — filter by both status and hostel block
    List<Complaint> findByStatusAndStudentHostelBlockOrderByCreatedAtDesc(
            ComplaintStatus status, String hostelBlock);

    // Admin — all complaints
    List<Complaint> findAllByOrderByCreatedAtDesc();

    long countByStatus(ComplaintStatus status);
}
