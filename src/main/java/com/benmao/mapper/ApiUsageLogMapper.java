package com.benmao.mapper;

import com.benmao.pojo.ApiUsageLog;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ApiUsageLogMapper {

    // 查询指定账号在 startTime 之后、状态码非 200 的接口调用日志（按时间倒序）
    @Select("SELECT log_id, trace_id, account_id, http_status, error_msg, call_time " +
            "FROM api_usage_log " +
            "WHERE account_id = #{accountId} " +
            "AND http_status != 200 " +
            "AND call_time >= #{startTime} " +
            "ORDER BY call_time DESC")
    List<ApiUsageLog> selectErrorLogs(@Param("accountId") Integer accountId,
                                      @Param("startTime") LocalDateTime startTime);

    // 插入一条接口调用日志记录，与ApiLicenseMapper中addQuota并发调用，用于补偿机制和日志记录共同执行
    @Options(useGeneratedKeys = true, keyProperty = "logId")
    @Insert("INSERT INTO api_usage_log (trace_id, account_id, http_status, error_msg, call_time) " +
            "VALUES (#{traceId}, #{accountId}, #{httpStatus}, #{errorMsg}, #{callTime})")
    int insertLog(ApiUsageLog log);

    // 提取核销/补偿记录防线,防止薅羊毛
    @Select("SELECT * FROM api_usage_log WHERE account_id = #{accountId} AND call_time >= #{startTime} AND http_status = 201")
    List<ApiUsageLog> selectCompensationLogs(@Param("accountId") Integer accountId, @Param("startTime") LocalDateTime startTime);

}