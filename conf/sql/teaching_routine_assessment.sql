-- Created by EntityToSQLGenerator
-- Table structure for teaching_routine_assessment

-- !Ups

drop table if exists teaching_routine_assessment;
create table teaching_routine_assessment (
  id bigint not null auto_increment,
  teacher_id bigint,
  assessment_id bigint,
  academic_year varchar(255),
  sick_leave_days integer,
  personal_leave_days integer,
  late_times integer,
  absence_times integer,
  political_study_attendance tinyint(1) default 0,
  teaching_research_attendance tinyint(1) default 0,
  school_meeting_attendance tinyint(1) default 0,
  other_collective_activities_attendance tinyint(1) default 0,
  attendance_score double,
  standard_class_hours integer,
  actual_class_hours integer,
  substitution_hours integer,
  special_class_hours integer,
  workload_completion_rate double,
  workload_score double,
  class_teacher_role tinyint(1) default 0,
  grade_leader_role tinyint(1) default 0,
  teaching_research_leader_role tinyint(1) default 0,
  young_pioneer_leader_role tinyint(1) default 0,
  youth_league_secretary_role tinyint(1) default 0,
  middle_manager_role tinyint(1) default 0,
  management_position_description varchar(255),
  management_work_evaluation varchar(255),
  management_score double,
  teaching_plan_quality varchar(255),
  teaching_organization varchar(255),
  classroom_management varchar(255),
  teaching_concept varchar(255),
  classroom_effectiveness varchar(255),
  moral_education_integration varchar(255),
  modern_education_tech_integration varchar(255),
  classroom_teaching_score double,
  homework_correction_quality varchar(255),
  personalized_guidance varchar(255),
  psychological_guidance varchar(255),
  student_comprehensive_assessment varchar(255),
  growth_records_maintenance varchar(255),
  learning_difficulty_support varchar(255),
  career_planning_guidance varchar(255),
  five_education_guidance varchar(255),
  learning_behavior_habits_cultivation varchar(255),
  student_development_score double,
  parent_meeting_participation varchar(255),
  home_visit_records varchar(255),
  parenting_guidance varchar(255),
  parent_school_training varchar(255),
  parent_contact_score double,
  total_teaching_routine_score double,
  constraint pk_teaching_routine_assessment primary key (id)
);

-- !Downs

