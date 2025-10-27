-- Created by EntityToSQLGenerator
-- Table structure for tk_v1_teacher_content

-- !Ups

drop table if exists tk_v1_teacher_content;
create table tk_v1_teacher_content (
  id bigint not null auto_increment,
  evaluation_content varchar(255),
  score double,
  constraint pk_tk_v1_teacher_content primary key (id)
);

-- !Downs

