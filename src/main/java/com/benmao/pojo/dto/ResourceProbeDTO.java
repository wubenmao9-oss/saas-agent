package com.benmao.pojo.dto;

import lombok.Data;

// 用于两个表的连表查询
@Data
public class ResourceProbeDTO {
    private Integer status;
    private String planType;
    private Integer totalQuota;
    private Integer usedQuota;
}
