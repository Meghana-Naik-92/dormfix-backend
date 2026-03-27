package com.dormfix.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dormfix.dto.ComplaintRequest;
import com.dormfix.dto.ComplaintResponse;
import com.dormfix.dto.StatsResponse;
import com.dormfix.entity.Complaint;
import com.dormfix.entity.User;
import com.dormfix.repository.ComplaintRepository;
import com.dormfix.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public ComplaintResponse submitComplaint(ComplaintRequest request) {
        User student = getCurrentUser();

        Complaint complaint = Complaint.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .student(student)
                .build();

        return mapToResponse(complaintRepository.save(complaint));
    }

    public List<ComplaintResponse> getMyComplaints() {
        User student = getCurrentUser();
        return complaintRepository
                .findByStudentIdOrderByCreatedAtDesc(student.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ComplaintResponse getComplaintById(Long id) {
        User student = getCurrentUser();
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        if (!complaint.getStudent().getId().equals(student.getId())) {
            throw new RuntimeException("Access denied");
        }

        return mapToResponse(complaint);
    }

    private ComplaintResponse mapToResponse(Complaint complaint) {
        return ComplaintResponse.builder()
                .id(complaint.getId())
                .title(complaint.getTitle())
                .description(complaint.getDescription())
                .category(complaint.getCategory())
                .status(complaint.getStatus())
                .studentName(complaint.getStudent().getName())
                .studentEmail(complaint.getStudent().getEmail())
                .hostelBlock(complaint.getStudent().getHostelBlock())
                .roomNumber(complaint.getStudent().getRoomNumber())
                .createdAt(complaint.getCreatedAt())
                .updatedAt(complaint.getUpdatedAt())
                .build();
    }

    public StatsResponse getMyStats() {
        User student = getCurrentUser();
        Long studentId = student.getId();

        long total = complaintRepository
                .findByStudentIdOrderByCreatedAtDesc(studentId).size();
        long pending = complaintRepository
                .findByStudentIdOrderByCreatedAtDesc(studentId)
                .stream()
                .filter(c -> c.getStatus()
                == com.dormfix.entity.ComplaintStatus.PENDING)
                .count();
        long inProgress = complaintRepository
                .findByStudentIdOrderByCreatedAtDesc(studentId)
                .stream()
                .filter(c -> c.getStatus()
                == com.dormfix.entity.ComplaintStatus.IN_PROGRESS)
                .count();
        long resolved = complaintRepository
                .findByStudentIdOrderByCreatedAtDesc(studentId)
                .stream()
                .filter(c -> c.getStatus()
                == com.dormfix.entity.ComplaintStatus.RESOLVED)
                .count();

        return new StatsResponse(total, pending, inProgress, resolved);
    }
}
