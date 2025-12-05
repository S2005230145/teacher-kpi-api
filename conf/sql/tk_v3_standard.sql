-- Created by EntityToSQLGenerator
-- Table structure for tk_v3_standard

-- !Ups

drop table if exists tk_v3_standard;
create table tk_v3_standard (
  id bigint not null auto_increment,
  name varchar(255),
  left_limit_score double,
  left_operator varchar(255),
  op varchar(255),
  right_limit_score double,
  right_operator varchar(255),
  constraint pk_tk_v3_standard primary key (id)
);

-- !Downs

