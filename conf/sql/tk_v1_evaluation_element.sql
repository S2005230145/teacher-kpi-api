-- Created by EntityToSQLGenerator
-- Table structure for tk_v1_evaluation_element

-- !Ups

drop table if exists tk_v1_evaluation_element;
create table tk_v1_evaluation_element (
  id bigint not null auto_increment,
  name varchar(255),
  constraint pk_tk_v1_evaluation_element primary key (id)
);

-- !Downs

