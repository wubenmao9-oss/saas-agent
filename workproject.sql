/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50540
 Source Host           : localhost:3306
 Source Schema         : workproject

 Target Server Type    : MySQL
 Target Server Version : 50540
 File Encoding         : 65001

 Date: 22/09/2026 19:37:51
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account_info
-- ----------------------------
DROP TABLE IF EXISTS `account_info`;
CREATE TABLE `account_info`
(
    `main_key`    int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `status`      tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态：0 或 1',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`main_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for api_license
-- ----------------------------
DROP TABLE IF EXISTS `api_license`;
CREATE TABLE `api_license`
(
    `main_key`    int(255) NOT NULL,
    `user_id`     int(11) NOT NULL,
    `user_type`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `total_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `used_token`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `last_time`   datetime NULL,
    PRIMARY KEY (`main_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for api_usage_log
-- ----------------------------
DROP TABLE IF EXISTS `api_usage_log`;
CREATE TABLE `api_usage_log`
(
    `main_key`    int(255) NOT NULL,
    `trace_id`    int(255) NOT NULL,
    `user_id`     int(255) NOT NULL,
    `http_status` int(255) NOT NULL,
    `error_msg`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `call_time`   datetime NULL,
    PRIMARY KEY (`main_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

SET
FOREIGN_KEY_CHECKS = 1;







-- 清理旧数据防止主键冲突（谨慎在生产环境使用）
TRUNCATE TABLE `api_usage_log`;
TRUNCATE TABLE `api_license`;
TRUNCATE TABLE `account_info`;

-- 1. 账户基础数据：构建正常与异常状态的变量
INSERT INTO `account_info` (`account_id`, `status`, `create_time`)
VALUES (1, 1, DATE_SUB(NOW(), INTERVAL 30 DAY)), -- 正常活跃账户（主测对象）
       (2, 1, DATE_SUB(NOW(), INTERVAL 60 DAY)), -- 正常活跃账户（对照组：无报错记录）
       (3, 0, DATE_SUB(NOW(), INTERVAL 10 DAY));
-- 冻结账户（边界情况）

-- 2. 授权资产数据：构建不同的业务套餐与额度余量
INSERT INTO `api_license` (`account_id`, `plan_type`, `total_quota`, `used_quota`)
VALUES (1, 'pro', 5000, 4800),           -- 账户1：Pro套餐，额度即将耗尽
       (2, 'enterprise', 100000, 50000), -- 账户2：企业套餐，额度充裕
       (3, 'free', 100, 100);
-- 账户3：免费套餐，额度已耗尽

-- 3. 流水监控数据：构建大模型探针核实所需的时间与状态变量
-- 账户1：最近2小时内发生多次500/429错误（探针预期返回：符合补偿条件）
INSERT INTO `api_usage_log` (`trace_id`, `account_id`, `http_status`, `error_msg`, `call_time`)
VALUES (REPLACE(UUID(), '-', ''), 1, 500, 'Internal Server Error: NullPointerException',
        DATE_SUB(NOW(), INTERVAL 1 HOUR)),
       (REPLACE(UUID(), '-', ''), 1, 429, 'Too Many Requests: Rate limit exceeded', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
       (REPLACE(UUID(), '-', ''), 1, 200, NULL, DATE_SUB(NOW(), INTERVAL 3 HOUR));

-- 账户2：近期调用全为200成功，无报错（探针预期返回：异常阻断，拒绝补偿）
INSERT INTO `api_usage_log` (`trace_id`, `account_id`, `http_status`, `error_msg`, `call_time`)
VALUES (REPLACE(UUID(), '-', ''), 2, 200, NULL, DATE_SUB(NOW(), INTERVAL 5 HOUR)),
       (REPLACE(UUID(), '-', ''), 2, 200, NULL, DATE_SUB(NOW(), INTERVAL 10 HOUR));

-- 账户3：报错记录发生在72小时前（测试Agent的时间范围回溯能力）
INSERT INTO `api_usage_log` (`trace_id`, `account_id`, `http_status`, `error_msg`, `call_time`)
VALUES (REPLACE(UUID(), '-', ''), 3, 500, 'Database Connection Timeout', DATE_SUB(NOW(), INTERVAL 72 HOUR));