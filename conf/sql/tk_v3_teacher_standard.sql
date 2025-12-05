-- Created by EntityToSQLGenerator
-- Table structure for tk_v3_teacher_standard

-- !Ups

drop table if exists tk_v3_teacher_standard;
create table tk_v3_teacher_standard (
  id bigint not null auto_increment,
  user_id bigint,
  standard_id bigint,
  constraint pk_tk_v3_teacher_standard primary key (id)
);

-- !Downs

