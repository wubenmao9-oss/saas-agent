package com.benmao.mapper;

import com.benmao.pojo.dto.ResourceProbeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountInfoMapper {

    // 查询指定账号的状态、套餐类型和额度使用情况（取最新一条许可记录）
    @Select("SELECT info.status, " +
            "license.plan_type AS planType, " +
            "license.total_quota AS totalQuota, " +
            "license.used_quota AS usedQuota " +
            "FROM account_info info " +
            "LEFT JOIN api_license license ON info.account_id = license.account_id " +
            "WHERE info.account_id = #{accountId} " +
            "ORDER BY license.license_id DESC LIMIT 1")

    // 不使用Map<K,V>的方式返回，而是封装成一个收纳类来返回
    ResourceProbeDTO getResourceProbe(@Param("accountId") Integer accountId);
}
