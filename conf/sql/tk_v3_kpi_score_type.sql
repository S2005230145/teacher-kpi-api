-- Created by EntityToSQLGenerator
-- Table structure for tk_v3_kpi_score_type

-- !Ups

drop table if exists tk_v3_kpi_score_type;
create table tk_v3_kpi_score_type (
  id bigint not null auto_increment,
  description varchar(255),
  json_param varchar(255),
  constraint pk_tk_v3_kpi_score_type primary key (id)
);

-- !Downs

