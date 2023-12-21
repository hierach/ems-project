/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80030 (8.0.30)
 Source Host           : localhost:3306
 Source Schema         : ems

 Target Server Type    : MySQL
 Target Server Version : 80030 (8.0.30)
 File Encoding         : 65001

 Date: 21/12/2023 10:51:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int NOT NULL COMMENT '主键',
  `admin_num` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员号',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名字',
  `password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'A001', '超级管理员', '123');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `course_num` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程id',
  `course_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程名',
  `start_time` datetime NOT NULL COMMENT '开课时间',
  `end_time` datetime NOT NULL COMMENT '结课时间',
  `full_mark` int NOT NULL COMMENT '满分',
  `score` int NOT NULL COMMENT '学分',
  `sel_people` int UNSIGNED NULL DEFAULT 0 COMMENT '选课人数',
  `max_people` int NULL DEFAULT NULL COMMENT '最大选课人数',
  `place` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上课地点',
  `sel_week` int NULL DEFAULT NULL COMMENT '上课星期几',
  `sel_start` int NULL DEFAULT NULL COMMENT '第几节课开始上课(默认有1-13节课)',
  `sel_end` int NULL DEFAULT NULL COMMENT '第几节课下课',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (1, 'C001', '软件工程', '2023-09-01 10:00:38', '2024-01-31 10:00:52', 100, 2, 2, 100, '博北B205', 1, 3, 4);
INSERT INTO `course` VALUES (2, 'C002', '操作系统', '2023-09-01 00:00:00', '2024-01-01 00:00:00', 100, 2, 1, 200, '博学北楼A101', 2, 5, 8);
INSERT INTO `course` VALUES (4, 'C003', '计算机接口技术', '2023-09-01 00:00:00', '2024-01-01 00:00:00', 100, 2, 1, 100, '博北B201', 3, 5, 7);

-- ----------------------------
-- Table structure for cst
-- ----------------------------
DROP TABLE IF EXISTS `cst`;
CREATE TABLE `cst`  (
  `course_num` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `stu_num` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `tech_num` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `mark` int NULL DEFAULT -1 COMMENT '分数，没打分的时候默认为-1',
  `leave_start` datetime NULL DEFAULT NULL COMMENT '请假开始时间',
  `leave_end` datetime NULL DEFAULT NULL COMMENT '请假结束时间',
  `leave_status` int NULL DEFAULT 0 COMMENT '请假状态。0表示未申请，1表示申请了请假等待状态，-1表示不同意请假要求，2表示申请通过'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cst
-- ----------------------------
INSERT INTO `cst` VALUES ('C002', 'E001', 'T001', 90, NULL, NULL, 0);
INSERT INTO `cst` VALUES ('C001', 'E001', 'T001', -1, '2023-12-06 00:00:00', '2023-12-30 00:00:00', 1);
INSERT INTO `cst` VALUES ('C003', 'E001', 'T001', -1, NULL, NULL, 0);

-- ----------------------------
-- Table structure for open
-- ----------------------------
DROP TABLE IF EXISTS `open`;
CREATE TABLE `open`  (
  `admin_num` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `course_num` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tech_num` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `course_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL COMMENT '批准状态。0表示申请状态，1表示批准，-1表示不批准',
  `score` int NULL DEFAULT NULL,
  `full_mark` int NULL DEFAULT NULL,
  `max_people` int NULL DEFAULT NULL COMMENT '选课最大人数',
  `start_time` datetime NULL DEFAULT NULL,
  `end_time` datetime NULL DEFAULT NULL,
  `place` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地点',
  `sel_week` int NULL DEFAULT NULL COMMENT '选择星期几上课',
  `sel_start` int NULL DEFAULT NULL COMMENT '上课开始时间(1-13可选)',
  `sel_end` int NULL DEFAULT NULL COMMENT '下课时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of open
-- ----------------------------
INSERT INTO `open` VALUES ('A001', 'C001', 'T001', '软件工程', 1, 2, 100, 100, '2023-09-01 00:10:38', '2024-01-31 10:00:52', '博北B205', 1, 3, 4);
INSERT INTO `open` VALUES ('A001', 'C002', 'T001', '操作系统', 1, 2, 100, 200, '2023-09-01 00:00:00', '2024-01-01 00:00:00', '博学北楼A101', 2, 5, 8);
INSERT INTO `open` VALUES ('A001', 'C003', 'T001', '计算机接口技术', 1, 2, 100, 100, '2023-09-01 00:00:00', '2024-01-01 00:00:00', '博北B201', 3, 5, 7);
INSERT INTO `open` VALUES ('A001', 'C004', 'T001', '计算机网络', 0, 2, 100, 200, '2023-10-01 00:00:00', '2024-01-17 00:00:00', '博北B303', 5, 1, 3);

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `stu_num` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号',
  `password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `type` int UNSIGNED NOT NULL DEFAULT 2 COMMENT '类型。1表示管理员，2学生，3教师',
  `sex` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '性别',
  `year` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '年级',
  `major` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专业',
  `need_score` int UNSIGNED NULL DEFAULT 15 COMMENT '所需学分。默认15个学分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, 'E001', '123', '秦明', 2, '男', '3', '计算机科学', 15);

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tech_num` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '教师号',
  `password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `type` int UNSIGNED NOT NULL DEFAULT 3 COMMENT '类型。默认为3',
  `sex` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '性别',
  `faculty` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学院',
  `job_title` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (1, 'T001', '123', '李明', 3, '男', '计算机学院', '教授');

SET FOREIGN_KEY_CHECKS = 1;
