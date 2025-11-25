-- Created by EntityToSQLGenerator
-- Table structure for tk_v3_kpi

-- !Ups

drop table if exists tk_v3_kpi;
create table tk_v3_kpi (
  id bigint not null auto_increment,
  title varchar(255),
  create_time datetime(6),
  end_time datetime(6),
  constraint pk_tk_v3_kpi primary key (id)
);

-- !Downs

