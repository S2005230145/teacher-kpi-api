-- Created by EntityToSQLGenerator
-- Table structure for tk_v3_campus

-- !Ups

drop table if exists tk_v3_campus;
create table tk_v3_campus (
  id bigint not null auto_increment,
  campus_name varchar(255),
  address varchar(255),
  phone varchar(255),
  principal varchar(255),
  capacity integer,
  establish_date datetime(6),
  status integer,
  create_time datetime(6),
  update_time datetime(6),
  constraint pk_tk_v3_campus primary key (id)
);

-- !Downs

