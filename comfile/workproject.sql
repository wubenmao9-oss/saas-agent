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
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account_info
-- ----------------------------
DROP TABLE IF EXISTS `account_info`;
CREATE TABLE `account_info`  (
  `main_key` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态：0 或 1',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`main_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for api_license
-- ----------------------------
DROP TABLE IF EXISTS `api_license`;
CREATE TABLE `api_license`  (
  `main_key` int(255) NOT NULL,
  `user_id` int(11) NOT NULL,
  `user_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `total_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `used_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `last_time` datetime NULL,
  PRIMARY KEY (`main_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for api_usage_log
-- ----------------------------
DROP TABLE IF EXISTS `api_usage_log`;
CREATE TABLE `api_usage_log`  (
  `main_key` int(255) NOT NULL,
  `trace_id` int(255) NOT NULL,
  `user_id` int(255) NOT NULL,
  `http_status` int(255) NOT NULL,
  `error_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `call_time` datetime NULL,
  PRIMARY KEY (`main_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
