package com.benmao.service;

import com.benmao.pojo.ApiUsageLog;

import java.time.LocalDateTime;
import java.util.List;

public interface SaaSResourceService {
    // 资源补偿调度事务
    public boolean userQuotaCompensate(Integer accountId, Integer amount);

    // 提取报错日志
    public void getUsageLog(Integer accountId, Integer hours);
}
