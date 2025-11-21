-- Created by EntityToSQLGenerator
-- Table structure for tk_v3_department

-- !Ups

drop table if exists tk_v3_department;
create table tk_v3_department (
  id bigint not null auto_increment,
  department_name varchar(255),
  department_code varchar(255),
  campus varchar(255),
  description varchar(255),
  create_time datetime(6),
  update_time datetime(6),
  constraint pk_tk_v3_department primary key (id)
);

-- !Downs

