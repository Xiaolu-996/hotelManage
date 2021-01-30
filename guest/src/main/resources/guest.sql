/*
 Navicat Premium Data Transfer

 Source Server         : MySQL 5
 Source Server Type    : MySQL
 Source Server Version : 50650
 Source Host           : localhost:3309
 Source Schema         : guest

 Target Server Type    : MySQL
 Target Server Version : 50650
 File Encoding         : 65001

 Date: 30/01/2021 14:45:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for background
-- ----------------------------
DROP TABLE IF EXISTS `background`;
CREATE TABLE `background`  (
  `back_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '后台管理的工号',
  `password` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '后台登录密码',
  PRIMARY KEY (`back_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of background
-- ----------------------------
INSERT INTO `background` VALUES ('admin', '981028');

-- ----------------------------
-- Table structure for book_msg
-- ----------------------------
DROP TABLE IF EXISTS `book_msg`;
CREATE TABLE `book_msg`  (
  `id` int(50) NOT NULL AUTO_INCREMENT COMMENT '预定信息id',
  `guest_id_card` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户的身份证号',
  `from_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '预计入住时间',
  `to_time` timestamp(0) NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '预计退房时间',
  `rank` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '预定的级别，分A,B,C,D级，级别一次降低',
  `state` int(10) NOT NULL DEFAULT 0 COMMENT '预定处理状态，0代表未处理，1代表已处理,11代表已入住(失效)',
  `result_room` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预定到的房间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `type_id`(`rank`) USING BTREE,
  INDEX `guest_id_card`(`guest_id_card`) USING BTREE,
  INDEX `result_room`(`result_room`) USING BTREE,
  CONSTRAINT `book_msg_ibfk_1` FOREIGN KEY (`guest_id_card`) REFERENCES `guest` (`id_card`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of book_msg
-- ----------------------------
INSERT INTO `book_msg` VALUES (15, '1', '2021-01-12 12:00:00', '2021-01-21 12:00:00', 'B', 1, '0001');
INSERT INTO `book_msg` VALUES (16, '3', '2021-01-11 12:00:00', '2021-01-22 12:00:00', 'B', 11, '0002');

-- ----------------------------
-- Table structure for check_in
-- ----------------------------
DROP TABLE IF EXISTS `check_in`;
CREATE TABLE `check_in`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '入住情况的id',
  `guest_id_card` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户的身份证号',
  `room_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '房间号',
  `from_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入住日期',
  `to_time` timestamp(0) NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '预计退房时间',
  `state` int(10) NOT NULL COMMENT '状态，0代表已退房，1代表正在入住',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `guest_id_card`(`guest_id_card`) USING BTREE,
  INDEX `room_id`(`room_id`) USING BTREE,
  CONSTRAINT `check_in_ibfk_1` FOREIGN KEY (`guest_id_card`) REFERENCES `guest` (`id_card`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `check_in_ibfk_2` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of check_in
-- ----------------------------
INSERT INTO `check_in` VALUES (16, '2', '0003', '2021-01-11 12:00:00', '2021-01-13 12:00:00', 1);
INSERT INTO `check_in` VALUES (17, '3', '0002', '2021-01-11 12:00:00', '2021-01-22 12:00:00', 1);

-- ----------------------------
-- Table structure for cost
-- ----------------------------
DROP TABLE IF EXISTS `cost`;
CREATE TABLE `cost`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '消费信息的id',
  `cost_type_id` int(20) NOT NULL COMMENT '消费的名称',
  `room_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '房间的id',
  `num` int(10) NOT NULL DEFAULT 1 COMMENT '数量',
  `state` int(20) NOT NULL DEFAULT 0 COMMENT '状态，0表示未结算，1表示已结算，11表示该次入住的用户全部结算',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `cost_type_id`(`cost_type_id`) USING BTREE,
  INDEX `room_id`(`room_id`) USING BTREE,
  CONSTRAINT `cost_ibfk_2` FOREIGN KEY (`cost_type_id`) REFERENCES `cost_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cost_ibfk_3` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of cost
-- ----------------------------
INSERT INTO `cost` VALUES (3, 7, '0003', 1, 11);
INSERT INTO `cost` VALUES (4, 8, '0003', 3, 11);
INSERT INTO `cost` VALUES (5, 9, '0004', 1, 11);
INSERT INTO `cost` VALUES (6, 10, '0004', 1, 11);
INSERT INTO `cost` VALUES (7, 15, '0004', 1, 11);
INSERT INTO `cost` VALUES (8, 15, '0004', 1, 11);
INSERT INTO `cost` VALUES (10, 13, '0005', 1, 11);
INSERT INTO `cost` VALUES (11, 14, '0005', 1, 11);
INSERT INTO `cost` VALUES (12, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (13, 16, '0001', 1, 11);
INSERT INTO `cost` VALUES (14, 15, '0005', 1, 11);
INSERT INTO `cost` VALUES (15, 16, '0005', 1, 11);
INSERT INTO `cost` VALUES (16, 16, '0001', 1, 11);
INSERT INTO `cost` VALUES (17, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (18, 7, '0003', 1, 11);
INSERT INTO `cost` VALUES (19, 8, '0003', 3, 11);
INSERT INTO `cost` VALUES (20, 16, '0003', 1, 11);
INSERT INTO `cost` VALUES (21, 15, '0003', 1, 11);
INSERT INTO `cost` VALUES (22, 3, '0001', 1, 11);
INSERT INTO `cost` VALUES (23, 4, '0001', 16, 11);
INSERT INTO `cost` VALUES (24, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (25, 16, '0001', 1, 11);
INSERT INTO `cost` VALUES (26, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (27, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (28, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (29, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (30, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (31, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (32, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (33, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (34, 16, '0001', 1, 11);
INSERT INTO `cost` VALUES (35, 16, '0001', 1, 11);
INSERT INTO `cost` VALUES (36, 16, '0001', 1, 11);
INSERT INTO `cost` VALUES (37, 16, '0001', 1, 11);
INSERT INTO `cost` VALUES (38, 16, '0001', 1, 11);
INSERT INTO `cost` VALUES (39, 16, '0001', 1, 11);
INSERT INTO `cost` VALUES (40, 16, '0001', 1, 11);
INSERT INTO `cost` VALUES (41, 9, '0004', 1, 11);
INSERT INTO `cost` VALUES (42, 10, '0004', 18, 11);
INSERT INTO `cost` VALUES (43, 3, '0001', 1, 11);
INSERT INTO `cost` VALUES (44, 4, '0001', 11, 11);
INSERT INTO `cost` VALUES (45, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (46, 16, '0001', 1, 11);
INSERT INTO `cost` VALUES (47, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (48, 16, '0001', 1, 11);
INSERT INTO `cost` VALUES (49, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (50, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (51, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (52, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (53, 15, '0001', 1, 11);
INSERT INTO `cost` VALUES (54, 3, '0001', 1, 11);
INSERT INTO `cost` VALUES (55, 4, '0001', 2, 11);
INSERT INTO `cost` VALUES (56, 3, '0001', 1, 11);
INSERT INTO `cost` VALUES (57, 4, '0001', 2, 11);
INSERT INTO `cost` VALUES (58, 3, '0001', 1, 11);
INSERT INTO `cost` VALUES (59, 4, '0001', 2, 11);
INSERT INTO `cost` VALUES (60, 3, '0001', 1, 11);
INSERT INTO `cost` VALUES (61, 4, '0001', 2, 11);
INSERT INTO `cost` VALUES (62, 5, '0002', 1, 0);
INSERT INTO `cost` VALUES (63, 6, '0002', 1, 1);
INSERT INTO `cost` VALUES (64, 7, '0003', 1, 0);
INSERT INTO `cost` VALUES (65, 8, '0003', 1, 1);
INSERT INTO `cost` VALUES (66, 3, '0001', 1, 0);
INSERT INTO `cost` VALUES (67, 4, '0001', 1, 1);
INSERT INTO `cost` VALUES (68, 7, '0003', 1, 0);
INSERT INTO `cost` VALUES (69, 8, '0003', 2, 1);
INSERT INTO `cost` VALUES (70, 5, '0002', 1, 0);
INSERT INTO `cost` VALUES (71, 6, '0002', 11, 1);

-- ----------------------------
-- Table structure for cost_type
-- ----------------------------
DROP TABLE IF EXISTS `cost_type`;
CREATE TABLE `cost_type`  (
  `id` int(50) NOT NULL AUTO_INCREMENT COMMENT '消费类型的id',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消费项目的名称',
  `money` float NOT NULL COMMENT '金额',
  PRIMARY KEY (`id`, `name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of cost_type
-- ----------------------------
INSERT INTO `cost_type` VALUES (3, '0001房间定金', -200);
INSERT INTO `cost_type` VALUES (4, '0001房间租金', 300);
INSERT INTO `cost_type` VALUES (5, '0002房间定金', -200);
INSERT INTO `cost_type` VALUES (6, '0002房间租金', 500);
INSERT INTO `cost_type` VALUES (7, '0003房间定金', -200);
INSERT INTO `cost_type` VALUES (8, '0003房间租金', 500);
INSERT INTO `cost_type` VALUES (9, '0004房间定金', -100);
INSERT INTO `cost_type` VALUES (10, '0004房间租金', 500);
INSERT INTO `cost_type` VALUES (13, '0005房间定金', 520);
INSERT INTO `cost_type` VALUES (14, '0005房间租金', 501);
INSERT INTO `cost_type` VALUES (15, '早餐', 50);
INSERT INTO `cost_type` VALUES (16, '晚餐', 50);
INSERT INTO `cost_type` VALUES (35, '0006房间定金', -2);
INSERT INTO `cost_type` VALUES (36, '0006房间租金', 2);
INSERT INTO `cost_type` VALUES (37, '1房间定金', -22);
INSERT INTO `cost_type` VALUES (38, '1房间租金', 22);

-- ----------------------------
-- Table structure for front
-- ----------------------------
DROP TABLE IF EXISTS `front`;
CREATE TABLE `front`  (
  `front_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '前台管理员的工号',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前台管理员的姓名',
  `password` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '前台管理的登录密码',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前台管理员的电话',
  PRIMARY KEY (`front_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of front
-- ----------------------------
INSERT INTO `front` VALUES ('0001', '张雪萍', '123', '13361374723');
INSERT INTO `front` VALUES ('0002', '连宇豪', '981028', '17360031807');
INSERT INTO `front` VALUES ('0003', 'lujianbin', '981028', '18827099738');

-- ----------------------------
-- Table structure for guest
-- ----------------------------
DROP TABLE IF EXISTS `guest`;
CREATE TABLE `guest`  (
  `id_card` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户的身份证号',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户的姓名',
  `contact` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户的联系方式',
  PRIMARY KEY (`id_card`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of guest
-- ----------------------------
INSERT INTO `guest` VALUES ('1', '1', '1');
INSERT INTO `guest` VALUES ('2', '2', '2');
INSERT INTO `guest` VALUES ('3', '3', '3');

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `room_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '房间编号',
  `size` double NOT NULL COMMENT '房间大小，以平方米为单位',
  `rank` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '级别，分A,B,C,D级，级别一次降低',
  `rent` double NOT NULL COMMENT '租金，单位是人民币元',
  `earnest` double NOT NULL COMMENT '入住定金，单位是人民币元',
  `max_num` int(5) NOT NULL COMMENT '最大人数',
  `position` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地理位置',
  PRIMARY KEY (`room_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of room
-- ----------------------------
INSERT INTO `room` VALUES ('0001', 53, 'B', 300, 200, 3, '撒擦擦');
INSERT INTO `room` VALUES ('0002', 53.2, 'B', 500, 200, 2, '撒擦擦');
INSERT INTO `room` VALUES ('0003', 53.2, 'A', 500, 200, 2, '撒擦擦');
INSERT INTO `room` VALUES ('0004', 53.2, 'A', 501, 100, 2, '撒擦擦');
INSERT INTO `room` VALUES ('0005', 53.2, 'A', 520, 100, 1, '撒擦擦');
INSERT INTO `room` VALUES ('0006', 2, 'A', 2, 2, 2, '2');
INSERT INTO `room` VALUES ('1', 22, 'C', 22, 22, 22, '二楼');

SET FOREIGN_KEY_CHECKS = 1;
