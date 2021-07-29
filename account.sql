/*
 Navicat Premium Data Transfer

 Source Server         : 172.16.62.216
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 172.16.62.216:3306
 Source Schema         : account

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 29/07/2021 09:52:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

create database account;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `account` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of account
-- ----------------------------
BEGIN;
INSERT INTO `account` VALUES ('111', 'zhangsan', '11110101010');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
