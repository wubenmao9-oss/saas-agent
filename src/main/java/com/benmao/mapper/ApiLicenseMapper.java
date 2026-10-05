package com.benmao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ApiLicenseMapper {
    // 原子累加操作，避免并发问题，用于用户额度补偿机制中
    @Update("UPDATE api_license SET total_quota = total_quota + #{amount}, last_update_time = NOW() WHERE account_id = #{accountId}")
    int addQuota(@Param("accountId") Integer accountId, @Param("amount") Integer amount);
}