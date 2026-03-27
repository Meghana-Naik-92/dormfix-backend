package com.dormfix.dto;

import java.time.LocalDateTime;

import com.dormfix.entity.ComplaintStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private ComplaintStatus status;
    private String studentName;
    private String studentEmail;
    private String hostelBlock;
    private String roomNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
