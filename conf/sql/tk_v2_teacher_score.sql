-- Created by EntityToSQLGenerator
-- Table structure for tk_v2_teacher_score

-- !Ups

drop table if exists tk_v2_teacher_score;
create table tk_v2_teacher_score (
  id bigint not null auto_increment,
  user_id bigint,
  element_id bigint,
  score double,
  constraint pk_tk_v2_teacher_score primary key (id)
);

-- !Downs

