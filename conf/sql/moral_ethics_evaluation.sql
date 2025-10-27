-- Created by EntityToSQLGenerator
-- Table structure for moral_ethics_evaluation

-- !Ups

drop table if exists moral_ethics_evaluation;
create table moral_ethics_evaluation (
  id bigint not null auto_increment,
  teacher_id bigint,
  assessment_id bigint,
  behavior_standard_compliance tinyint(1) default 0,
  has_violation_behavior tinyint(1) default 0,
  has_negative_list_behavior tinyint(1) default 0,
  evaluation_level_id integer,
  evaluation_basis varchar(255),
  evaluator varchar(255),
  evaluate_date datetime(6),
  constraint pk_moral_ethics_evaluation primary key (id)
);

-- !Downs

