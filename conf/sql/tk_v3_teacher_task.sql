-- Created by EntityToSQLGenerator
-- Table structure for tk_v3_teacher_task

-- !Ups

drop table if exists tk_v3_teacher_task;
create table tk_v3_teacher_task (
  id bigint not null auto_increment,
  user_id bigint,
  parent_ids varchar(255),
  status varchar(255),
  tes_id bigint,
  constraint pk_tk_v3_teacher_task primary key (id)
);

-- !Downs

