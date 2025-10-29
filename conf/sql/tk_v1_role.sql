-- Created by EntityToSQLGenerator
-- Table structure for tk_v1_role

-- !Ups

drop table if exists tk_v1_role;
create table tk_v1_role (
  id bigint not null auto_increment,
  nick_name varchar(255),
  constraint pk_tk_v1_role primary key (id)
);

-- !Downs

