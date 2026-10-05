package com.benmao.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountInfo {
    private Integer accountId;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
