package com.dormfix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsResponse {

    private long total;
    private long pending;
    private long inProgress;
    private long resolved;
}
