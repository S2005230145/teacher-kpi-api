-- Created by EntityToSQLGenerator
-- Table structure for tk_v3_teacher_content_score

-- !Ups

drop table if exists tk_v3_teacher_content_score;
create table tk_v3_teacher_content_score (
  id bigint not null auto_increment,
  user_id bigint,
  content_id bigint,
  element_id bigint,
  score double,
  constraint pk_tk_v3_teacher_content_score primary key (id)
);

-- !Downs

