package com.dormfix.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dormfix.dto.ComplaintResponse;
import com.dormfix.dto.StatsResponse;
import com.dormfix.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // GET /admin/complaints — get all complaints
    // Optional filters: ?status=PENDING or ?hostelBlock=Block A
    @GetMapping("/complaints")
    public ResponseEntity<List<ComplaintResponse>> getAllComplaints(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String hostelBlock) {
        return ResponseEntity.ok(
                adminService.getAllComplaints(status, hostelBlock));
    }

    // GET /admin/complaints/{id} — get single complaint detail
    @GetMapping("/complaints/{id}")
    public ResponseEntity<ComplaintResponse> getComplaintById(
            @PathVariable Long id) {
        return ResponseEntity.ok(adminService.getComplaintById(id));
    }

    // PATCH /admin/complaints/{id}/status — update complaint status
    @PatchMapping("/complaints/{id}/status")
    public ResponseEntity<ComplaintResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(adminService.updateStatus(id, status));
    }

    // GET /admin/stats — dashboard stat cards
    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }
}
