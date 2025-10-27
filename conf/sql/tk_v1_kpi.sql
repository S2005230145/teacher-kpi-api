-- Created by EntityToSQLGenerator
-- Table structure for tk_v1_kpi

-- !Ups

drop table if exists tk_v1_kpi;
create table tk_v1_kpi (
  id bigint not null auto_increment,
  start_time datetime(6),
  end_time datetime(6),
  user_id bigint,
  parent_id bigint,
  constraint pk_tk_v1_kpi primary key (id)
);

-- !Downs

