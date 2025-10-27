-- Created by EntityToSQLGenerator
-- Table structure for tk_v1_evaluation_indicator

-- !Ups

drop table if exists tk_v1_evaluation_indicator;
create table tk_v1_evaluation_indicator (
  id bigint not null auto_increment,
  name varchar(255),
  sub_name varchar(255),
  kpi_id bigint,
  constraint pk_tk_v1_evaluation_indicator primary key (id)
);

-- !Downs

