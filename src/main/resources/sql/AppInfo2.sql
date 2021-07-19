-- 创建数据库
create database IF NOT EXISTS `locationservice` default character set utf8mb4 collate utf8mb4_general_ci;

use locationservice;
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nameZh` varchar(11) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(11) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '管理员', 'ROLE_ADMIN');
INSERT INTO `role` VALUES ('2', '普通用户', 'ROLE_USER');

-- ----------------------------
-- Table structure for `role_resource`
-- 配置在这个表里的接口必须在http请求的header里加上token
-- ----------------------------
DROP TABLE IF EXISTS `role_resource`;
CREATE TABLE `role_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `resource_uri` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_resource_unique` (`role_id`,`resource_uri`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of role_resource
-- ----------------------------
-- 超级用户设备
INSERT INTO `role_resource` VALUES ('1', '1', '/location/getDeviceList');
INSERT INTO `role_resource` VALUES ('2', '1', '/location/saveUserDevice');
INSERT INTO `role_resource` VALUES ('3', '1', '/location/deleteUserDevice');
INSERT INTO `role_resource` VALUES ('4', '1', '/location/addDevice');
INSERT INTO `role_resource` VALUES ('5', '1', '/location/deleteDevice');
-- 超级用户用户
INSERT INTO `role_resource` VALUES ('6', '1', '/location/getAllUsers');
INSERT INTO `role_resource` VALUES ('7', '1', '/location/addUser');
INSERT INTO `role_resource` VALUES ('8', '1', '/location/deleteUser');
-- 超级用户位置信息
INSERT INTO `role_resource` VALUES ('9', '1', '/location/getLocation');
INSERT INTO `role_resource` VALUES ('10', '1', '/location/getLocationLast');
INSERT INTO `role_resource` VALUES ('11', '1', '/location/getLocationAll');
INSERT INTO `role_resource` VALUES ('12', '1', '/location/getLocationByTime');
-- 普通用户设备
INSERT INTO `role_resource` VALUES ('13', '2', '/location/getDeviceList');
-- 普通用户位置信息
INSERT INTO `role_resource` VALUES ('14', '2', '/location/getLocationByTime');


-- ----------------------------
-- Table structure for `user_role`
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_role_unique` (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1', '1');
INSERT INTO `user_role` VALUES ('2', '2', '2');
INSERT INTO `user_role` VALUES ('3', '3', '2');
INSERT INTO `user_role` VALUES ('4', '4', '2');

