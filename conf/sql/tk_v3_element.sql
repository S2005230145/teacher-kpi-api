-- Created by EntityToSQLGenerator
-- Table structure for tk_v3_element

-- !Ups

drop table if exists tk_v3_element;
create table tk_v3_element (
  id bigint not null auto_increment,
  indicator_id bigint,
  element varchar(255),
  criteria varchar(255),
  is_auto tinyint(1) default 0,
  constraint pk_tk_v3_element primary key (id)
);

-- !Downs

