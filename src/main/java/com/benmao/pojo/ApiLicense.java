package com.benmao.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiLicense {
    private Integer licenseId;
    private Integer accountId;
    private String planType;
    private Integer totalQuota;
    private Integer usedQuota;
    private LocalDateTime lastUpdateTime;
}
