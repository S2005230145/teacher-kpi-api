-- Created by EntityToSQLGenerator
-- Table structure for tk_v1_user

-- !Ups

drop table if exists tk_v1_user;
create table tk_v1_user (
  id bigint not null auto_increment,
  user_name varchar(255),
  password varchar(255),
  type_name varchar(255),
  status integer,
  role_id bigint,
  constraint pk_tk_v1_user primary key (id)
);

-- !Downs

