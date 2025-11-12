-- Created by EntityToSQLGenerator
-- Table structure for tk_v3_teacher_indicator_score

-- !Ups

drop table if exists tk_v3_teacher_indicator_score;
create table tk_v3_teacher_indicator_score (
  id bigint not null auto_increment,
  user_id bigint,
  indicator_id bigint,
  kpi_id bigint,
  score double,
  constraint pk_tk_v3_teacher_indicator_score primary key (id)
);

-- !Downs

