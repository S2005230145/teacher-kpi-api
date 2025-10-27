-- Created by EntityToSQLGenerator
-- Table structure for critical_work_assessment

-- !Ups

drop table if exists critical_work_assessment;
create table critical_work_assessment (
  id bigint not null auto_increment,
  teacher_id bigint,
  assessment_id bigint,
  academic_year varchar(255),
  administrative_positions varchar(255),
  work_categories varchar(255),
  specific_duties varchar(255),
  work_performance varchar(255),
  work_difficulty_level varchar(255),
  work_importance_level varchar(255),
  work_completion_evaluation varchar(255),
  administrative_work_score double,
  temporary_task_types varchar(255),
  task_descriptions varchar(255),
  task_start_date varchar(255),
  task_end_date varchar(255),
  task_urgency_level varchar(255),
  task_completion_status varchar(255),
  task_achievements varchar(255),
  temporary_tasks_score double,
  total_critical_work_score double,
  constraint pk_critical_work_assessment primary key (id)
);

-- !Downs

