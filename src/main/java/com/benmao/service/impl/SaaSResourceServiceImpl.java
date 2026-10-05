package com.benmao.service.impl;

import com.benmao.mapper.ApiLicenseMapper;
import com.benmao.mapper.ApiUsageLogMapper;
import com.benmao.pojo.ApiUsageLog;
import com.benmao.service.SaaSResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SaaSResourceServiceImpl implements SaaSResourceService {
    private static final int MAX_COMPENSATION_AMOUNT = 1000;

    @Autowired
    private ApiLicenseMapper apiLicenseMapper;
    @Autowired
    private ApiUsageLogMapper apiUsageLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean userQuotaCompensate(Integer accountId, Integer amount) {
        // 系统检查amount
        if (accountId == null || accountId <= 0) {
            throw new IllegalArgumentException("非法参数：accountId 必须为正整数");
        }
        if (amount == null || amount <= 0 || amount > MAX_COMPENSATION_AMOUNT) {
            throw new IllegalArgumentException("非法参数：补偿数额必须在 1 ~ " + MAX_COMPENSATION_AMOUNT + " 之间");
        }

        int updateRows = apiLicenseMapper.addQuota(accountId, amount);
        if (updateRows == 0) {
            // 没找到对应的许可记录，直接抛异常，触发事务回滚
            throw new RuntimeException("补偿失败：account_id=" + accountId + " 不存在对应的许可证记录");
        }

        ApiUsageLog log = new ApiUsageLog();
        // 生成 traceId，并塞入日志
        log.setTraceId(UUID.randomUUID().toString().replace("-", ""));
        log.setAccountId(accountId);
        log.setHttpStatus(201); // 201 作为“补偿成功”标识
        log.setErrorMsg("大模型裁定故障，自动补偿配额：" + amount);
        log.setCallTime(LocalDateTime.now());

        int insertRows = apiUsageLogMapper.insertLog(log);
        if (insertRows == 0) {
            throw new RuntimeException("补偿日志写入失败，配额变更作废");
        }
        return true;
    }


    @Override
    public void getUsageLog(Integer accountId, Integer hours) {
        if (accountId == null || accountId <= 0 || hours == null || hours <= 0) {
            throw new IllegalArgumentException("非法参数传入：accountId 和 hours 必须为正整数");
        }

        LocalDateTime localDateTime = LocalDateTime.now().minusHours(hours);

        List<ApiUsageLog> errorLogs = apiUsageLogMapper.selectErrorLogs(accountId, localDateTime);
        if (errorLogs == null || errorLogs.isEmpty()) {
            throw new RuntimeException("未查询到该账户近期有报错记录，不符合补偿条件");
        }

        // 看看有没有之前补偿过，防止薅羊毛
        List<ApiUsageLog> compensationLogs = apiUsageLogMapper.selectCompensationLogs(accountId, localDateTime);

        // 状态机互斥拦截
        if (compensationLogs != null && !compensationLogs.isEmpty()) {
            throw new RuntimeException("该账户在近期已获取过故障补偿，系统处于赔付冷却期，拒绝重复发放");
        }
    }
}
