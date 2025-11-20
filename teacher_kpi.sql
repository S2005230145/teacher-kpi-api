/*
 Navicat Premium Dump SQL

 Source Server         : LocalConnection
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : teacher_kpi

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 20/11/2025 13:46:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cp_group
-- ----------------------------
DROP TABLE IF EXISTS `cp_group`;
CREATE TABLE `cp_group`  (
  `id` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_admin` tinyint(1) NOT NULL DEFAULT 0,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cp_group
-- ----------------------------

-- ----------------------------
-- Table structure for cp_group_action
-- ----------------------------
DROP TABLE IF EXISTS `cp_group_action`;
CREATE TABLE `cp_group_action`  (
  `id` int NOT NULL,
  `group_id` int NOT NULL,
  `system_action_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cp_group_action
-- ----------------------------

-- ----------------------------
-- Table structure for cp_group_menu
-- ----------------------------
DROP TABLE IF EXISTS `cp_group_menu`;
CREATE TABLE `cp_group_menu`  (
  `id` int NOT NULL,
  `menu_id` int NOT NULL,
  `group_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cp_group_menu
-- ----------------------------

-- ----------------------------
-- Table structure for cp_group_user
-- ----------------------------
DROP TABLE IF EXISTS `cp_group_user`;
CREATE TABLE `cp_group_user`  (
  `id` bigint NOT NULL,
  `group_id` int NOT NULL,
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `member_id` bigint NOT NULL,
  `realname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cp_group_user
-- ----------------------------

-- ----------------------------
-- Table structure for cp_log
-- ----------------------------
DROP TABLE IF EXISTS `cp_log`;
CREATE TABLE `cp_log`  (
  `log_id` bigint NOT NULL,
  `log_unique` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `log_sym_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `log_mer_id` int NOT NULL,
  `log_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `log_created` bigint NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cp_log
-- ----------------------------

-- ----------------------------
-- Table structure for cp_member
-- ----------------------------
DROP TABLE IF EXISTS `cp_member`;
CREATE TABLE `cp_member`  (
  `id` bigint NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `realname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  `last_time` bigint NOT NULL,
  `last_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `org_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pinyin_abbr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `bg_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `area_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cp_member
-- ----------------------------

-- ----------------------------
-- Table structure for cp_menu
-- ----------------------------
DROP TABLE IF EXISTS `cp_menu`;
CREATE TABLE `cp_menu`  (
  `id` int NOT NULL,
  `sort` int NOT NULL,
  `parent_id` int NOT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `hidden` tinyint(1) NOT NULL DEFAULT 0,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `no_cache` tinyint(1) NOT NULL DEFAULT 0,
  `relative_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `active_menu` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cp_menu
-- ----------------------------

-- ----------------------------
-- Table structure for cp_system_action
-- ----------------------------
DROP TABLE IF EXISTS `cp_system_action`;
CREATE TABLE `cp_system_action`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `action_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `action_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `module_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `module_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `need_show` tinyint(1) NOT NULL DEFAULT 0,
  `display_order` int NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cp_system_action
-- ----------------------------

-- ----------------------------
-- Table structure for critical_work_assessment
-- ----------------------------
DROP TABLE IF EXISTS `critical_work_assessment`;
CREATE TABLE `critical_work_assessment`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `teacher_id` bigint NULL DEFAULT NULL,
  `assessment_id` bigint NULL DEFAULT NULL,
  `academic_year` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `administrative_positions` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `work_categories` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `specific_duties` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `work_performance` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `work_difficulty_level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `work_importance_level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `work_completion_evaluation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `administrative_work_score` double NULL DEFAULT NULL,
  `temporary_task_types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `task_descriptions` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `task_start_date` datetime NULL DEFAULT NULL,
  `task_end_date` datetime NULL DEFAULT NULL,
  `task_urgency_level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `task_completion_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `task_achievements` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `temporary_tasks_score` double NULL DEFAULT NULL,
  `total_critical_work_score` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of critical_work_assessment
-- ----------------------------

-- ----------------------------
-- Table structure for moral_ethics_evaluation
-- ----------------------------
DROP TABLE IF EXISTS `moral_ethics_evaluation`;
CREATE TABLE `moral_ethics_evaluation`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `teacher_id` bigint NULL DEFAULT NULL,
  `assessment_id` bigint NULL DEFAULT NULL,
  `behavior_standard_compliance` tinyint(1) NULL DEFAULT 0,
  `has_violation_behavior` tinyint(1) NULL DEFAULT 0,
  `has_negative_list_behavior` tinyint(1) NULL DEFAULT 0,
  `evaluation_level_id` int NULL DEFAULT NULL,
  `evaluation_basis` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `evaluator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `evaluate_date` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of moral_ethics_evaluation
-- ----------------------------

-- ----------------------------
-- Table structure for personal_development_assessment
-- ----------------------------
DROP TABLE IF EXISTS `personal_development_assessment`;
CREATE TABLE `personal_development_assessment`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `teacher_id` bigint NULL DEFAULT NULL,
  `assessment_id` bigint NULL DEFAULT NULL,
  `academic_year` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `demonstration_classes_count` int NULL DEFAULT NULL,
  `observation_classes_count` int NULL DEFAULT NULL,
  `special_lectures_count` int NULL DEFAULT NULL,
  `class_observation_activities` int NULL DEFAULT NULL,
  `regional_exchange_activities` int NULL DEFAULT NULL,
  `activity_levels` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `public_teaching_score` double NULL DEFAULT NULL,
  `education_papers_count` int NULL DEFAULT NULL,
  `paper_titles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `paper_types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `award_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `compilation_level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `publish_date` datetime(6) NULL DEFAULT NULL,
  `papers_score` double NULL DEFAULT NULL,
  `research_projects` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `research_levels` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `research_roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `subject_proposition_work` tinyint(1) NULL DEFAULT 0,
  `continuing_education_completion` tinyint(1) NULL DEFAULT 0,
  `continuing_education_hours` int NULL DEFAULT NULL,
  `research_participation_score` double NULL DEFAULT NULL,
  `teaching_special_awards` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `comprehensive_honors` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `teaching_competition_awards` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `excellent_teacher_certification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `award_dates` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `personal_awards_score` double NULL DEFAULT NULL,
  `total_personal_development_score` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of personal_development_assessment
-- ----------------------------

-- ----------------------------
-- Table structure for teaching_achievement_assessment
-- ----------------------------
DROP TABLE IF EXISTS `teaching_achievement_assessment`;
CREATE TABLE `teaching_achievement_assessment`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `teacher_id` bigint NULL DEFAULT NULL,
  `assessment_id` bigint NULL DEFAULT NULL,
  `academic_year` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `student_academic_development` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `exam_types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `average_score` double NULL DEFAULT NULL,
  `pass_rate` double NULL DEFAULT NULL,
  `excellence_rate` double NULL DEFAULT NULL,
  `score_improvement` double NULL DEFAULT NULL,
  `city_ranking` int NULL DEFAULT NULL,
  `school_ranking` int NULL DEFAULT NULL,
  `academic_score` double NULL DEFAULT NULL,
  `mentorship_agreement_fulfillment` tinyint(1) NULL DEFAULT NULL,
  `guidance_effectiveness` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `research_group_work` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `master_studio_participation` tinyint(1) NULL DEFAULT NULL,
  `training_instructor_role` tinyint(1) NULL DEFAULT NULL,
  `rural_teaching_activities` tinyint(1) NULL DEFAULT NULL,
  `demonstration_score` double NULL DEFAULT NULL,
  `class_meeting_awards` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `team_activity_awards` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `single_competition_awards` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `comprehensive_honors` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `award_levels` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `class_awards_score` double NULL DEFAULT NULL,
  `subject_competition_guidance` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `innovation_competition_guidance` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sports_league_guidance` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `art_performance_guidance` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `other_competition_guidance` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `student_awards_levels` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `student_guidance_score` double NULL DEFAULT NULL,
  `school_based_curriculum_development` tinyint(1) NULL DEFAULT NULL,
  `research_learning_guidance` tinyint(1) NULL DEFAULT NULL,
  `comprehensive_practice_organization` tinyint(1) NULL DEFAULT NULL,
  `social_practice_organization` tinyint(1) NULL DEFAULT NULL,
  `club_activities_guidance` tinyint(1) NULL DEFAULT NULL,
  `extracurricular_tutor_role` tinyint(1) NULL DEFAULT NULL,
  `school_activities_score` double NULL DEFAULT NULL,
  `teaching_research_participation` tinyint(1) NULL DEFAULT NULL,
  `collective_preparation_participation` tinyint(1) NULL DEFAULT NULL,
  `resource_sharing` tinyint(1) NULL DEFAULT NULL,
  `post_training_participation` tinyint(1) NULL DEFAULT NULL,
  `training_learning_participation` tinyint(1) NULL DEFAULT NULL,
  `class_observation_completion` tinyint(1) NULL DEFAULT NULL,
  `team_cooperation_score` double NULL DEFAULT NULL,
  `student_satisfaction_rate` double NULL DEFAULT NULL,
  `parent_satisfaction_rate` double NULL DEFAULT NULL,
  `evaluation_participant_count` int NULL DEFAULT NULL,
  `satisfaction_score` double NULL DEFAULT NULL,
  `total_teaching_achievement_score` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teaching_achievement_assessment
-- ----------------------------

-- ----------------------------
-- Table structure for teaching_routine_assessment
-- ----------------------------
DROP TABLE IF EXISTS `teaching_routine_assessment`;
CREATE TABLE `teaching_routine_assessment`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `teacher_id` bigint NULL DEFAULT NULL,
  `assessment_id` bigint NULL DEFAULT NULL,
  `academic_year` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sick_leave_days` int NULL DEFAULT NULL,
  `personal_leave_days` int NULL DEFAULT NULL,
  `late_times` int NULL DEFAULT NULL,
  `absence_times` int NULL DEFAULT NULL,
  `political_study_attendance` tinyint(1) NULL DEFAULT 0,
  `teaching_research_attendance` tinyint(1) NULL DEFAULT 0,
  `school_meeting_attendance` tinyint(1) NULL DEFAULT 0,
  `other_collective_activities_attendance` tinyint(1) NULL DEFAULT 0,
  `attendance_score` double NULL DEFAULT NULL,
  `standard_class_hours` int NULL DEFAULT NULL,
  `actual_class_hours` int NULL DEFAULT NULL,
  `substitution_hours` int NULL DEFAULT NULL,
  `special_class_hours` int NULL DEFAULT NULL,
  `workload_completion_rate` double NULL DEFAULT NULL,
  `workload_score` double NULL DEFAULT NULL,
  `class_teacher_role` tinyint(1) NULL DEFAULT 0,
  `grade_leader_role` tinyint(1) NULL DEFAULT 0,
  `teaching_research_leader_role` tinyint(1) NULL DEFAULT 0,
  `young_pioneer_leader_role` tinyint(1) NULL DEFAULT 0,
  `youth_league_secretary_role` tinyint(1) NULL DEFAULT 0,
  `middle_manager_role` tinyint(1) NULL DEFAULT 0,
  `management_position_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `management_work_evaluation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `management_score` double NULL DEFAULT NULL,
  `teaching_plan_quality` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `teaching_organization` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `classroom_management` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `teaching_concept` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `classroom_effectiveness` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `moral_education_integration` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `modern_education_tech_integration` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `classroom_teaching_score` double NULL DEFAULT NULL,
  `homework_correction_quality` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `personalized_guidance` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `psychological_guidance` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `student_comprehensive_assessment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `growth_records_maintenance` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `learning_difficulty_support` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `career_planning_guidance` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `five_education_guidance` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `learning_behavior_habits_cultivation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `student_development_score` double NULL DEFAULT NULL,
  `parent_meeting_participation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `home_visit_records` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `parenting_guidance` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `parent_school_training` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `parent_contact_score` double NULL DEFAULT NULL,
  `total_teaching_routine_score` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teaching_routine_assessment
-- ----------------------------

-- ----------------------------
-- Table structure for tk_assessment_detail
-- ----------------------------
DROP TABLE IF EXISTS `tk_assessment_detail`;
CREATE TABLE `tk_assessment_detail`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `assessment_id` bigint NULL DEFAULT NULL,
  `category_id` int NULL DEFAULT NULL,
  `evaluation_element` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `evaluation_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `evaluation_standard` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `score` double NULL DEFAULT NULL,
  `max_score` double NULL DEFAULT NULL,
  `evidence_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `evaluator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `evaluate_date` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_assessment_detail
-- ----------------------------

-- ----------------------------
-- Table structure for tk_assessment_element_config
-- ----------------------------
DROP TABLE IF EXISTS `tk_assessment_element_config`;
CREATE TABLE `tk_assessment_element_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` int NULL DEFAULT NULL,
  `element_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `element_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `evaluation_criteria` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `max_score` double NULL DEFAULT NULL,
  `weight` double NULL DEFAULT NULL,
  `calculation_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_active` tinyint(1) NULL DEFAULT 0,
  `display_order` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_assessment_element_config
-- ----------------------------

-- ----------------------------
-- Table structure for tk_teacher_performance_assessment
-- ----------------------------
DROP TABLE IF EXISTS `tk_teacher_performance_assessment`;
CREATE TABLE `tk_teacher_performance_assessment`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `teacher_id` bigint NULL DEFAULT NULL,
  `academic_year` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `assessment_date` datetime NULL DEFAULT NULL,
  `total_score` double NULL DEFAULT NULL,
  `final_grade` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `assessor` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `assessment_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_teacher_performance_assessment
-- ----------------------------

-- ----------------------------
-- Table structure for tk_v1_evaluation_element
-- ----------------------------
DROP TABLE IF EXISTS `tk_v1_evaluation_element`;
CREATE TABLE `tk_v1_evaluation_element`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v1_evaluation_element
-- ----------------------------

-- ----------------------------
-- Table structure for tk_v1_evaluation_indicator
-- ----------------------------
DROP TABLE IF EXISTS `tk_v1_evaluation_indicator`;
CREATE TABLE `tk_v1_evaluation_indicator`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sub_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `kpi_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v1_evaluation_indicator
-- ----------------------------

-- ----------------------------
-- Table structure for tk_v1_kpi
-- ----------------------------
DROP TABLE IF EXISTS `tk_v1_kpi`;
CREATE TABLE `tk_v1_kpi`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `parent_id` bigint NULL DEFAULT NULL,
  `start_time` datetime NULL DEFAULT NULL,
  `end_time` datetime NULL DEFAULT NULL,
  `total_score` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v1_kpi
-- ----------------------------

-- ----------------------------
-- Table structure for tk_v1_role
-- ----------------------------
DROP TABLE IF EXISTS `tk_v1_role`;
CREATE TABLE `tk_v1_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v1_role
-- ----------------------------
INSERT INTO `tk_v1_role` VALUES (1, '领导');
INSERT INTO `tk_v1_role` VALUES (2, '老师');
INSERT INTO `tk_v1_role` VALUES (3, '管理员');

-- ----------------------------
-- Table structure for tk_v1_teacher_content
-- ----------------------------
DROP TABLE IF EXISTS `tk_v1_teacher_content`;
CREATE TABLE `tk_v1_teacher_content`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `evaluation_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `score` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v1_teacher_content
-- ----------------------------

-- ----------------------------
-- Table structure for tk_v1_teacher_performance_assessment
-- ----------------------------
DROP TABLE IF EXISTS `tk_v1_teacher_performance_assessment`;
CREATE TABLE `tk_v1_teacher_performance_assessment`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `element_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `evaluation_id` bigint NULL DEFAULT NULL,
  `content_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `evaluation_standard` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `score` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v1_teacher_performance_assessment
-- ----------------------------

-- ----------------------------
-- Table structure for tk_v1_user
-- ----------------------------
DROP TABLE IF EXISTS `tk_v1_user`;
CREATE TABLE `tk_v1_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role_id` bigint NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dispatch_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v1_user
-- ----------------------------
INSERT INTO `tk_v1_user` VALUES (1, 'd77c758f63238837b49b725a7477a594', 3, 'admin', NULL, 1, '18850793837', '+1+');
INSERT INTO `tk_v1_user` VALUES (2, 'd77c758f63238837b49b725a7477a594', 1, 'zs', NULL, 1, NULL, NULL);
INSERT INTO `tk_v1_user` VALUES (3, 'd77c758f63238837b49b725a7477a594', 2, 'ls', NULL, 1, NULL, NULL);
INSERT INTO `tk_v1_user` VALUES (4, 'd77c758f63238837b49b725a7477a594', 2, 'ww', '班主任', 1, NULL, '+1+');
INSERT INTO `tk_v1_user` VALUES (5, 'd77c758f63238837b49b725a7477a594', 2, 'zl', NULL, 1, NULL, NULL);
INSERT INTO `tk_v1_user` VALUES (7, 'd77c758f63238837b49b725a7477a594', 3, 'J.YIN', NULL, 1, '13625063671', NULL);

-- ----------------------------
-- Table structure for tk_v3_content
-- ----------------------------
DROP TABLE IF EXISTS `tk_v3_content`;
CREATE TABLE `tk_v3_content`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `element_id` bigint NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `score` double NULL DEFAULT NULL,
  `type_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 148 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v3_content
-- ----------------------------
INSERT INTO `tk_v3_content` VALUES (1, 1, '1、按照教育部《新时代中小学教师职业行为十项准则》、《新时代幼儿园教师职业行为十项准则》执行', 30, 3);
INSERT INTO `tk_v3_content` VALUES (2, 1, '2.未存在教育部《中小学教师违反职业道德行为处理办法（2018年修订）》、《幼儿园教师违反职业道德行为处理办法》中应予处理的教师违反职业道德的行为', 40, 3);
INSERT INTO `tk_v3_content` VALUES (3, 1, '3.未存在《福州市中小学（幼儿园）教师职业行为负面清单》及《福州市教师职业行为负面清单处理办法》中的师德失范行为', 30, 3);
INSERT INTO `tk_v3_content` VALUES (4, 2, '病假', -0.2, 1);
INSERT INTO `tk_v3_content` VALUES (5, 2, '事假', -0.2, 1);
INSERT INTO `tk_v3_content` VALUES (6, 2, '迟到', -0.05, 1);
INSERT INTO `tk_v3_content` VALUES (7, 2, '旷课', -0.5, 1);
INSERT INTO `tk_v3_content` VALUES (8, 2, '未参加政治学习', -0.1, 1);
INSERT INTO `tk_v3_content` VALUES (9, 2, '未参加教研活动', -0.1, 1);
INSERT INTO `tk_v3_content` VALUES (10, 2, '未参加学校会议及其他集体活动出勤情况', -0.1, 1);
INSERT INTO `tk_v3_content` VALUES (11, 3, '完成标准课时工作量', 6, 1);
INSERT INTO `tk_v3_content` VALUES (12, 3, '代课', 1, 1);
INSERT INTO `tk_v3_content` VALUES (13, 3, '特殊课时', 1, 1);
INSERT INTO `tk_v3_content` VALUES (14, 4, '担任班主任', 6, 1);
INSERT INTO `tk_v3_content` VALUES (15, 4, '年段长', 6, 1);
INSERT INTO `tk_v3_content` VALUES (16, 4, '教研组长', 6, 1);
INSERT INTO `tk_v3_content` VALUES (17, 4, '少先队总辅导员', 6, 1);
INSERT INTO `tk_v3_content` VALUES (18, 4, '团委书记及中层以上干部工作', 6, 1);
INSERT INTO `tk_v3_content` VALUES (19, 5, '制定教学计划', 1, 1);
INSERT INTO `tk_v3_content` VALUES (20, 5, '组织教学', 1, 1);
INSERT INTO `tk_v3_content` VALUES (21, 5, '课堂管理', 2, 1);
INSERT INTO `tk_v3_content` VALUES (22, 5, '教学理念', 1, 1);
INSERT INTO `tk_v3_content` VALUES (23, 5, '课堂实效', 2, 1);
INSERT INTO `tk_v3_content` VALUES (24, 5, '德育渗透', 1, 1);
INSERT INTO `tk_v3_content` VALUES (25, 5, '现代教育技术手段与学科融合等', 1, 1);
INSERT INTO `tk_v3_content` VALUES (26, 6, '作业批改', 2, 1);
INSERT INTO `tk_v3_content` VALUES (27, 6, '个性化辅导', 2, 1);
INSERT INTO `tk_v3_content` VALUES (28, 6, '心理辅导', 1, 1);
INSERT INTO `tk_v3_content` VALUES (29, 6, '学生综合素质评定', 1, 1);
INSERT INTO `tk_v3_content` VALUES (30, 6, '成长档案记录', 1, 1);
INSERT INTO `tk_v3_content` VALUES (31, 6, '帮扶学困生（特殊生）生涯规划指导', 1, 1);
INSERT INTO `tk_v3_content` VALUES (32, 6, '五育并举指导', 1, 1);
INSERT INTO `tk_v3_content` VALUES (33, 6, '学习行为习惯培养等', 2, 1);
INSERT INTO `tk_v3_content` VALUES (34, 7, '家长会', 1, 1);
INSERT INTO `tk_v3_content` VALUES (35, 7, '家访', 1, 1);
INSERT INTO `tk_v3_content` VALUES (36, 7, '家教指导', 1, 1);
INSERT INTO `tk_v3_content` VALUES (37, 7, '家长学校培训等', 1, 1);
INSERT INTO `tk_v3_content` VALUES (38, 8, '任教班级学生学业发展情况', 14, 1);
INSERT INTO `tk_v3_content` VALUES (39, 8, '任教学科中高考', 0.5, 1);
INSERT INTO `tk_v3_content` VALUES (40, 8, '合格性考试（会考）.市质检', 0.5, 1);
INSERT INTO `tk_v3_content` VALUES (41, 8, '期末考试', 0.5, 1);
INSERT INTO `tk_v3_content` VALUES (42, 8, '质量监测等的平均分', 0.5, 1);
INSERT INTO `tk_v3_content` VALUES (43, 8, '及格率', 0.5, 1);
INSERT INTO `tk_v3_content` VALUES (44, 8, '优秀率', 0.5, 1);
INSERT INTO `tk_v3_content` VALUES (45, 9, '履行师徒协议及指导成效', 1, 1);
INSERT INTO `tk_v3_content` VALUES (46, 9, '名师工作室工作，担任学科培训班导师，参加“送教送培下乡”活动等', 1, 1);
INSERT INTO `tk_v3_content` VALUES (47, 10, '班（团、队）会活动', 2, 1);
INSERT INTO `tk_v3_content` VALUES (48, 10, '先进集体', 2, 1);
INSERT INTO `tk_v3_content` VALUES (49, 10, '单项竞赛获奖', 2, 2);
INSERT INTO `tk_v3_content` VALUES (50, 10, '综合性荣誉表彰', 2, 1);
INSERT INTO `tk_v3_content` VALUES (51, 11, '指导学生参加学科竞赛', 4, 2);
INSERT INTO `tk_v3_content` VALUES (52, 11, '创新大赛', 2, 1);
INSERT INTO `tk_v3_content` VALUES (53, 11, '体育联赛', 2, 1);
INSERT INTO `tk_v3_content` VALUES (54, 11, '艺术展演等获奖', 2, 1);
INSERT INTO `tk_v3_content` VALUES (55, 12, '参与校本课程研发', 0.5, 1);
INSERT INTO `tk_v3_content` VALUES (56, 12, '指导研究性学习，组织综合实践，社会实践，社团活动，担任课外活动辅导员等', 0.5, 1);
INSERT INTO `tk_v3_content` VALUES (58, 13, '参与教研，集备活动', 1, 1);
INSERT INTO `tk_v3_content` VALUES (59, 13, '教案', 2, 1);
INSERT INTO `tk_v3_content` VALUES (61, 13, '参加岗位练兵,培训学习，完成听评课任务等', 2, 1);
INSERT INTO `tk_v3_content` VALUES (66, 15, '开设各级学科示范课', 3, 1);
INSERT INTO `tk_v3_content` VALUES (67, 15, '观摩课及专题讲座', 3, 1);
INSERT INTO `tk_v3_content` VALUES (68, 15, '主持班（团、队）观摩活动', 3, 1);
INSERT INTO `tk_v3_content` VALUES (70, 15, '区域交流活动等', 3, 1);
INSERT INTO `tk_v3_content` VALUES (71, 16, '县级以上收入汇编', 2, 1);
INSERT INTO `tk_v3_content` VALUES (72, 16, '德育', 0.5, 1);
INSERT INTO `tk_v3_content` VALUES (73, 16, '党建', 0.5, 1);
INSERT INTO `tk_v3_content` VALUES (74, 16, '在校收入汇编', 1.5, 1);
INSERT INTO `tk_v3_content` VALUES (75, 17, '校级课题研究等', 1, 1);
INSERT INTO `tk_v3_content` VALUES (76, 17, '县区级课题研究等', 1.5, 1);
INSERT INTO `tk_v3_content` VALUES (77, 17, '省市级课题研究等', 2, 1);
INSERT INTO `tk_v3_content` VALUES (79, 17, '缺席继续教育', -0.2, 1);
INSERT INTO `tk_v3_content` VALUES (80, 17, '迟到、早退继续教育', -0.1, 1);
INSERT INTO `tk_v3_content` VALUES (81, 17, '完成继续教育', 2, 1);
INSERT INTO `tk_v3_content` VALUES (82, 18, '获得各级教育教学专项表彰', 2, 1);
INSERT INTO `tk_v3_content` VALUES (83, 18, '综合性荣誉表彰', 2, 1);
INSERT INTO `tk_v3_content` VALUES (84, 18, '公开教学竞赛获奖', 2, 1);
INSERT INTO `tk_v3_content` VALUES (85, 18, '名优骨干认定', 2, 1);
INSERT INTO `tk_v3_content` VALUES (86, 19, '校级学校党建', 4, 1);
INSERT INTO `tk_v3_content` VALUES (87, 19, '中层学校党建', 3.5, 1);
INSERT INTO `tk_v3_content` VALUES (88, 19, '处室干事、党建干事', 2.5, 1);
INSERT INTO `tk_v3_content` VALUES (89, 19, '其他', 2.5, 1);
INSERT INTO `tk_v3_content` VALUES (99, 20, '校级比赛评委', 0.3, 1);
INSERT INTO `tk_v3_content` VALUES (100, 20, '县区级比赛评委', 0.6, 1);
INSERT INTO `tk_v3_content` VALUES (101, 20, '其他', 2, 1);
INSERT INTO `tk_v3_content` VALUES (124, 2, '拖课', -0.2, 1);
INSERT INTO `tk_v3_content` VALUES (125, 2, '旷工', -3, 1);
INSERT INTO `tk_v3_content` VALUES (126, 2, '导辅', 1.5, 1);
INSERT INTO `tk_v3_content` VALUES (127, 2, '监操', 0.5, 1);
INSERT INTO `tk_v3_content` VALUES (128, 5, '未履行组织教学', -0.2, 1);
INSERT INTO `tk_v3_content` VALUES (129, 5, '未履行课堂管理', -0.5, 1);
INSERT INTO `tk_v3_content` VALUES (130, 5, '学科融合落实不到位', -0.2, 1);
INSERT INTO `tk_v3_content` VALUES (131, 2, '导辅迟到、早退', -0.2, 1);
INSERT INTO `tk_v3_content` VALUES (132, 2, '监操迟到、早退', -0.2, 1);
INSERT INTO `tk_v3_content` VALUES (133, 9, '承担各级学科研训组', 1, 1);
INSERT INTO `tk_v3_content` VALUES (134, 13, '缺席练兵，学习', -0.2, 1);
INSERT INTO `tk_v3_content` VALUES (135, 13, '缺听课和评课', -0.2, 1);
INSERT INTO `tk_v3_content` VALUES (144, 14, '学生', 2, 1);
INSERT INTO `tk_v3_content` VALUES (145, 14, '家长满意度', 2, 1);

-- ----------------------------
-- Table structure for tk_v3_element
-- ----------------------------
DROP TABLE IF EXISTS `tk_v3_element`;
CREATE TABLE `tk_v3_element`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `indicator_id` bigint NULL DEFAULT NULL,
  `element` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `criteria` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` int NULL DEFAULT NULL,
  `score` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v3_element
-- ----------------------------
INSERT INTO `tk_v3_element` VALUES (1, 1, NULL, '是否合格，不设分值', 0, 100);
INSERT INTO `tk_v3_element` VALUES (2, 2, '1.出勤情况', NULL, 0, 10);
INSERT INTO `tk_v3_element` VALUES (3, 2, '2.课时工作量', NULL, 0, 8);
INSERT INTO `tk_v3_element` VALUES (4, 2, '3.班主任等工作', NULL, 0, 6);
INSERT INTO `tk_v3_element` VALUES (5, 2, '4.课堂教学', NULL, 0, 8);
INSERT INTO `tk_v3_element` VALUES (6, 2, '5.学生发展指导', NULL, 0, 6);
INSERT INTO `tk_v3_element` VALUES (7, 2, '6.家校联系', NULL, 0, 2);
INSERT INTO `tk_v3_element` VALUES (8, 3, '1.教育教学业绩', NULL, 0, 16);
INSERT INTO `tk_v3_element` VALUES (9, 3, '2.示范引领', NULL, 0, 3);
INSERT INTO `tk_v3_element` VALUES (10, 3, '3.任教班级获奖', NULL, 0, 3);
INSERT INTO `tk_v3_element` VALUES (11, 3, '4.指导学生获奖情况', NULL, 0, 4);
INSERT INTO `tk_v3_element` VALUES (12, 3, '5.校本课程、综合实践活动', NULL, 0, 5);
INSERT INTO `tk_v3_element` VALUES (13, 3, '6.教学团队合作', NULL, 0, 6);
INSERT INTO `tk_v3_element` VALUES (14, 3, '7.学生（家长）评价', NULL, 0, 3);
INSERT INTO `tk_v3_element` VALUES (15, 4, '1.承担公开教学活动', NULL, 0, 3);
INSERT INTO `tk_v3_element` VALUES (16, 4, '2.撰写教育教学论文', NULL, 0, 2);
INSERT INTO `tk_v3_element` VALUES (17, 4, '3.参与教育教学研究', NULL, 0, 4);
INSERT INTO `tk_v3_element` VALUES (18, 4, '4.个人获奖', NULL, 0, 3);
INSERT INTO `tk_v3_element` VALUES (19, 5, '1.承担学校行政工作', NULL, 0, 4);
INSERT INTO `tk_v3_element` VALUES (20, 5, '2.承担学校临时性重要任务', NULL, 0, 4);

-- ----------------------------
-- Table structure for tk_v3_indicator
-- ----------------------------
DROP TABLE IF EXISTS `tk_v3_indicator`;
CREATE TABLE `tk_v3_indicator`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `indicator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sub_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `kpi_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v3_indicator
-- ----------------------------
INSERT INTO `tk_v3_indicator` VALUES (1, '师德师风', '（优、合格、不合格）', 1);
INSERT INTO `tk_v3_indicator` VALUES (2, '教育教学常规', '30-40 分', 1);
INSERT INTO `tk_v3_indicator` VALUES (3, '教育教学业绩', '30-45 分', 1);
INSERT INTO `tk_v3_indicator` VALUES (4, '个人专业发展', '5-15 分', 1);
INSERT INTO `tk_v3_indicator` VALUES (5, '承担急难险重工作', '5-10 分', 1);

-- ----------------------------
-- Table structure for tk_v3_kpi
-- ----------------------------
DROP TABLE IF EXISTS `tk_v3_kpi`;
CREATE TABLE `tk_v3_kpi`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v3_kpi
-- ----------------------------
INSERT INTO `tk_v3_kpi` VALUES (1, '福清市中小学、中职学校教师绩效考核要点');
INSERT INTO `tk_v3_kpi` VALUES (3, '9999');

-- ----------------------------
-- Table structure for tk_v3_kpi_score_type
-- ----------------------------
DROP TABLE IF EXISTS `tk_v3_kpi_score_type`;
CREATE TABLE `tk_v3_kpi_score_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `json_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v3_kpi_score_type
-- ----------------------------
INSERT INTO `tk_v3_kpi_score_type` VALUES (1, '{\"type\":\"count\",\"data\":[]}', '计分');
INSERT INTO `tk_v3_kpi_score_type` VALUES (2, '{\"type\":\"select\",\"data\":[{\"name\":\"一等奖\",\"score\":18},{\"name\":\"二等奖\",\"score\":16},{\"name\":\"三等奖\",\"score\":14}]}', '选择');
INSERT INTO `tk_v3_kpi_score_type` VALUES (3, '{\"type\":\"exclude\",\"data\":[{\"name\":\"完成\",\"score\":30},{\"name\":\"未完成\",\"score\":0}]}', '排它');

-- ----------------------------
-- Table structure for tk_v3_teacher_content_score
-- ----------------------------
DROP TABLE IF EXISTS `tk_v3_teacher_content_score`;
CREATE TABLE `tk_v3_teacher_content_score`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `content_id` bigint NULL DEFAULT NULL,
  `score` double NULL DEFAULT NULL,
  `element_id` bigint NULL DEFAULT NULL,
  `time` int NULL DEFAULT NULL,
  `file_id` bigint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1580 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v3_teacher_content_score
-- ----------------------------
INSERT INTO `tk_v3_teacher_content_score` VALUES (404, 1, 1, 30, 1, 1, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (405, 1, 2, 40, 1, 1, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (406, 1, 3, 30, 1, 1, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (407, 1, 4, 0, 2, 0, 1);
INSERT INTO `tk_v3_teacher_content_score` VALUES (408, 1, 5, 0, 2, 0, 2);
INSERT INTO `tk_v3_teacher_content_score` VALUES (409, 1, 6, 0, 2, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (410, 1, 7, 0, 2, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (411, 1, 8, 0, 2, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (412, 1, 9, 0, 2, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (413, 1, 10, 0, 2, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (414, 1, 11, NULL, 3, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (415, 1, 12, NULL, 3, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (416, 1, 13, NULL, 3, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (417, 1, 14, NULL, 4, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (418, 1, 15, NULL, 4, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (419, 1, 16, NULL, 4, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (420, 1, 17, NULL, 4, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (421, 1, 18, NULL, 4, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (422, 1, 19, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (423, 1, 20, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (424, 1, 21, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (425, 1, 22, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (426, 1, 23, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (427, 1, 24, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (428, 1, 25, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (429, 1, 26, 4, 6, 2, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (430, 1, 27, 2, 6, 1, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (431, 1, 28, 0, 6, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (432, 1, 29, 0, 6, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (433, 1, 30, 0, 6, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (434, 1, 31, 0, 6, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (435, 1, 32, 0, 6, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (436, 1, 33, 0, 6, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (437, 1, 34, NULL, 7, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (438, 1, 35, NULL, 7, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (439, 1, 36, NULL, 7, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (440, 1, 37, NULL, 7, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (441, 1, 38, NULL, 8, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (442, 1, 39, NULL, 8, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (443, 1, 40, NULL, 8, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (444, 1, 41, NULL, 8, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (445, 1, 42, NULL, 8, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (446, 1, 43, NULL, 8, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (447, 1, 44, NULL, 8, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (448, 1, 45, NULL, 9, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (449, 1, 46, NULL, 9, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (450, 1, 47, 3, 10, 2, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (451, 1, 48, 2, 10, 1, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (452, 1, 49, 2, 10, 1, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (453, 1, 50, 0, 10, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (454, 1, 51, NULL, 11, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (455, 1, 52, NULL, 11, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (456, 1, 53, NULL, 11, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (457, 1, 54, NULL, 11, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (458, 1, 55, NULL, 12, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (459, 1, 56, NULL, 12, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (460, 1, 58, NULL, 13, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (461, 1, 59, NULL, 13, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (462, 1, 61, NULL, 13, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (463, 1, 66, NULL, 15, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (464, 1, 67, NULL, 15, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (465, 1, 68, NULL, 15, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (466, 1, 70, NULL, 15, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (467, 1, 71, NULL, 16, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (468, 1, 72, NULL, 16, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (469, 1, 73, NULL, 16, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (470, 1, 74, NULL, 16, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (471, 1, 75, NULL, 17, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (472, 1, 76, NULL, 17, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (473, 1, 77, NULL, 17, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (474, 1, 79, NULL, 17, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (475, 1, 80, NULL, 17, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (476, 1, 81, NULL, 17, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (477, 1, 82, NULL, 18, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (478, 1, 83, NULL, 18, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (479, 1, 84, NULL, 18, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (480, 1, 85, NULL, 18, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (481, 1, 86, NULL, 19, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (482, 1, 87, NULL, 19, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (483, 1, 88, NULL, 19, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (484, 1, 89, NULL, 19, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (485, 1, 99, NULL, 20, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (486, 1, 100, NULL, 20, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (487, 1, 101, NULL, 20, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (488, 1, 124, 0, 2, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (489, 1, 125, 0, 2, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (490, 1, 126, 0, 2, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (491, 1, 127, 0, 2, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (492, 1, 128, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (493, 1, 129, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (494, 1, 130, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (495, 1, 131, 0, 2, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (496, 1, 132, 0, 2, 0, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (497, 1, 133, NULL, 9, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (498, 1, 134, NULL, 13, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (499, 1, 135, NULL, 13, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (500, 1, 144, NULL, 14, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (501, 1, 145, NULL, 14, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1482, 4, 1, NULL, 1, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1483, 4, 2, NULL, 1, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1484, 4, 3, NULL, 1, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1485, 4, 4, NULL, 2, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1486, 4, 5, NULL, 2, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1487, 4, 6, NULL, 2, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1488, 4, 7, NULL, 2, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1489, 4, 8, NULL, 2, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1490, 4, 9, NULL, 2, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1491, 4, 10, NULL, 2, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1492, 4, 11, NULL, 3, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1493, 4, 12, NULL, 3, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1494, 4, 13, NULL, 3, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1495, 4, 14, NULL, 4, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1496, 4, 15, NULL, 4, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1497, 4, 16, NULL, 4, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1498, 4, 17, NULL, 4, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1499, 4, 18, NULL, 4, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1500, 4, 19, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1501, 4, 20, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1502, 4, 21, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1503, 4, 22, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1504, 4, 23, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1505, 4, 24, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1506, 4, 25, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1507, 4, 26, NULL, 6, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1508, 4, 27, NULL, 6, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1509, 4, 28, NULL, 6, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1510, 4, 29, NULL, 6, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1511, 4, 30, NULL, 6, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1512, 4, 31, NULL, 6, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1513, 4, 32, NULL, 6, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1514, 4, 33, NULL, 6, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1515, 4, 34, NULL, 7, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1516, 4, 35, NULL, 7, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1517, 4, 36, NULL, 7, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1518, 4, 37, NULL, 7, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1519, 4, 38, NULL, 8, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1520, 4, 39, NULL, 8, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1521, 4, 40, NULL, 8, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1522, 4, 41, NULL, 8, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1523, 4, 42, NULL, 8, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1524, 4, 43, NULL, 8, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1525, 4, 44, NULL, 8, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1526, 4, 45, NULL, 9, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1527, 4, 46, NULL, 9, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1528, 4, 47, NULL, 10, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1529, 4, 48, NULL, 10, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1530, 4, 49, NULL, 10, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1531, 4, 50, NULL, 10, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1532, 4, 51, NULL, 11, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1533, 4, 52, NULL, 11, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1534, 4, 53, NULL, 11, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1535, 4, 54, NULL, 11, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1536, 4, 55, NULL, 12, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1537, 4, 56, NULL, 12, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1538, 4, 58, NULL, 13, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1539, 4, 59, NULL, 13, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1540, 4, 61, NULL, 13, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1541, 4, 66, NULL, 15, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1542, 4, 67, NULL, 15, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1543, 4, 68, NULL, 15, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1544, 4, 70, NULL, 15, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1545, 4, 71, NULL, 16, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1546, 4, 72, NULL, 16, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1547, 4, 73, NULL, 16, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1548, 4, 74, NULL, 16, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1549, 4, 75, NULL, 17, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1550, 4, 76, NULL, 17, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1551, 4, 77, NULL, 17, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1552, 4, 79, NULL, 17, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1553, 4, 80, NULL, 17, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1554, 4, 81, NULL, 17, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1555, 4, 82, NULL, 18, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1556, 4, 83, NULL, 18, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1557, 4, 84, NULL, 18, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1558, 4, 85, NULL, 18, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1559, 4, 86, NULL, 19, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1560, 4, 87, NULL, 19, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1561, 4, 88, NULL, 19, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1562, 4, 89, NULL, 19, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1563, 4, 99, NULL, 20, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1564, 4, 100, NULL, 20, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1565, 4, 101, NULL, 20, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1566, 4, 124, NULL, 2, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1567, 4, 125, NULL, 2, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1568, 4, 126, NULL, 2, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1569, 4, 127, NULL, 2, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1570, 4, 128, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1571, 4, 129, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1572, 4, 130, NULL, 5, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1573, 4, 131, NULL, 2, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1574, 4, 132, NULL, 2, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1575, 4, 133, NULL, 9, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1576, 4, 134, NULL, 13, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1577, 4, 135, NULL, 13, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1578, 4, 144, NULL, 14, NULL, 0);
INSERT INTO `tk_v3_teacher_content_score` VALUES (1579, 4, 145, NULL, 14, NULL, 0);

-- ----------------------------
-- Table structure for tk_v3_teacher_element_score
-- ----------------------------
DROP TABLE IF EXISTS `tk_v3_teacher_element_score`;
CREATE TABLE `tk_v3_teacher_element_score`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `element_id` bigint NULL DEFAULT NULL,
  `score` double NULL DEFAULT NULL,
  `kpi_id` bigint NULL DEFAULT NULL,
  `task_id` bigint NULL DEFAULT NULL,
  `final_score` double NULL DEFAULT NULL,
  `indicator_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 321 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v3_teacher_element_score
-- ----------------------------
INSERT INTO `tk_v3_teacher_element_score` VALUES (81, 1, 1, 100, 1, NULL, 90, 1);
INSERT INTO `tk_v3_teacher_element_score` VALUES (82, 1, 2, 0, 1, NULL, 10, 2);
INSERT INTO `tk_v3_teacher_element_score` VALUES (83, 1, 3, NULL, 1, NULL, NULL, 2);
INSERT INTO `tk_v3_teacher_element_score` VALUES (84, 1, 4, NULL, 1, NULL, NULL, 2);
INSERT INTO `tk_v3_teacher_element_score` VALUES (85, 1, 5, NULL, 1, NULL, NULL, 2);
INSERT INTO `tk_v3_teacher_element_score` VALUES (86, 1, 6, 6, 1, NULL, NULL, 2);
INSERT INTO `tk_v3_teacher_element_score` VALUES (87, 1, 7, NULL, 1, NULL, NULL, 2);
INSERT INTO `tk_v3_teacher_element_score` VALUES (88, 1, 8, NULL, 1, NULL, NULL, 3);
INSERT INTO `tk_v3_teacher_element_score` VALUES (89, 1, 9, NULL, 1, NULL, NULL, 3);
INSERT INTO `tk_v3_teacher_element_score` VALUES (90, 1, 10, 7, 1, NULL, NULL, 3);
INSERT INTO `tk_v3_teacher_element_score` VALUES (91, 1, 11, NULL, 1, NULL, NULL, 3);
INSERT INTO `tk_v3_teacher_element_score` VALUES (92, 1, 12, NULL, 1, NULL, NULL, 3);
INSERT INTO `tk_v3_teacher_element_score` VALUES (93, 1, 13, NULL, 1, NULL, NULL, 3);
INSERT INTO `tk_v3_teacher_element_score` VALUES (94, 1, 14, NULL, 1, NULL, NULL, 3);
INSERT INTO `tk_v3_teacher_element_score` VALUES (95, 1, 15, NULL, 1, NULL, NULL, 4);
INSERT INTO `tk_v3_teacher_element_score` VALUES (96, 1, 16, NULL, 1, NULL, NULL, 4);
INSERT INTO `tk_v3_teacher_element_score` VALUES (97, 1, 17, NULL, 1, NULL, NULL, 4);
INSERT INTO `tk_v3_teacher_element_score` VALUES (98, 1, 18, NULL, 1, NULL, NULL, 4);
INSERT INTO `tk_v3_teacher_element_score` VALUES (99, 1, 19, NULL, 1, NULL, NULL, 5);
INSERT INTO `tk_v3_teacher_element_score` VALUES (100, 1, 20, NULL, 1, NULL, NULL, 5);
INSERT INTO `tk_v3_teacher_element_score` VALUES (301, 4, 1, NULL, 1, NULL, NULL, 1);
INSERT INTO `tk_v3_teacher_element_score` VALUES (302, 4, 2, NULL, 1, NULL, NULL, 2);
INSERT INTO `tk_v3_teacher_element_score` VALUES (303, 4, 3, NULL, 1, NULL, NULL, 2);
INSERT INTO `tk_v3_teacher_element_score` VALUES (304, 4, 4, NULL, 1, NULL, NULL, 2);
INSERT INTO `tk_v3_teacher_element_score` VALUES (305, 4, 5, NULL, 1, NULL, NULL, 2);
INSERT INTO `tk_v3_teacher_element_score` VALUES (306, 4, 6, NULL, 1, NULL, NULL, 2);
INSERT INTO `tk_v3_teacher_element_score` VALUES (307, 4, 7, NULL, 1, NULL, NULL, 2);
INSERT INTO `tk_v3_teacher_element_score` VALUES (308, 4, 8, NULL, 1, NULL, NULL, 3);
INSERT INTO `tk_v3_teacher_element_score` VALUES (309, 4, 9, NULL, 1, NULL, NULL, 3);
INSERT INTO `tk_v3_teacher_element_score` VALUES (310, 4, 10, NULL, 1, NULL, NULL, 3);
INSERT INTO `tk_v3_teacher_element_score` VALUES (311, 4, 11, NULL, 1, NULL, NULL, 3);
INSERT INTO `tk_v3_teacher_element_score` VALUES (312, 4, 12, NULL, 1, NULL, NULL, 3);
INSERT INTO `tk_v3_teacher_element_score` VALUES (313, 4, 13, NULL, 1, NULL, NULL, 3);
INSERT INTO `tk_v3_teacher_element_score` VALUES (314, 4, 14, NULL, 1, NULL, NULL, 3);
INSERT INTO `tk_v3_teacher_element_score` VALUES (315, 4, 15, NULL, 1, NULL, NULL, 4);
INSERT INTO `tk_v3_teacher_element_score` VALUES (316, 4, 16, NULL, 1, NULL, NULL, 4);
INSERT INTO `tk_v3_teacher_element_score` VALUES (317, 4, 17, NULL, 1, NULL, NULL, 4);
INSERT INTO `tk_v3_teacher_element_score` VALUES (318, 4, 18, NULL, 1, NULL, NULL, 4);
INSERT INTO `tk_v3_teacher_element_score` VALUES (319, 4, 19, NULL, 1, NULL, NULL, 5);
INSERT INTO `tk_v3_teacher_element_score` VALUES (320, 4, 20, NULL, 1, NULL, NULL, 5);

-- ----------------------------
-- Table structure for tk_v3_teacher_file
-- ----------------------------
DROP TABLE IF EXISTS `tk_v3_teacher_file`;
CREATE TABLE `tk_v3_teacher_file`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v3_teacher_file
-- ----------------------------
INSERT INTO `tk_v3_teacher_file` VALUES (1, 'test1', 'teacher_file/1.png', 4);
INSERT INTO `tk_v3_teacher_file` VALUES (2, 'test123', 'teacher_file/5A6A2EA04F4B4FAA998E8571EF8FFEF8.jpg', 5);

-- ----------------------------
-- Table structure for tk_v3_teacher_indicator_score
-- ----------------------------
DROP TABLE IF EXISTS `tk_v3_teacher_indicator_score`;
CREATE TABLE `tk_v3_teacher_indicator_score`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `kpi_id` bigint NULL DEFAULT NULL,
  `score` double NULL DEFAULT NULL,
  `indicator_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v3_teacher_indicator_score
-- ----------------------------
INSERT INTO `tk_v3_teacher_indicator_score` VALUES (1, 1, 1, 90, 1);
INSERT INTO `tk_v3_teacher_indicator_score` VALUES (2, 1, 1, 16, 2);
INSERT INTO `tk_v3_teacher_indicator_score` VALUES (3, 1, 1, 7, 3);
INSERT INTO `tk_v3_teacher_indicator_score` VALUES (4, 1, 1, NULL, 4);
INSERT INTO `tk_v3_teacher_indicator_score` VALUES (5, 1, 1, NULL, 5);
INSERT INTO `tk_v3_teacher_indicator_score` VALUES (56, 4, 1, NULL, 1);
INSERT INTO `tk_v3_teacher_indicator_score` VALUES (57, 4, 1, NULL, 2);
INSERT INTO `tk_v3_teacher_indicator_score` VALUES (58, 4, 1, NULL, 3);
INSERT INTO `tk_v3_teacher_indicator_score` VALUES (59, 4, 1, NULL, 4);
INSERT INTO `tk_v3_teacher_indicator_score` VALUES (60, 4, 1, NULL, 5);

-- ----------------------------
-- Table structure for tk_v3_teacher_kpi_score
-- ----------------------------
DROP TABLE IF EXISTS `tk_v3_teacher_kpi_score`;
CREATE TABLE `tk_v3_teacher_kpi_score`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `kpi_id` bigint NULL DEFAULT NULL,
  `score` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v3_teacher_kpi_score
-- ----------------------------
INSERT INTO `tk_v3_teacher_kpi_score` VALUES (5, 1, 1, 113);
INSERT INTO `tk_v3_teacher_kpi_score` VALUES (16, 4, 1, NULL);

-- ----------------------------
-- Table structure for tk_v3_teacher_task
-- ----------------------------
DROP TABLE IF EXISTS `tk_v3_teacher_task`;
CREATE TABLE `tk_v3_teacher_task`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `parent_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tes_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tk_v3_teacher_task
-- ----------------------------
INSERT INTO `tk_v3_teacher_task` VALUES (21, 1, '1,2', '已完成', 81);
INSERT INTO `tk_v3_teacher_task` VALUES (22, 1, '1,2', '已完成', 82);
INSERT INTO `tk_v3_teacher_task` VALUES (23, 1, '1,2', '待完成', 83);
INSERT INTO `tk_v3_teacher_task` VALUES (24, 1, '1,2', '待完成', 84);
INSERT INTO `tk_v3_teacher_task` VALUES (25, 1, '1,2', '待完成', 85);
INSERT INTO `tk_v3_teacher_task` VALUES (26, 1, '1,2', '待完成', 86);
INSERT INTO `tk_v3_teacher_task` VALUES (27, 1, '1,2', '待完成', 87);
INSERT INTO `tk_v3_teacher_task` VALUES (28, 1, '1,2', '待完成', 88);
INSERT INTO `tk_v3_teacher_task` VALUES (29, 1, '1,2', '待完成', 89);
INSERT INTO `tk_v3_teacher_task` VALUES (30, 1, '1,2', '待完成', 90);
INSERT INTO `tk_v3_teacher_task` VALUES (31, 1, '1,2', '待完成', 91);
INSERT INTO `tk_v3_teacher_task` VALUES (32, 1, '1,2', '待完成', 92);
INSERT INTO `tk_v3_teacher_task` VALUES (33, 1, '1,2', '待完成', 93);
INSERT INTO `tk_v3_teacher_task` VALUES (34, 1, '1,2', '待完成', 94);
INSERT INTO `tk_v3_teacher_task` VALUES (35, 1, '1,2', '待完成', 95);
INSERT INTO `tk_v3_teacher_task` VALUES (36, 1, '1,2', '待完成', 96);
INSERT INTO `tk_v3_teacher_task` VALUES (37, 1, '1,2', '待完成', 97);
INSERT INTO `tk_v3_teacher_task` VALUES (38, 1, '1,2', '待完成', 98);
INSERT INTO `tk_v3_teacher_task` VALUES (39, 1, '1,2', '待完成', 99);
INSERT INTO `tk_v3_teacher_task` VALUES (40, 1, '1,2', '待完成', 100);

-- ----------------------------
-- Table structure for v1_activity
-- ----------------------------
DROP TABLE IF EXISTS `v1_activity`;
CREATE TABLE `v1_activity`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_id` bigint NOT NULL,
  `shop_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `begin_time` bigint NOT NULL,
  `end_time` bigint NOT NULL,
  `begin_hour` int NOT NULL,
  `end_hour` int NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sub_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `head_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `activity_type` int NOT NULL,
  `page_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `rules_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `bg_color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_activity
-- ----------------------------

-- ----------------------------
-- Table structure for v1_activity_category
-- ----------------------------
DROP TABLE IF EXISTS `v1_activity_category`;
CREATE TABLE `v1_activity_category`  (
  `id` bigint NOT NULL,
  `activity_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `parent_id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `poster` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pinyin_abbr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `path_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_shown` int NOT NULL,
  `sort` int NOT NULL,
  `sold_amount` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_activity_category
-- ----------------------------

-- ----------------------------
-- Table structure for v1_admin_config
-- ----------------------------
DROP TABLE IF EXISTS `v1_admin_config`;
CREATE TABLE `v1_admin_config`  (
  `id` int NOT NULL,
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `is_encrypt` tinyint(1) NOT NULL DEFAULT 0,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_admin_config
-- ----------------------------

-- ----------------------------
-- Table structure for v1_agreement
-- ----------------------------
DROP TABLE IF EXISTS `v1_agreement`;
CREATE TABLE `v1_agreement`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `agreement` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sign_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `membership_id` bigint NOT NULL,
  `membership_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `membership_level` int NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_agreement
-- ----------------------------

-- ----------------------------
-- Table structure for v1_announcement
-- ----------------------------
DROP TABLE IF EXISTS `v1_announcement`;
CREATE TABLE `v1_announcement`  (
  `id` bigint NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `area_id` bigint NOT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_announcement
-- ----------------------------

-- ----------------------------
-- Table structure for v1_area
-- ----------------------------
DROP TABLE IF EXISTS `v1_area`;
CREATE TABLE `v1_area`  (
  `id` bigint NOT NULL,
  `status` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `digest` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `lat` double NOT NULL,
  `lon` double NOT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `rect_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_area
-- ----------------------------

-- ----------------------------
-- Table structure for v1_article
-- ----------------------------
DROP TABLE IF EXISTS `v1_article`;
CREATE TABLE `v1_article`  (
  `id` bigint NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cate_id` bigint NOT NULL,
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `publish_time` bigint NOT NULL,
  `status` int NOT NULL,
  `is_top` tinyint(1) NOT NULL DEFAULT 0,
  `is_recommend` tinyint(1) NOT NULL DEFAULT 0,
  `sort` int NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `digest` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `head_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_id_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `views` bigint NOT NULL,
  `favs` bigint NOT NULL,
  `comments` bigint NOT NULL,
  `shares` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_article
-- ----------------------------

-- ----------------------------
-- Table structure for v1_article_category
-- ----------------------------
DROP TABLE IF EXISTS `v1_article_category`;
CREATE TABLE `v1_article_category`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` int NOT NULL,
  `status` int NOT NULL,
  `cate_type` int NOT NULL,
  `org_id` bigint NOT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `head_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `display_mode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_article_category
-- ----------------------------

-- ----------------------------
-- Table structure for v1_article_comment
-- ----------------------------
DROP TABLE IF EXISTS `v1_article_comment`;
CREATE TABLE `v1_article_comment`  (
  `id` bigint NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `article_id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `likes` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_article_comment
-- ----------------------------

-- ----------------------------
-- Table structure for v1_article_comment_like
-- ----------------------------
DROP TABLE IF EXISTS `v1_article_comment_like`;
CREATE TABLE `v1_article_comment_like`  (
  `id` bigint NOT NULL,
  `comment_id` bigint NOT NULL,
  `has_like` tinyint(1) NOT NULL DEFAULT 0,
  `uid` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `author_uid` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_article_comment_like
-- ----------------------------

-- ----------------------------
-- Table structure for v1_article_comment_reply
-- ----------------------------
DROP TABLE IF EXISTS `v1_article_comment_reply`;
CREATE TABLE `v1_article_comment_reply`  (
  `id` bigint NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `article_comment_id` bigint NOT NULL,
  `at_uid` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_article_comment_reply
-- ----------------------------

-- ----------------------------
-- Table structure for v1_article_fav
-- ----------------------------
DROP TABLE IF EXISTS `v1_article_fav`;
CREATE TABLE `v1_article_fav`  (
  `id` bigint NOT NULL,
  `article_id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `head_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `article_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_article_fav
-- ----------------------------

-- ----------------------------
-- Table structure for v1_article_read_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_article_read_log`;
CREATE TABLE `v1_article_read_log`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `article_id` bigint NOT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `count` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_article_read_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_balance_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_balance_log`;
CREATE TABLE `v1_balance_log`  (
  `id` bigint NOT NULL,
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uid` bigint NOT NULL,
  `member_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `member_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `item_id` int NOT NULL,
  `balance_type` int NOT NULL,
  `left_balance` bigint NOT NULL,
  `freeze_balance` bigint NOT NULL,
  `total_balance` bigint NOT NULL,
  `change_amount` bigint NOT NULL,
  `biz_type` int NOT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `freeze_status` int NOT NULL,
  `org_id` bigint NOT NULL,
  `shop_id` bigint NOT NULL,
  `shop_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operator_id` bigint NOT NULL,
  `operator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `staff_id` bigint NOT NULL,
  `staff_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_balance_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_birthday_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_birthday_log`;
CREATE TABLE `v1_birthday_log`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `birthday` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `year` int NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_birthday_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_brand
-- ----------------------------
DROP TABLE IF EXISTS `v1_brand`;
CREATE TABLE `v1_brand`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `poster` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` int NOT NULL,
  `status` int NOT NULL,
  `seo_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `seo_keywords` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `seo_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `show_at_home` tinyint(1) NOT NULL DEFAULT 0,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_brand
-- ----------------------------

-- ----------------------------
-- Table structure for v1_browse_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_browse_log`;
CREATE TABLE `v1_browse_log`  (
  `id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_id` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_browse_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_cabinet
-- ----------------------------
DROP TABLE IF EXISTS `v1_cabinet`;
CREATE TABLE `v1_cabinet`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `area_id` bigint NOT NULL,
  `shop_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cabinet_sn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cabinet_app_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cabinet_secret_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `box_count` int NOT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `filter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `update_time` bigint NOT NULL,
  `sort` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_cabinet
-- ----------------------------

-- ----------------------------
-- Table structure for v1_cabinet_box
-- ----------------------------
DROP TABLE IF EXISTS `v1_cabinet_box`;
CREATE TABLE `v1_cabinet_box`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `shop_id` bigint NOT NULL,
  `shop_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cabinet_id` bigint NOT NULL,
  `cabinet_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `box_no` int NOT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_cabinet_box
-- ----------------------------

-- ----------------------------
-- Table structure for v1_cabinet_callback
-- ----------------------------
DROP TABLE IF EXISTS `v1_cabinet_callback`;
CREATE TABLE `v1_cabinet_callback`  (
  `id` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_cabinet_callback
-- ----------------------------

-- ----------------------------
-- Table structure for v1_cabinet_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_cabinet_log`;
CREATE TABLE `v1_cabinet_log`  (
  `id` bigint NOT NULL,
  `area_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uid` bigint NOT NULL,
  `driver_id` bigint NOT NULL,
  `source` int NOT NULL,
  `cabinet_status` int NOT NULL,
  `cabinet_id` bigint NOT NULL,
  `box_no` int NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_cabinet_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_card_coupon_config
-- ----------------------------
DROP TABLE IF EXISTS `v1_card_coupon_config`;
CREATE TABLE `v1_card_coupon_config`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `shop_id` bigint NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `digest` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `filter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `days` bigint NOT NULL,
  `expire_time` bigint NOT NULL,
  `no_limit_count` tinyint(1) NOT NULL DEFAULT 0,
  `create_time` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `sku_id` bigint NOT NULL,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `give_count` int NOT NULL,
  `status` int NOT NULL,
  `card_type` int NOT NULL,
  `discount` bigint NOT NULL,
  `price` bigint NOT NULL,
  `begin_use_time` bigint NOT NULL,
  `only_pick_up` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_card_coupon_config
-- ----------------------------

-- ----------------------------
-- Table structure for v1_category
-- ----------------------------
DROP TABLE IF EXISTS `v1_category`;
CREATE TABLE `v1_category`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `parent_id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `poster` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pinyin_abbr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `path_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_shown` int NOT NULL,
  `exclude` tinyint(1) NOT NULL DEFAULT 0,
  `need_time_to_make` tinyint(1) NOT NULL DEFAULT 0,
  `discount_json` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `staff_award_ratio` double NOT NULL,
  `sort` int NOT NULL,
  `category_type` int NOT NULL,
  `sold_amount` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_category
-- ----------------------------

-- ----------------------------
-- Table structure for v1_category_attr
-- ----------------------------
DROP TABLE IF EXISTS `v1_category_attr`;
CREATE TABLE `v1_category_attr`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_category_attr
-- ----------------------------

-- ----------------------------
-- Table structure for v1_category_classify
-- ----------------------------
DROP TABLE IF EXISTS `v1_category_classify`;
CREATE TABLE `v1_category_classify`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `category_id` bigint NOT NULL,
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `classify_id` bigint NOT NULL,
  `classify_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cover_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_category_classify
-- ----------------------------

-- ----------------------------
-- Table structure for v1_charge_config
-- ----------------------------
DROP TABLE IF EXISTS `v1_charge_config`;
CREATE TABLE `v1_charge_config`  (
  `id` bigint NOT NULL,
  `upto` bigint NOT NULL,
  `give` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `only_pos` tinyint(1) NOT NULL DEFAULT 0,
  `only_give` tinyint(1) NOT NULL DEFAULT 0,
  `allow_free_order` tinyint(1) NOT NULL DEFAULT 0,
  `sort` bigint NOT NULL,
  `coupon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `card_wallet` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `begin_time` bigint NOT NULL,
  `end_time` bigint NOT NULL,
  `level_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_charge_config
-- ----------------------------

-- ----------------------------
-- Table structure for v1_client_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_client_log`;
CREATE TABLE `v1_client_log`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `system_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `platform` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `brand` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `version_number` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_client_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_contact_detail
-- ----------------------------
DROP TABLE IF EXISTS `v1_contact_detail`;
CREATE TABLE `v1_contact_detail`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `storage_place_id` bigint NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `telephone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_default` int NOT NULL,
  `update_time` bigint NOT NULL,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_contact_detail
-- ----------------------------

-- ----------------------------
-- Table structure for v1_coupon_config
-- ----------------------------
DROP TABLE IF EXISTS `v1_coupon_config`;
CREATE TABLE `v1_coupon_config`  (
  `id` bigint NOT NULL,
  `coupon_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `coupon_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `org_id` bigint NOT NULL,
  `area_id` bigint NOT NULL,
  `amount` int NOT NULL,
  `type` int NOT NULL,
  `status` int NOT NULL,
  `need_show` tinyint(1) NOT NULL DEFAULT 0,
  `claim_per_member` int NOT NULL,
  `total_amount` int NOT NULL,
  `claim_amount` int NOT NULL,
  `id_type` int NOT NULL,
  `rule_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type_rules` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `use_rules` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `upto` bigint NOT NULL,
  `free` bigint NOT NULL,
  `merchant_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_category_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `begin_use_time` bigint NOT NULL,
  `begin_time` bigint NOT NULL,
  `end_time` bigint NOT NULL,
  `expire_days` bigint NOT NULL,
  `expire_time` bigint NOT NULL,
  `old_price` int NOT NULL,
  `current_price` int NOT NULL,
  `update_time` bigint NOT NULL,
  `source` int NOT NULL,
  `combo_coupon_id_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `combo_coupon_title_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_coupon_config
-- ----------------------------

-- ----------------------------
-- Table structure for v1_default_product_param
-- ----------------------------
DROP TABLE IF EXISTS `v1_default_product_param`;
CREATE TABLE `v1_default_product_param`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sys_cate_type_id` bigint NOT NULL,
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_default_product_param
-- ----------------------------

-- ----------------------------
-- Table structure for v1_deliver_fee_change_apply
-- ----------------------------
DROP TABLE IF EXISTS `v1_deliver_fee_change_apply`;
CREATE TABLE `v1_deliver_fee_change_apply`  (
  `id` bigint NOT NULL,
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `change_amount` bigint NOT NULL,
  `area_id` bigint NOT NULL,
  `begin_time` bigint NOT NULL,
  `end_time` bigint NOT NULL,
  `status` int NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_deliver_fee_change_apply
-- ----------------------------

-- ----------------------------
-- Table structure for v1_deliver_fee_change_confirm_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_deliver_fee_change_confirm_log`;
CREATE TABLE `v1_deliver_fee_change_confirm_log`  (
  `id` bigint NOT NULL,
  `apply_id` bigint NOT NULL,
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `change_amount` bigint NOT NULL,
  `area_id` bigint NOT NULL,
  `begin_time` bigint NOT NULL,
  `end_time` bigint NOT NULL,
  `status` int NOT NULL,
  `org_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_deliver_fee_change_confirm_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_delivery_fee
-- ----------------------------
DROP TABLE IF EXISTS `v1_delivery_fee`;
CREATE TABLE `v1_delivery_fee`  (
  `id` bigint NOT NULL,
  `area_id` bigint NOT NULL,
  `shop_place_id` bigint NOT NULL,
  `shop_place_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `storage_place_id` bigint NOT NULL,
  `storage_place_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `default_fee` bigint NOT NULL,
  `driver_fee_json` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_delivery_fee
-- ----------------------------

-- ----------------------------
-- Table structure for v1_deposit
-- ----------------------------
DROP TABLE IF EXISTS `v1_deposit`;
CREATE TABLE `v1_deposit`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `trade_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uid` bigint NOT NULL,
  `config_id` bigint NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `member_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `amount` bigint NOT NULL,
  `give` bigint NOT NULL,
  `real_pay` bigint NOT NULL,
  `status` int NOT NULL,
  `pay_tx` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `account_settle` int NOT NULL,
  `balance_time` bigint NOT NULL,
  `pay_method` int NOT NULL,
  `pay_method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `friend_id` bigint NOT NULL,
  `operator_id` bigint NOT NULL,
  `operator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_id` bigint NOT NULL,
  `shop_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `source` int NOT NULL,
  `mp_status` int NOT NULL,
  `staff_id` bigint NOT NULL,
  `member_level` int NOT NULL,
  `staff_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `staff_award` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_deposit
-- ----------------------------

-- ----------------------------
-- Table structure for v1_dict
-- ----------------------------
DROP TABLE IF EXISTS `v1_dict`;
CREATE TABLE `v1_dict`  (
  `id` bigint NOT NULL,
  `parent_id` bigint NOT NULL,
  `sort` int NOT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `attr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `attr_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pinyin_abbr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `need_show` tinyint(1) NOT NULL DEFAULT 0,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_dict
-- ----------------------------

-- ----------------------------
-- Table structure for v1_district
-- ----------------------------
DROP TABLE IF EXISTS `v1_district`;
CREATE TABLE `v1_district`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deliver_fee` bigint NOT NULL,
  `area_id` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_district
-- ----------------------------

-- ----------------------------
-- Table structure for v1_driver
-- ----------------------------
DROP TABLE IF EXISTS `v1_driver`;
CREATE TABLE `v1_driver`  (
  `id` bigint NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `realname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  `last_time` bigint NOT NULL,
  `last_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `id_card_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `id_card_pic_front` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `id_card_pic_back` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_admin` tinyint(1) NOT NULL DEFAULT 0,
  `enable` int NOT NULL,
  `type` int NOT NULL,
  `area_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_driver
-- ----------------------------

-- ----------------------------
-- Table structure for v1_driver_audit
-- ----------------------------
DROP TABLE IF EXISTS `v1_driver_audit`;
CREATE TABLE `v1_driver_audit`  (
  `id` bigint NOT NULL,
  `audit_status` int NOT NULL,
  `audit_time` bigint NOT NULL,
  `audit_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `driver_id` bigint NOT NULL,
  `driver_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operator_id` bigint NOT NULL,
  `operator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_driver_audit
-- ----------------------------

-- ----------------------------
-- Table structure for v1_driver_balance
-- ----------------------------
DROP TABLE IF EXISTS `v1_driver_balance`;
CREATE TABLE `v1_driver_balance`  (
  `id` bigint NOT NULL,
  `item_id` int NOT NULL,
  `left_balance` bigint NOT NULL,
  `freeze_balance` bigint NOT NULL,
  `total_balance` bigint NOT NULL,
  `real_pay` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `give` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_driver_balance
-- ----------------------------

-- ----------------------------
-- Table structure for v1_driver_feedback_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_driver_feedback_log`;
CREATE TABLE `v1_driver_feedback_log`  (
  `id` bigint NOT NULL,
  `area_id` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `orderId` bigint NOT NULL,
  `driverUid` bigint NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `attach` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_driver_feedback_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_everyday_sign_in
-- ----------------------------
DROP TABLE IF EXISTS `v1_everyday_sign_in`;
CREATE TABLE `v1_everyday_sign_in`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_everyday_sign_in
-- ----------------------------

-- ----------------------------
-- Table structure for v1_item
-- ----------------------------
DROP TABLE IF EXISTS `v1_item`;
CREATE TABLE `v1_item`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_shown` int NOT NULL,
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_item
-- ----------------------------

-- ----------------------------
-- Table structure for v1_login_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_login_log`;
CREATE TABLE `v1_login_log`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `place` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_logistics
-- ----------------------------
DROP TABLE IF EXISTS `v1_logistics`;
CREATE TABLE `v1_logistics`  (
  `id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `express_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `consignee_realname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `consignee_phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `consignee_phone_number2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `consignee_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `consignee_postcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `logistics_type` int NOT NULL,
  `logistics_fee` bigint NOT NULL,
  `logistics_status` int NOT NULL,
  `logistics_settlement_status` int NOT NULL,
  `logistics_last_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `logistics_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `settlement_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_logistics
-- ----------------------------

-- ----------------------------
-- Table structure for v1_mail_fee_config
-- ----------------------------
DROP TABLE IF EXISTS `v1_mail_fee_config`;
CREATE TABLE `v1_mail_fee_config`  (
  `id` int NOT NULL,
  `region_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `region_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `first_weight_fee` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `next_weight_fee` bigint NOT NULL,
  `first_amount_fee` bigint NOT NULL,
  `next_amount_fee` bigint NOT NULL,
  `upto_money_free` bigint NOT NULL,
  `upto_amount_free` bigint NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `method` int NOT NULL,
  `first_km_fee` bigint NOT NULL,
  `next_km_fee` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_mail_fee_config
-- ----------------------------

-- ----------------------------
-- Table structure for v1_member
-- ----------------------------
DROP TABLE IF EXISTS `v1_member`;
CREATE TABLE `v1_member`  (
  `id` bigint NOT NULL,
  `login_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pay_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  `dealer_id` bigint NOT NULL,
  `dealer_type` bigint NOT NULL,
  `level` int NOT NULL,
  `shop_id` bigint NOT NULL,
  `shop_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `org_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_type` int NOT NULL,
  `member_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `birthday` bigint NOT NULL,
  `birthday_month` int NOT NULL,
  `birthday_day` int NOT NULL,
  `barcode_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `session_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `union_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `id_card_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `filter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `inside_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dealer_level` bigint NOT NULL,
  `sex` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_member
-- ----------------------------

-- ----------------------------
-- Table structure for v1_member_balance
-- ----------------------------
DROP TABLE IF EXISTS `v1_member_balance`;
CREATE TABLE `v1_member_balance`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `item_id` int NOT NULL,
  `left_balance` bigint NOT NULL,
  `freeze_balance` bigint NOT NULL,
  `total_balance` bigint NOT NULL,
  `real_pay` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `give` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_member_balance
-- ----------------------------

-- ----------------------------
-- Table structure for v1_member_card_coupon
-- ----------------------------
DROP TABLE IF EXISTS `v1_member_card_coupon`;
CREATE TABLE `v1_member_card_coupon`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `card_coupon_id` bigint NOT NULL,
  `shop_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `digest` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `end_time` bigint NOT NULL,
  `status` bigint NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tx_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sub_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `third_tx_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `barcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pay_type` int NOT NULL,
  `real_pay` int NOT NULL,
  `update_time` bigint NOT NULL,
  `use_time` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `sku_id` bigint NOT NULL,
  `no_limit_count` tinyint(1) NOT NULL DEFAULT 0,
  `use_count` bigint NOT NULL,
  `discount` bigint NOT NULL,
  `begin_time` bigint NOT NULL,
  `only_pick_up` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_member_card_coupon
-- ----------------------------

-- ----------------------------
-- Table structure for v1_member_coupon
-- ----------------------------
DROP TABLE IF EXISTS `v1_member_coupon`;
CREATE TABLE `v1_member_coupon`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `coupon_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `coupon_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `amount` int NOT NULL,
  `rule_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `begin_time` bigint NOT NULL,
  `end_time` bigint NOT NULL,
  `status` bigint NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tx_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sub_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pay_type` int NOT NULL,
  `real_pay` int NOT NULL,
  `update_time` bigint NOT NULL,
  `use_time` bigint NOT NULL,
  `type_rules` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `use_rules` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `area_id` bigint NOT NULL,
  `source` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_member_coupon
-- ----------------------------

-- ----------------------------
-- Table structure for v1_member_level
-- ----------------------------
DROP TABLE IF EXISTS `v1_member_level`;
CREATE TABLE `v1_member_level`  (
  `id` int NOT NULL,
  `need_score` bigint NOT NULL,
  `level` int NOT NULL,
  `level_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sketch` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `order_discount` int NOT NULL,
  `award_ratio` double NOT NULL,
  `coupon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `mail_free` int NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_member_level
-- ----------------------------

-- ----------------------------
-- Table structure for v1_member_score_config
-- ----------------------------
DROP TABLE IF EXISTS `v1_member_score_config`;
CREATE TABLE `v1_member_score_config`  (
  `id` bigint NOT NULL,
  `type` int NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `score` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_member_score_config
-- ----------------------------

-- ----------------------------
-- Table structure for v1_msg
-- ----------------------------
DROP TABLE IF EXISTS `v1_msg`;
CREATE TABLE `v1_msg`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `item_id` int NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `msg_type` int NOT NULL,
  `category_id` int NOT NULL,
  `area_id` bigint NOT NULL,
  `target_id` bigint NOT NULL,
  `status` int NOT NULL,
  `change_amount` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_msg
-- ----------------------------

-- ----------------------------
-- Table structure for v1_news_search
-- ----------------------------
DROP TABLE IF EXISTS `v1_news_search`;
CREATE TABLE `v1_news_search`  (
  `id` bigint NOT NULL,
  `keyword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `views` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_news_search
-- ----------------------------

-- ----------------------------
-- Table structure for v1_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_operation_log`;
CREATE TABLE `v1_operation_log`  (
  `id` bigint NOT NULL,
  `admin_id` bigint NOT NULL,
  `admin_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `place` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order
-- ----------------------------
DROP TABLE IF EXISTS `v1_order`;
CREATE TABLE `v1_order`  (
  `id` bigint NOT NULL,
  `area_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uid` bigint NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `post_service_status` int NOT NULL,
  `mp_status` int NOT NULL,
  `product_count` int NOT NULL,
  `total_money` bigint NOT NULL,
  `total_return_money` bigint NOT NULL,
  `total_return_number` bigint NOT NULL,
  `real_pay` bigint NOT NULL,
  `discount_money` bigint NOT NULL,
  `mail_fee` bigint NOT NULL,
  `packing_fee` bigint NOT NULL,
  `pay_method` int NOT NULL,
  `print_count` int NOT NULL,
  `pay_method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `barcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pickup_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `pay_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `errand_status` int NOT NULL,
  `print_status` int NOT NULL,
  `delivery_type` int NOT NULL,
  `schedule_time` bigint NOT NULL,
  `order_type` int NOT NULL,
  `paid_up` bigint NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `filter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `driver_take_task_time` bigint NOT NULL,
  `driver_pickup_time` bigint NOT NULL,
  `driver_delivered_time` bigint NOT NULL,
  `shop_place_id` bigint NOT NULL,
  `shop_place` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `storage_place_Id` bigint NOT NULL,
  `storage_place` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cabinet_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cabinet_id` bigint NOT NULL,
  `cabinet_box` int NOT NULL,
  `packing_box_use` int NOT NULL,
  `packing_box_money` bigint NOT NULL,
  `packing_bag_money` bigint NOT NULL,
  `packing_box_price` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_commission
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_commission`;
CREATE TABLE `v1_order_commission`  (
  `id` bigint NOT NULL,
  `area_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `total_money` bigint NOT NULL,
  `order_total` bigint NOT NULL,
  `products` bigint NOT NULL,
  `real_pay` bigint NOT NULL,
  `addition_fee` bigint NOT NULL,
  `shop_deliver_fee` bigint NOT NULL,
  `shop_receive_deliver_fee` bigint NOT NULL,
  `packing_fee` bigint NOT NULL,
  `shop_favor` bigint NOT NULL,
  `platform_favor` bigint NOT NULL,
  `status` int NOT NULL,
  `driver_deliver_fee` bigint NOT NULL,
  `commission` bigint NOT NULL,
  `fix_fee` bigint NOT NULL,
  `income` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_commission
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_coupon
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_coupon`;
CREATE TABLE `v1_order_coupon`  (
  `id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `member_coupon_id` bigint NOT NULL,
  `coupon_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `coupon_free` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_coupon
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_dealer
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_dealer`;
CREATE TABLE `v1_order_dealer`  (
  `id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `award_type` int NOT NULL,
  `real_pay` bigint NOT NULL,
  `award_amount` bigint NOT NULL,
  `dealer_id` bigint NOT NULL,
  `dealer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_id` bigint NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `order_settlement_time` bigint NOT NULL,
  `commission_handled` tinyint(1) NOT NULL DEFAULT 0,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_dealer
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_delivery
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_delivery`;
CREATE TABLE `v1_order_delivery`  (
  `id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `delivery_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `express_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `express_company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uid` bigint NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operator_id` bigint NOT NULL,
  `operator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_delivery
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_delivery_detail
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_delivery_detail`;
CREATE TABLE `v1_order_delivery_detail`  (
  `id` bigint NOT NULL,
  `order_delivery_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `order_detail_id` bigint NOT NULL,
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sku_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_price` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_delivery_detail
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_delivery_form
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_delivery_form`;
CREATE TABLE `v1_order_delivery_form`  (
  `id` bigint NOT NULL,
  `delivery_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uid` bigint NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operator_id` bigint NOT NULL,
  `operator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `express_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `express_company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_delivery_form
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_detail`;
CREATE TABLE `v1_order_detail`  (
  `id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `shop_id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `category_id` bigint NOT NULL,
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `purchasing_category_id` bigint NOT NULL,
  `purchasing_category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `old_price` bigint NOT NULL,
  `weight` double NOT NULL,
  `product_price` bigint NOT NULL,
  `purchasing_price` bigint NOT NULL,
  `result_price` bigint NOT NULL,
  `sku_id` bigint NOT NULL,
  `sku_type` int NOT NULL,
  `sku_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_mode_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_mode_params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `discount_rate` int NOT NULL,
  `discount_amount` bigint NOT NULL,
  `number` bigint NOT NULL,
  `storage_number` bigint NOT NULL,
  `sub_total` bigint NOT NULL,
  `already_delivery_number` bigint NOT NULL,
  `is_product_available` tinyint(1) NOT NULL DEFAULT 0,
  `exclude_discount` tinyint(1) NOT NULL DEFAULT 0,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `source` int NOT NULL,
  `delivery_method` int NOT NULL,
  `return_status` bigint NOT NULL,
  `sub_return` bigint NOT NULL,
  `return_number` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `take_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `sku_quantity` bigint NOT NULL,
  `staff_id` bigint NOT NULL,
  `status` bigint NOT NULL,
  `supplier_id` bigint NOT NULL,
  `bid_price` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_detail
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_favor_detail
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_favor_detail`;
CREATE TABLE `v1_order_favor_detail`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `score_gave` bigint NOT NULL,
  `score_use` bigint NOT NULL,
  `score_to_money` bigint NOT NULL,
  `coupon_id` bigint NOT NULL,
  `member_card_coupon_id` bigint NOT NULL,
  `coupon_free` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_favor_detail
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_gift
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_gift`;
CREATE TABLE `v1_order_gift`  (
  `id` bigint NOT NULL,
  `upto` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `sku_id` bigint NOT NULL,
  `amount` int NOT NULL,
  `gave_amount` bigint NOT NULL,
  `total_amount` bigint NOT NULL,
  `require_first_order` tinyint(1) NOT NULL DEFAULT 0,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `begin_time` bigint NOT NULL,
  `end_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_gift
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_log`;
CREATE TABLE `v1_order_log`  (
  `id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `old_status` int NOT NULL,
  `new_status` int NOT NULL,
  `operator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_notify
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_notify`;
CREATE TABLE `v1_order_notify`  (
  `id` bigint NOT NULL,
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `order_time` bigint NOT NULL,
  `shop_id` bigint NOT NULL,
  `shop_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `have_notify` tinyint(1) NOT NULL DEFAULT 0,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_notify
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_pay_detail
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_pay_detail`;
CREATE TABLE `v1_order_pay_detail`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `shop_id` bigint NOT NULL,
  `pay_method` int NOT NULL,
  `pay_method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `out_trade_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pay_tx_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pay_time` bigint NOT NULL,
  `refund_tx_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pay_detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `paid_up` bigint NOT NULL,
  `account_settle` int NOT NULL,
  `cost` bigint NOT NULL,
  `refund_status` int NOT NULL,
  `refund_operator_id` bigint NOT NULL,
  `refund_operator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_pay_detail
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_print_detail
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_print_detail`;
CREATE TABLE `v1_order_print_detail`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uid` bigint NOT NULL,
  `source` int NOT NULL,
  `print_status` int NOT NULL,
  `print_result_data` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_print_detail
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_read_status
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_read_status`;
CREATE TABLE `v1_order_read_status`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `has_read` tinyint(1) NOT NULL DEFAULT 0,
  `uid` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_read_status
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_receive_detail
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_receive_detail`;
CREATE TABLE `v1_order_receive_detail`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `contact_detail_id` bigint NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `delivery_time` bigint NOT NULL,
  `express_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `express_company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `delivery_type` int NOT NULL,
  `pickup_note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `schedule_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pick_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `table_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dine_in_numbers` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operator_id` bigint NOT NULL,
  `need_bag` tinyint(1) NOT NULL DEFAULT 0,
  `operator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `distance` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_receive_detail
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_return_delivery_form
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_return_delivery_form`;
CREATE TABLE `v1_order_return_delivery_form`  (
  `id` bigint NOT NULL,
  `delivery_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `return_apply_id` bigint NOT NULL,
  `return_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uid` bigint NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operator_id` bigint NOT NULL,
  `operator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `express_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `express_company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_return_delivery_form
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_return_refund_form
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_return_refund_form`;
CREATE TABLE `v1_order_return_refund_form`  (
  `id` bigint NOT NULL,
  `refund_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `return_apply_id` bigint NOT NULL,
  `return_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uid` bigint NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operator_id` bigint NOT NULL,
  `operator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `refund_money` bigint NOT NULL,
  `refund_method` bigint NOT NULL,
  `status` int NOT NULL,
  `pay_method` int NOT NULL,
  `tx_data` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_return_refund_form
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_returns
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_returns`;
CREATE TABLE `v1_order_returns`  (
  `id` bigint NOT NULL,
  `returns_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `return_tx` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `order_detail_id` bigint NOT NULL,
  `express_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `consignee_realname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `consignee_phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `consignee_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `consignee_postcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `logis_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `state` int NOT NULL,
  `pre_status` int NOT NULL,
  `status` int NOT NULL,
  `uid` bigint NOT NULL,
  `operator_id` bigint NOT NULL,
  `operator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `logistics_last_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `logistics_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `return_type` int NOT NULL,
  `handling_way` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `return_money` bigint NOT NULL,
  `return_submit_time` bigint NOT NULL,
  `handling_return_time` bigint NOT NULL,
  `return_submit_end_time` bigint NOT NULL,
  `handling_apply_end_time` bigint NOT NULL,
  `handling_return_end_time` bigint NOT NULL,
  `handling_refund_end_time` bigint NOT NULL,
  `handling_refund_time` bigint NOT NULL,
  `refund_resp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `audit_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `audit_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `total_return_number` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_returns
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_returns_apply
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_returns_apply`;
CREATE TABLE `v1_order_returns_apply`  (
  `id` bigint NOT NULL,
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `order_detail_id` bigint NOT NULL,
  `returns_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uid` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `state` int NOT NULL,
  `delivery_status` int NOT NULL,
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `return_submit_time` bigint NOT NULL,
  `audit_time` bigint NOT NULL,
  `audit_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operator_id` bigint NOT NULL,
  `operator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_returns_apply
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_returns_detail
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_returns_detail`;
CREATE TABLE `v1_order_returns_detail`  (
  `id` bigint NOT NULL,
  `return_id` bigint NOT NULL,
  `order_detail_id` bigint NOT NULL,
  `old_price` bigint NOT NULL,
  `product_price` bigint NOT NULL,
  `return_amount` bigint NOT NULL,
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sku_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_returns_detail
-- ----------------------------

-- ----------------------------
-- Table structure for v1_order_returns_img
-- ----------------------------
DROP TABLE IF EXISTS `v1_order_returns_img`;
CREATE TABLE `v1_order_returns_img`  (
  `id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `order_detail_id` bigint NOT NULL,
  `return_apply_id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_order_returns_img
-- ----------------------------

-- ----------------------------
-- Table structure for v1_org
-- ----------------------------
DROP TABLE IF EXISTS `v1_org`;
CREATE TABLE `v1_org`  (
  `id` bigint NOT NULL,
  `status` int NOT NULL,
  `area_id` bigint NOT NULL,
  `score` int NOT NULL,
  `sold_amount` bigint NOT NULL,
  `min_to_send` int NOT NULL,
  `delivery_target` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `area_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `digest` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `license_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `license_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_amount` bigint NOT NULL,
  `shop_max_amount` bigint NOT NULL,
  `sort` bigint NOT NULL,
  `create_uid` bigint NOT NULL,
  `create_name` bigint NOT NULL,
  `charge_time` bigint NOT NULL,
  `renew_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `filter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `rect_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `company_type` int NOT NULL,
  `license_type` int NOT NULL,
  `business_items` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `open_time` int NOT NULL,
  `close_time` int NOT NULL,
  `open_time_minute` int NOT NULL,
  `close_time_minute` int NOT NULL,
  `shop_place_id` bigint NOT NULL,
  `lat` double NOT NULL,
  `lon` double NOT NULL,
  `shop_place_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `floor` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `recommend` int NOT NULL,
  `packing_fee` int NOT NULL,
  `packing_box_price` bigint NOT NULL,
  `delivery_fee` int NOT NULL,
  `time_to_deliver` bigint NOT NULL,
  `min_deliver_fee` int NOT NULL,
  `announcement` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `enable` int NOT NULL,
  `bank_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `bank_account_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `bank_card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `commission_percent` double NOT NULL,
  `delivery_type` int NOT NULL,
  `service_fee` bigint NOT NULL,
  `same_district_deliver_fee` bigint NOT NULL,
  `diff_district_deliver_fee` bigint NOT NULL,
  `receiver_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `profit_share` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_org
-- ----------------------------

-- ----------------------------
-- Table structure for v1_org_charge
-- ----------------------------
DROP TABLE IF EXISTS `v1_org_charge`;
CREATE TABLE `v1_org_charge`  (
  `id` bigint NOT NULL,
  `charge_time` bigint NOT NULL,
  `charge_amount` bigint NOT NULL,
  `renew_time` bigint NOT NULL,
  `operator_name` bigint NOT NULL,
  `proof` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_org_charge
-- ----------------------------

-- ----------------------------
-- Table structure for v1_out_stock_reg
-- ----------------------------
DROP TABLE IF EXISTS `v1_out_stock_reg`;
CREATE TABLE `v1_out_stock_reg`  (
  `id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `sku_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_out_stock_reg
-- ----------------------------

-- ----------------------------
-- Table structure for v1_pay_method
-- ----------------------------
DROP TABLE IF EXISTS `v1_pay_method`;
CREATE TABLE `v1_pay_method`  (
  `id` bigint NOT NULL,
  `sort` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `value` int NOT NULL,
  `pinyin_abbr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `need_show` tinyint(1) NOT NULL DEFAULT 0,
  `free_pay` tinyint(1) NOT NULL DEFAULT 0,
  `org_id` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_pay_method
-- ----------------------------

-- ----------------------------
-- Table structure for v1_poster
-- ----------------------------
DROP TABLE IF EXISTS `v1_poster`;
CREATE TABLE `v1_poster`  (
  `id` bigint NOT NULL,
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `publish_date` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_poster
-- ----------------------------

-- ----------------------------
-- Table structure for v1_print_template
-- ----------------------------
DROP TABLE IF EXISTS `v1_print_template`;
CREATE TABLE `v1_print_template`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `template` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_print_template
-- ----------------------------

-- ----------------------------
-- Table structure for v1_product
-- ----------------------------
DROP TABLE IF EXISTS `v1_product`;
CREATE TABLE `v1_product`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `area_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `category_id` bigint NOT NULL,
  `activity_category_id` bigint NOT NULL,
  `purchasing_category_id` bigint NOT NULL,
  `type_id` bigint NOT NULL,
  `mail_fee_id` bigint NOT NULL,
  `sketch` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `details` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `keywords` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `marque` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `barcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `price` bigint NOT NULL,
  `old_price` bigint NOT NULL,
  `min_price` bigint NOT NULL,
  `min_order_amount` bigint NOT NULL,
  `max_price` bigint NOT NULL,
  `sold_amount` bigint NOT NULL,
  `virtual_amount` bigint NOT NULL,
  `weight` double NOT NULL,
  `volume` double NOT NULL,
  `cover_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `poster` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `sort` int NOT NULL,
  `stock` bigint NOT NULL,
  `stock_mode` int NOT NULL,
  `warning_stock` bigint NOT NULL,
  `cover_frame_id` bigint NOT NULL,
  `cover_frame_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `delivery_methods` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deleted_at` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `enable_mail` tinyint(1) NOT NULL DEFAULT 0,
  `enable_all_region` tinyint(1) NOT NULL DEFAULT 0,
  `only_pos` tinyint(1) NOT NULL DEFAULT 0,
  `enable_free_mail` tinyint(1) NOT NULL DEFAULT 0,
  `sku` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `attr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `album` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dealer_award_json` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `calc_by` int NOT NULL,
  `packing_fee` int NOT NULL,
  `packing_use` double NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_product
-- ----------------------------

-- ----------------------------
-- Table structure for v1_product_classify
-- ----------------------------
DROP TABLE IF EXISTS `v1_product_classify`;
CREATE TABLE `v1_product_classify`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `classify_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sub_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `head_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `bg_color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `font_color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `classify_type` int NOT NULL,
  `height` int NOT NULL,
  `item_size` int NOT NULL,
  `sort` bigint NOT NULL,
  `product_count` int NOT NULL,
  `parent_id` bigint NOT NULL,
  `is_parent` tinyint(1) NOT NULL DEFAULT 0,
  `activity_type` int NOT NULL,
  `product_id_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_product_classify
-- ----------------------------

-- ----------------------------
-- Table structure for v1_product_cover_frame
-- ----------------------------
DROP TABLE IF EXISTS `v1_product_cover_frame`;
CREATE TABLE `v1_product_cover_frame`  (
  `id` int NOT NULL,
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_product_cover_frame
-- ----------------------------

-- ----------------------------
-- Table structure for v1_product_default_recommend
-- ----------------------------
DROP TABLE IF EXISTS `v1_product_default_recommend`;
CREATE TABLE `v1_product_default_recommend`  (
  `id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_product_default_recommend
-- ----------------------------

-- ----------------------------
-- Table structure for v1_product_fav
-- ----------------------------
DROP TABLE IF EXISTS `v1_product_fav`;
CREATE TABLE `v1_product_fav`  (
  `id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `sku_id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_product_fav
-- ----------------------------

-- ----------------------------
-- Table structure for v1_product_recommend
-- ----------------------------
DROP TABLE IF EXISTS `v1_product_recommend`;
CREATE TABLE `v1_product_recommend`  (
  `id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `recommend_product_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_product_recommend
-- ----------------------------

-- ----------------------------
-- Table structure for v1_product_relate
-- ----------------------------
DROP TABLE IF EXISTS `v1_product_relate`;
CREATE TABLE `v1_product_relate`  (
  `id` bigint NOT NULL,
  `combo_product_id` bigint NOT NULL,
  `relate_product_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_product_relate
-- ----------------------------

-- ----------------------------
-- Table structure for v1_product_search
-- ----------------------------
DROP TABLE IF EXISTS `v1_product_search`;
CREATE TABLE `v1_product_search`  (
  `id` bigint NOT NULL,
  `keyword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `views` bigint NOT NULL,
  `shop_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_product_search
-- ----------------------------

-- ----------------------------
-- Table structure for v1_product_tab_classify
-- ----------------------------
DROP TABLE IF EXISTS `v1_product_tab_classify`;
CREATE TABLE `v1_product_tab_classify`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `product_tab_id` bigint NOT NULL,
  `classify_id` bigint NOT NULL,
  `classify_cover_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `classify_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_product_tab_classify
-- ----------------------------

-- ----------------------------
-- Table structure for v1_product_tag
-- ----------------------------
DROP TABLE IF EXISTS `v1_product_tag`;
CREATE TABLE `v1_product_tag`  (
  `id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_product_tag
-- ----------------------------

-- ----------------------------
-- Table structure for v1_product_uni_img
-- ----------------------------
DROP TABLE IF EXISTS `v1_product_uni_img`;
CREATE TABLE `v1_product_uni_img`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `rules` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_product_uni_img
-- ----------------------------

-- ----------------------------
-- Table structure for v1_product_views
-- ----------------------------
DROP TABLE IF EXISTS `v1_product_views`;
CREATE TABLE `v1_product_views`  (
  `id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `views` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_product_views
-- ----------------------------

-- ----------------------------
-- Table structure for v1_region
-- ----------------------------
DROP TABLE IF EXISTS `v1_region`;
CREATE TABLE `v1_region`  (
  `id` int NOT NULL,
  `region_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `region_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `parent_id` int NOT NULL,
  `region_level` int NOT NULL,
  `region_order` int NOT NULL,
  `region_name_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `region_short_name_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_region
-- ----------------------------

-- ----------------------------
-- Table structure for v1_return_contact_detail
-- ----------------------------
DROP TABLE IF EXISTS `v1_return_contact_detail`;
CREATE TABLE `v1_return_contact_detail`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `province_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `city_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `area_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `details` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `postcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `telephone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_return_contact_detail
-- ----------------------------

-- ----------------------------
-- Table structure for v1_score_category
-- ----------------------------
DROP TABLE IF EXISTS `v1_score_category`;
CREATE TABLE `v1_score_category`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `parent_id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `poster` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pinyin_abbr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `path_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_shown` int NOT NULL,
  `exclude` tinyint(1) NOT NULL DEFAULT 0,
  `need_time_to_make` tinyint(1) NOT NULL DEFAULT 0,
  `sort` int NOT NULL,
  `category_type` int NOT NULL,
  `sold_amount` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_score_category
-- ----------------------------

-- ----------------------------
-- Table structure for v1_score_product
-- ----------------------------
DROP TABLE IF EXISTS `v1_score_product`;
CREATE TABLE `v1_score_product`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `org_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `category_id` bigint NOT NULL,
  `purchasing_category_id` bigint NOT NULL,
  `type_id` bigint NOT NULL,
  `mail_fee_id` bigint NOT NULL,
  `is_virtual` tinyint(1) NOT NULL DEFAULT 0,
  `coupon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sketch` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `details` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `keywords` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `marque` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `barcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `score` bigint NOT NULL,
  `convert_amount` bigint NOT NULL,
  `old_score` bigint NOT NULL,
  `min_score` bigint NOT NULL,
  `max_score` bigint NOT NULL,
  `sold_amount` bigint NOT NULL,
  `virtual_amount` bigint NOT NULL,
  `weight` double NOT NULL,
  `volume` double NOT NULL,
  `cover_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `poster` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `sort` int NOT NULL,
  `stock` bigint NOT NULL,
  `warning_stock` bigint NOT NULL,
  `cover_frame_id` bigint NOT NULL,
  `cover_frame_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `delivery_methods` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deleted_at` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `inner_use` tinyint(1) NOT NULL DEFAULT 0,
  `enable_mail` tinyint(1) NOT NULL DEFAULT 0,
  `enable_all_region` tinyint(1) NOT NULL DEFAULT 0,
  `enable_free_mail` tinyint(1) NOT NULL DEFAULT 0,
  `sku` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `attr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `album` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_score_product
-- ----------------------------

-- ----------------------------
-- Table structure for v1_search_keyword
-- ----------------------------
DROP TABLE IF EXISTS `v1_search_keyword`;
CREATE TABLE `v1_search_keyword`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `source` int NOT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `keyword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_search_keyword
-- ----------------------------

-- ----------------------------
-- Table structure for v1_search_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_search_log`;
CREATE TABLE `v1_search_log`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `keyword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `views` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_search_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop`;
CREATE TABLE `v1_shop`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `run_type` int NOT NULL,
  `shop_level` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `digest` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `license_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `license_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `approve_note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `log` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `creator_id` bigint NOT NULL,
  `approver_id` bigint NOT NULL,
  `lat` double NOT NULL,
  `lon` double NOT NULL,
  `open_time` int NOT NULL,
  `close_time` int NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `auth_sec` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `filter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `business_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `rect_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_counts` bigint NOT NULL,
  `views` bigint NOT NULL,
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `images` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `discount_str` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `branches` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `discount` int NOT NULL,
  `bid_discount` int NOT NULL,
  `average_consumption` int NOT NULL,
  `order_count` bigint NOT NULL,
  `sort` int NOT NULL,
  `place_top` tinyint(1) NOT NULL DEFAULT 0,
  `bulletin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `env_images` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `open_time_minute` int NOT NULL,
  `close_time_minute` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_admin
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_admin`;
CREATE TABLE `v1_shop_admin`  (
  `id` bigint NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `realname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  `last_time` bigint NOT NULL,
  `last_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_admin` tinyint(1) NOT NULL DEFAULT 0,
  `org_id` bigint NOT NULL,
  `area_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_id` bigint NOT NULL,
  `shop_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `rules` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pinyin_abbr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `bg_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_admin
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_balance
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_balance`;
CREATE TABLE `v1_shop_balance`  (
  `id` bigint NOT NULL,
  `item_id` int NOT NULL,
  `left_balance` bigint NOT NULL,
  `freeze_balance` bigint NOT NULL,
  `total_balance` bigint NOT NULL,
  `real_pay` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `give` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_balance
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_carousel
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_carousel`;
CREATE TABLE `v1_shop_carousel`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `mobile_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `mobile_link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `client_type` int NOT NULL,
  `biz_type` int NOT NULL,
  `sort` int NOT NULL,
  `need_show` tinyint(1) NOT NULL DEFAULT 0,
  `title1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `title2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tab_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `shop_id` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_carousel
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_category
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_category`;
CREATE TABLE `v1_shop_category`  (
  `id` bigint NOT NULL,
  `shop_id` bigint NOT NULL,
  `parent_id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `poster` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pinyin_abbr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_shown` int NOT NULL,
  `sort` int NOT NULL,
  `sold_amount` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_category
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_config
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_config`;
CREATE TABLE `v1_shop_config`  (
  `id` int NOT NULL,
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `org_id` bigint NOT NULL,
  `shop_id` bigint NOT NULL,
  `is_encrypt` tinyint(1) NOT NULL DEFAULT 0,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_config
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_delivered_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_delivered_log`;
CREATE TABLE `v1_shop_delivered_log`  (
  `id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `shop_admin_id` bigint NOT NULL,
  `member_id` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_delivered_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_fav
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_fav`;
CREATE TABLE `v1_shop_fav`  (
  `id` bigint NOT NULL,
  `shop_id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_fav
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_finish_meal
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_finish_meal`;
CREATE TABLE `v1_shop_finish_meal`  (
  `id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `shop_admin_id` bigint NOT NULL,
  `member_id` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_finish_meal
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_place
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_place`;
CREATE TABLE `v1_shop_place`  (
  `id` bigint NOT NULL,
  `status` int NOT NULL,
  `area_id` bigint NOT NULL,
  `area_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `lat` double NOT NULL,
  `lon` double NOT NULL,
  `district` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `district_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_place
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_printer
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_printer`;
CREATE TABLE `v1_shop_printer`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `shop_id` bigint NOT NULL,
  `printer_type` int NOT NULL,
  `shop_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `printer_sn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `printer_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `filter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `update_time` bigint NOT NULL,
  `sort` int NOT NULL,
  `only_products` tinyint(1) NOT NULL DEFAULT 0,
  `category_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_printer
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_search
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_search`;
CREATE TABLE `v1_shop_search`  (
  `id` bigint NOT NULL,
  `keyword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `views` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_search
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_search_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_search_log`;
CREATE TABLE `v1_shop_search_log`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `keyword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `views` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_search_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_self_took_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_self_took_log`;
CREATE TABLE `v1_shop_self_took_log`  (
  `id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `shop_admin_id` bigint NOT NULL,
  `member_id` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_self_took_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_sku_attr
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_sku_attr`;
CREATE TABLE `v1_shop_sku_attr`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sku_template_id` bigint NOT NULL,
  `shop_id` bigint NOT NULL,
  `sort` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_sku_attr
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_sku_attr_option
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_sku_attr_option`;
CREATE TABLE `v1_shop_sku_attr_option`  (
  `id` bigint NOT NULL,
  `attr_id` bigint NOT NULL,
  `option_id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_sku_attr_option
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_sku_template
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_sku_template`;
CREATE TABLE `v1_shop_sku_template`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `org_id` bigint NOT NULL,
  `attr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_sku_template
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_tag
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_tag`;
CREATE TABLE `v1_shop_tag`  (
  `id` bigint NOT NULL,
  `shop_id` bigint NOT NULL,
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_tag
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shop_take_order_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_take_order_log`;
CREATE TABLE `v1_shop_take_order_log`  (
  `id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `shop_admin_id` bigint NOT NULL,
  `member_id` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shop_take_order_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `v1_shopping_cart`;
CREATE TABLE `v1_shopping_cart`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `sku_id` bigint NOT NULL,
  `amount` bigint NOT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `update_time` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `source` int NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_shopping_cart
-- ----------------------------

-- ----------------------------
-- Table structure for v1_show_case
-- ----------------------------
DROP TABLE IF EXISTS `v1_show_case`;
CREATE TABLE `v1_show_case`  (
  `id` int NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `images` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `image_count` bigint NOT NULL,
  `shop_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_show_case
-- ----------------------------

-- ----------------------------
-- Table structure for v1_sku_category_type
-- ----------------------------
DROP TABLE IF EXISTS `v1_sku_category_type`;
CREATE TABLE `v1_sku_category_type`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `attr_count` int NOT NULL,
  `param_count` int NOT NULL,
  `sort` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_sku_category_type
-- ----------------------------

-- ----------------------------
-- Table structure for v1_sms_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_sms_log`;
CREATE TABLE `v1_sms_log`  (
  `id` bigint NOT NULL,
  `msg_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `extno` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `req_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `resp_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `org_id` bigint NOT NULL,
  `req_time` bigint NOT NULL,
  `resp_time` bigint NOT NULL,
  `query_resp_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_sms_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_sms_template
-- ----------------------------
DROP TABLE IF EXISTS `v1_sms_template`;
CREATE TABLE `v1_sms_template`  (
  `id` bigint NOT NULL,
  `sort` int NOT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `template_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_sms_template
-- ----------------------------

-- ----------------------------
-- Table structure for v1_staff_month_salary
-- ----------------------------
DROP TABLE IF EXISTS `v1_staff_month_salary`;
CREATE TABLE `v1_staff_month_salary`  (
  `id` bigint NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uid` bigint NOT NULL,
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `org_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_id` bigint NOT NULL,
  `shop_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `work_days` bigint NOT NULL,
  `enroll_time` bigint NOT NULL,
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `basic_salary` bigint NOT NULL,
  `fix_salary` bigint NOT NULL,
  `dine_allowance` bigint NOT NULL,
  `social_assurance_allowance` bigint NOT NULL,
  `traffic_allowance` bigint NOT NULL,
  `salary_sub_total` bigint NOT NULL,
  `full_attendance_award` bigint NOT NULL,
  `age_award` bigint NOT NULL,
  `member_award` bigint NOT NULL,
  `product_award` bigint NOT NULL,
  `award_sub_total` bigint NOT NULL,
  `payroll_salary` bigint NOT NULL,
  `social_assurance_deduction` bigint NOT NULL,
  `attendance_deduction` bigint NOT NULL,
  `other_deduction` bigint NOT NULL,
  `broken_deduction` bigint NOT NULL,
  `total_deduction` bigint NOT NULL,
  `pay_advance_salaray` bigint NOT NULL,
  `bonus` bigint NOT NULL,
  `net_salary` bigint NOT NULL,
  `bank_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `bank_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `month` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_staff_month_salary
-- ----------------------------

-- ----------------------------
-- Table structure for v1_staff_month_work_day
-- ----------------------------
DROP TABLE IF EXISTS `v1_staff_month_work_day`;
CREATE TABLE `v1_staff_month_work_day`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `month` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `work_day` int NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_staff_month_work_day
-- ----------------------------

-- ----------------------------
-- Table structure for v1_staff_salary
-- ----------------------------
DROP TABLE IF EXISTS `v1_staff_salary`;
CREATE TABLE `v1_staff_salary`  (
  `id` bigint NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uid` bigint NOT NULL,
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `org_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_id` bigint NOT NULL,
  `shop_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `enroll_time` bigint NOT NULL,
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `basic_salary` bigint NOT NULL,
  `fix_salary` bigint NOT NULL,
  `dine_allowance` bigint NOT NULL,
  `social_assurance_allowance` bigint NOT NULL,
  `traffic_allowance` bigint NOT NULL,
  `full_attendance_award` bigint NOT NULL,
  `age_award` bigint NOT NULL,
  `bank_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `bank_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_staff_salary
-- ----------------------------

-- ----------------------------
-- Table structure for v1_stat_area_day
-- ----------------------------
DROP TABLE IF EXISTS `v1_stat_area_day`;
CREATE TABLE `v1_stat_area_day`  (
  `id` bigint NOT NULL,
  `day` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `area_id` bigint NOT NULL,
  `area_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `products` bigint NOT NULL,
  `orders` bigint NOT NULL,
  `total_money` bigint NOT NULL,
  `order_total` bigint NOT NULL,
  `real_pay` bigint NOT NULL,
  `addition_fee` bigint NOT NULL,
  `shop_deliver_fee` bigint NOT NULL,
  `shop_receive_deliver_fee` bigint NOT NULL,
  `packing_fee` bigint NOT NULL,
  `shop_favor` bigint NOT NULL,
  `platform_favor` bigint NOT NULL,
  `driver_deliver_fee` bigint NOT NULL,
  `commission` bigint NOT NULL,
  `fix_fee` bigint NOT NULL,
  `shop_income` bigint NOT NULL,
  `platform_income` bigint NOT NULL,
  `reg_count` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_stat_area_day
-- ----------------------------

-- ----------------------------
-- Table structure for v1_stat_area_month
-- ----------------------------
DROP TABLE IF EXISTS `v1_stat_area_month`;
CREATE TABLE `v1_stat_area_month`  (
  `id` bigint NOT NULL,
  `month` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `area_id` bigint NOT NULL,
  `area_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `products` bigint NOT NULL,
  `orders` bigint NOT NULL,
  `total_money` bigint NOT NULL,
  `order_total` bigint NOT NULL,
  `real_pay` bigint NOT NULL,
  `addition_fee` bigint NOT NULL,
  `shop_deliver_fee` bigint NOT NULL,
  `shop_receive_deliver_fee` bigint NOT NULL,
  `packing_fee` bigint NOT NULL,
  `shop_favor` bigint NOT NULL,
  `platform_favor` bigint NOT NULL,
  `driver_deliver_fee` bigint NOT NULL,
  `commission` bigint NOT NULL,
  `fix_fee` bigint NOT NULL,
  `shop_income` bigint NOT NULL,
  `platform_income` bigint NOT NULL,
  `reg_count` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_stat_area_month
-- ----------------------------

-- ----------------------------
-- Table structure for v1_stat_platform_day
-- ----------------------------
DROP TABLE IF EXISTS `v1_stat_platform_day`;
CREATE TABLE `v1_stat_platform_day`  (
  `id` bigint NOT NULL,
  `day` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `products` bigint NOT NULL,
  `orders` bigint NOT NULL,
  `total_money` bigint NOT NULL,
  `order_total` bigint NOT NULL,
  `real_pay` bigint NOT NULL,
  `addition_fee` bigint NOT NULL,
  `shop_deliver_fee` bigint NOT NULL,
  `shop_receive_deliver_fee` bigint NOT NULL,
  `packing_fee` bigint NOT NULL,
  `shop_favor` bigint NOT NULL,
  `platform_favor` bigint NOT NULL,
  `driver_deliver_fee` bigint NOT NULL,
  `commission` bigint NOT NULL,
  `fix_fee` bigint NOT NULL,
  `shop_income` bigint NOT NULL,
  `platform_income` bigint NOT NULL,
  `reg_count` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_stat_platform_day
-- ----------------------------

-- ----------------------------
-- Table structure for v1_stat_platform_month
-- ----------------------------
DROP TABLE IF EXISTS `v1_stat_platform_month`;
CREATE TABLE `v1_stat_platform_month`  (
  `id` bigint NOT NULL,
  `month` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `products` bigint NOT NULL,
  `orders` bigint NOT NULL,
  `total_money` bigint NOT NULL,
  `order_total` bigint NOT NULL,
  `real_pay` bigint NOT NULL,
  `addition_fee` bigint NOT NULL,
  `shop_deliver_fee` bigint NOT NULL,
  `shop_receive_deliver_fee` bigint NOT NULL,
  `packing_fee` bigint NOT NULL,
  `shop_favor` bigint NOT NULL,
  `platform_favor` bigint NOT NULL,
  `driver_deliver_fee` bigint NOT NULL,
  `commission` bigint NOT NULL,
  `fix_fee` bigint NOT NULL,
  `shop_income` bigint NOT NULL,
  `platform_income` bigint NOT NULL,
  `reg_count` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_stat_platform_month
-- ----------------------------

-- ----------------------------
-- Table structure for v1_stat_shop_day
-- ----------------------------
DROP TABLE IF EXISTS `v1_stat_shop_day`;
CREATE TABLE `v1_stat_shop_day`  (
  `id` bigint NOT NULL,
  `day` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `area_id` bigint NOT NULL,
  `area_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `org_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `products` bigint NOT NULL,
  `orders` bigint NOT NULL,
  `total_money` bigint NOT NULL,
  `order_total` bigint NOT NULL,
  `real_pay` bigint NOT NULL,
  `addition_fee` bigint NOT NULL,
  `shop_deliver_fee` bigint NOT NULL,
  `shop_receive_deliver_fee` bigint NOT NULL,
  `packing_fee` bigint NOT NULL,
  `shop_favor` bigint NOT NULL,
  `platform_favor` bigint NOT NULL,
  `driver_deliver_fee` bigint NOT NULL,
  `commission` bigint NOT NULL,
  `fix_fee` bigint NOT NULL,
  `shop_income` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_stat_shop_day
-- ----------------------------

-- ----------------------------
-- Table structure for v1_stat_shop_month
-- ----------------------------
DROP TABLE IF EXISTS `v1_stat_shop_month`;
CREATE TABLE `v1_stat_shop_month`  (
  `id` bigint NOT NULL,
  `month` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `area_id` bigint NOT NULL,
  `area_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `org_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `products` bigint NOT NULL,
  `orders` bigint NOT NULL,
  `total_money` bigint NOT NULL,
  `order_total` bigint NOT NULL,
  `real_pay` bigint NOT NULL,
  `addition_fee` bigint NOT NULL,
  `shop_deliver_fee` bigint NOT NULL,
  `shop_receive_deliver_fee` bigint NOT NULL,
  `packing_fee` bigint NOT NULL,
  `shop_favor` bigint NOT NULL,
  `platform_favor` bigint NOT NULL,
  `driver_deliver_fee` bigint NOT NULL,
  `commission` bigint NOT NULL,
  `fix_fee` bigint NOT NULL,
  `shop_income` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_stat_shop_month
-- ----------------------------

-- ----------------------------
-- Table structure for v1_storage_place
-- ----------------------------
DROP TABLE IF EXISTS `v1_storage_place`;
CREATE TABLE `v1_storage_place`  (
  `id` bigint NOT NULL,
  `status` int NOT NULL,
  `area_id` bigint NOT NULL,
  `area_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `lat` double NOT NULL,
  `sort` bigint NOT NULL,
  `lon` double NOT NULL,
  `cabinet_id` bigint NOT NULL,
  `district` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `district_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_storage_place
-- ----------------------------

-- ----------------------------
-- Table structure for v1_suggestion
-- ----------------------------
DROP TABLE IF EXISTS `v1_suggestion`;
CREATE TABLE `v1_suggestion`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uid` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `status` int NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_suggestion
-- ----------------------------

-- ----------------------------
-- Table structure for v1_system_attr
-- ----------------------------
DROP TABLE IF EXISTS `v1_system_attr`;
CREATE TABLE `v1_system_attr`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sys_cate_type_id` bigint NOT NULL,
  `sort` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_system_attr
-- ----------------------------

-- ----------------------------
-- Table structure for v1_system_attr_option
-- ----------------------------
DROP TABLE IF EXISTS `v1_system_attr_option`;
CREATE TABLE `v1_system_attr_option`  (
  `id` bigint NOT NULL,
  `attr_id` bigint NOT NULL,
  `option_id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_system_attr_option
-- ----------------------------

-- ----------------------------
-- Table structure for v1_system_carousel
-- ----------------------------
DROP TABLE IF EXISTS `v1_system_carousel`;
CREATE TABLE `v1_system_carousel`  (
  `id` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `mobile_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `mobile_link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `client_type` int NOT NULL,
  `biz_type` int NOT NULL,
  `sort` int NOT NULL,
  `need_show` tinyint(1) NOT NULL DEFAULT 0,
  `title1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `title2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `region_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `region_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_system_carousel
-- ----------------------------

-- ----------------------------
-- Table structure for v1_system_category_type
-- ----------------------------
DROP TABLE IF EXISTS `v1_system_category_type`;
CREATE TABLE `v1_system_category_type`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `attr_count` int NOT NULL,
  `param_count` int NOT NULL,
  `sort` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_system_category_type
-- ----------------------------

-- ----------------------------
-- Table structure for v1_system_config
-- ----------------------------
DROP TABLE IF EXISTS `v1_system_config`;
CREATE TABLE `v1_system_config`  (
  `id` bigint NOT NULL,
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `org_id` bigint NOT NULL,
  `tab_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_encrypt` tinyint(1) NOT NULL DEFAULT 0,
  `content_type` int NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_system_config
-- ----------------------------

-- ----------------------------
-- Table structure for v1_system_config_template
-- ----------------------------
DROP TABLE IF EXISTS `v1_system_config_template`;
CREATE TABLE `v1_system_config_template`  (
  `id` bigint NOT NULL,
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tab_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `content_type` int NOT NULL,
  `is_encrypt` tinyint(1) NOT NULL DEFAULT 0,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_system_config_template
-- ----------------------------

-- ----------------------------
-- Table structure for v1_system_link
-- ----------------------------
DROP TABLE IF EXISTS `v1_system_link`;
CREATE TABLE `v1_system_link`  (
  `id` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` int NOT NULL,
  `status` int NOT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_system_link
-- ----------------------------

-- ----------------------------
-- Table structure for v1_system_param
-- ----------------------------
DROP TABLE IF EXISTS `v1_system_param`;
CREATE TABLE `v1_system_param`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sys_cate_type_id` bigint NOT NULL,
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_system_param
-- ----------------------------

-- ----------------------------
-- Table structure for v1_tab
-- ----------------------------
DROP TABLE IF EXISTS `v1_tab`;
CREATE TABLE `v1_tab`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `tab_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `head_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `bg_color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` int NOT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `is_default` tinyint(1) NOT NULL DEFAULT 0,
  `product_id_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_tab
-- ----------------------------

-- ----------------------------
-- Table structure for v1_task
-- ----------------------------
DROP TABLE IF EXISTS `v1_task`;
CREATE TABLE `v1_task`  (
  `id` bigint NOT NULL,
  `task_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `area_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_place_id` bigint NOT NULL,
  `shop_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_place_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_place_lat` double NOT NULL,
  `shop_place_lon` double NOT NULL,
  `storage_place_id` bigint NOT NULL,
  `storage_place_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `storage_place_lat` double NOT NULL,
  `storage_place_lon` double NOT NULL,
  `user_phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `order_money` bigint NOT NULL,
  `award` bigint NOT NULL,
  `status` int NOT NULL,
  `is_took` tinyint(1) NOT NULL DEFAULT 0,
  `sort` int NOT NULL,
  `driver_take_task_time` bigint NOT NULL,
  `driver_uid` bigint NOT NULL,
  `driver_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `driver_phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `driver_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `task_create_time` bigint NOT NULL,
  `driver_pickup_time` bigint NOT NULL,
  `driver_delivered_time` bigint NOT NULL,
  `cancel_task_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `floor` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `task_type` int NOT NULL,
  `contact_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deliver_fee` bigint NOT NULL,
  `org_deliver_fee` bigint NOT NULL,
  `pay_time` bigint NOT NULL,
  `order_time` bigint NOT NULL,
  `pickup_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_cross` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_task
-- ----------------------------

-- ----------------------------
-- Table structure for v1_task_award
-- ----------------------------
DROP TABLE IF EXISTS `v1_task_award`;
CREATE TABLE `v1_task_award`  (
  `id` bigint NOT NULL,
  `task_id` bigint NOT NULL,
  `task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uid` bigint NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `take_award` bigint NOT NULL,
  `sub_award` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_task_award
-- ----------------------------

-- ----------------------------
-- Table structure for v1_task_stat
-- ----------------------------
DROP TABLE IF EXISTS `v1_task_stat`;
CREATE TABLE `v1_task_stat`  (
  `id` bigint NOT NULL,
  `task_id` bigint NOT NULL,
  `task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `date` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `finish_amount` bigint NOT NULL,
  `failed_amount` bigint NOT NULL,
  `to_submit_amount` bigint NOT NULL,
  `submit_amount` bigint NOT NULL,
  `taken_amount` bigint NOT NULL,
  `lost_amount` bigint NOT NULL,
  `used_amount` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_task_stat
-- ----------------------------

-- ----------------------------
-- Table structure for v1_upgrade_config
-- ----------------------------
DROP TABLE IF EXISTS `v1_upgrade_config`;
CREATE TABLE `v1_upgrade_config`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `version_number` int NOT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `force_update_rule` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `normal_update_rule` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_type` int NOT NULL,
  `version_type` int NOT NULL,
  `platform` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_upgrade_config
-- ----------------------------

-- ----------------------------
-- Table structure for v1_upgrade_grey_user
-- ----------------------------
DROP TABLE IF EXISTS `v1_upgrade_grey_user`;
CREATE TABLE `v1_upgrade_grey_user`  (
  `id` bigint NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uid` bigint NOT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_upgrade_grey_user
-- ----------------------------

-- ----------------------------
-- Table structure for v1_user_dict
-- ----------------------------
DROP TABLE IF EXISTS `v1_user_dict`;
CREATE TABLE `v1_user_dict`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `count` bigint NOT NULL,
  `dict_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cate_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pinyin_abbr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_user_dict
-- ----------------------------

-- ----------------------------
-- Table structure for v1_voucher_category
-- ----------------------------
DROP TABLE IF EXISTS `v1_voucher_category`;
CREATE TABLE `v1_voucher_category`  (
  `id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `parent_id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `poster` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pinyin_abbr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `path_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_shown` int NOT NULL,
  `sort` int NOT NULL,
  `category_type` int NOT NULL,
  `sold_amount` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_voucher_category
-- ----------------------------

-- ----------------------------
-- Table structure for v1_voucher_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_voucher_log`;
CREATE TABLE `v1_voucher_log`  (
  `id` bigint NOT NULL,
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `category_id` bigint NOT NULL,
  `balance_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `voucher_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `digest` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `balance_direction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `amount` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `status` int NOT NULL,
  `shop_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `is_public` tinyint(1) NOT NULL DEFAULT 0,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `participants` bigint NOT NULL,
  `participants_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `filter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `balance_time` bigint NOT NULL,
  `date` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_voucher_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_withdraw_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_withdraw_log`;
CREATE TABLE `v1_withdraw_log`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `balance_log_id` bigint NOT NULL,
  `amount` bigint NOT NULL,
  `real_amount` bigint NOT NULL,
  `fee_amount` bigint NOT NULL,
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `bank_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `bank_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operator_id` bigint NOT NULL,
  `operator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `payment_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `payment_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `org_id` bigint NOT NULL,
  `audit_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `type` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of v1_withdraw_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
