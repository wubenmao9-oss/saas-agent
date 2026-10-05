package com.benmao.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiUsageLog {
    private Long logId;
    private String traceId;
    private Integer accountId;
    private Integer httpStatus;
    private String errorMsg;
    private LocalDateTime callTime;
}
