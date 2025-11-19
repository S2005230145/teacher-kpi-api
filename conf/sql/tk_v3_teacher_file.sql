-- Created by EntityToSQLGenerator
-- Table structure for tk_v3_teacher_file

-- !Ups

drop table if exists tk_v3_teacher_file;
create table tk_v3_teacher_file (
  id bigint not null auto_increment,
  description varchar(255),
  file_path varchar(255),
  constraint pk_tk_v3_teacher_file primary key (id)
);

-- !Downs

