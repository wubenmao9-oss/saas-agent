package com.benmao.saasagent;

import com.benmao.mapper.AccountInfoMapper;
import com.benmao.mapper.ApiUsageLogMapper;
import com.benmao.pojo.ApiUsageLog;
import com.benmao.pojo.dto.ResourceProbeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

// 声明这是一个 Spring Boot 测试类
@SpringBootTest
public class Test1 {

    // 依赖注入 Mapper
    @Autowired
    private AccountInfoMapper accountInfoMapper;
    @Autowired
    private ApiUsageLogMapper apiUsageLogMapper;

    @Test //测试无问题
    public void testGetStatus() {
        Integer accountId = 1;
        // 建议把变量名从 status 改成 result，因为它包含 status 和其他配额信息，叫 status 容易误导
        //Map<String, Object> result = accountInfoMapper.getResourceProbe(accountId);
        ResourceProbeDTO result = accountInfoMapper.getResourceProbe(accountId);

        // 直接打印整个 Map（会输出类似 {status=1, planType=VIP, totalQuota=1000, usedQuota=200}）
        System.out.println("查询结果 : " + result);
    }

    @Test
    public void testSelectErrorLogs() {
        Integer accountId = 1;
        LocalDateTime startTime = LocalDateTime.now().minusDays(3);

        System.out.println("开始查询 account_id = " + accountId + " 且在 " + startTime + " 之后的错误日志...");
        List<ApiUsageLog> errorLogs = apiUsageLogMapper.selectErrorLogs(accountId, startTime);

        if (errorLogs != null && !errorLogs.isEmpty()) {
            System.out.println("共查询到 " + errorLogs.size() + " 条错误记录：");
            for (ApiUsageLog log : errorLogs) {
                System.out.println("日志ID: " + log.getLogId() +
                        ", 追踪ID: " + log.getTraceId() +
                        ", 状态码: " + log.getHttpStatus() +
                        " 错误信息: " + log.getErrorMsg() +
                        ", 时间: " + log.getCallTime());
            }
        } else {
            System.out.println("未查询到符合条件的错误记录。请检查数据库是否有数据，或时间条件是否合适。");
        }
    }
}