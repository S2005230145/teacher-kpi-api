-- Created by EntityToSQLGenerator
-- Table structure for tk_v1_teacher_performance_assessment

-- !Ups

drop table if exists tk_v1_teacher_performance_assessment;
create table tk_v1_teacher_performance_assessment (
  id bigint not null auto_increment,
  element_ids varchar(255),
  evaluation_id bigint,
  content_ids varchar(255),
  evaluation_standard varchar(255),
  score double,
  constraint pk_tk_v1_teacher_performance_assessment primary key (id)
);

-- !Downs

