-- Created by EntityToSQLGenerator
-- Table structure for tk_v1_kpi

-- !Ups

drop table if exists tk_v1_kpi;
create table tk_v1_kpi (
  id bigint not null auto_increment,
  start_time varchar(255),
  end_time varchar(255),
  user_id bigint,
  parent_id bigint,
  total_score double,
  constraint pk_tk_v1_kpi primary key (id)
);

-- !Downs

