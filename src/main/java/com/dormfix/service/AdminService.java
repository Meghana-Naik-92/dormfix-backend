package com.dormfix.service;

import com.dormfix.dto.ComplaintResponse;
import com.dormfix.dto.StatsResponse;
import com.dormfix.entity.Complaint;
import com.dormfix.entity.ComplaintStatus;
import com.dormfix.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ComplaintRepository complaintRepository;

    // Get all complaints with optional filters
    public List<ComplaintResponse> getAllComplaints(String status,
            String hostelBlock) {
        List<Complaint> complaints;

        // Filter by status only
        if (status != null && !status.isEmpty()
                && (hostelBlock == null || hostelBlock.isEmpty())) {
            ComplaintStatus cs = ComplaintStatus.valueOf(status.toUpperCase());
            complaints = complaintRepository
                    .findByStatusOrderByCreatedAtDesc(cs);

            // Filter by hostel block only
        } else if (hostelBlock != null && !hostelBlock.isEmpty()
                && (status == null || status.isEmpty())) {
            complaints = complaintRepository
                    .findByStudentHostelBlockOrderByCreatedAtDesc(hostelBlock);

            // Filter by both
        } else if (status != null && !status.isEmpty()
                && hostelBlock != null && !hostelBlock.isEmpty()) {
            ComplaintStatus cs = ComplaintStatus.valueOf(status.toUpperCase());
            complaints = complaintRepository
                    .findByStatusAndStudentHostelBlockOrderByCreatedAtDesc(
                            cs, hostelBlock);

            // No filter — return all
        } else {
            complaints = complaintRepository.findAllByOrderByCreatedAtDesc();
        }

        return complaints.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get single complaint by ID
    public ComplaintResponse getComplaintById(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                "Complaint not found"));
        return mapToResponse(complaint);
    }

    // Update complaint status
    public ComplaintResponse updateStatus(Long id, String status) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                "Complaint not found"));

        try {
            complaint.setStatus(
                    ComplaintStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(
                    "Invalid status. Use: PENDING, IN_PROGRESS, RESOLVED");
        }

        return mapToResponse(complaintRepository.save(complaint));
    }

    // Reuse same mapToResponse from ComplaintService
    private ComplaintResponse mapToResponse(Complaint complaint) {
        return com.dormfix.dto.ComplaintResponse.builder()
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

    public StatsResponse getStats() {
        long total = complaintRepository.count();
        long pending = complaintRepository
                .countByStatus(ComplaintStatus.PENDING);
        long inProgress = complaintRepository
                .countByStatus(ComplaintStatus.IN_PROGRESS);
        long resolved = complaintRepository
                .countByStatus(ComplaintStatus.RESOLVED);
        return new StatsResponse(total, pending, inProgress, resolved);
    }
}
