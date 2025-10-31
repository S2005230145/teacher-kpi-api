-- Created by EntityToSQLGenerator
-- Table structure for tk_v3_indicator

-- !Ups

drop table if exists tk_v3_indicator;
create table tk_v3_indicator (
  id bigint not null auto_increment,
  indicator_name varchar(255),
  sub_name varchar(255),
  constraint pk_tk_v3_indicator primary key (id)
);

-- !Downs

