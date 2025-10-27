-- Created by EntityToSQLGenerator
-- Table structure for tk_teacher_performance_assessment

-- !Ups

drop table if exists tk_teacher_performance_assessment;
create table tk_teacher_performance_assessment (
  id bigint not null auto_increment,
  teacher_id bigint,
  academic_year varchar(255),
  assessment_date varchar(255),
  total_score double,
  final_grade varchar(255),
  assessor varchar(255),
  assessment_status varchar(255),
  constraint pk_tk_teacher_performance_assessment primary key (id)
);

-- !Downs

