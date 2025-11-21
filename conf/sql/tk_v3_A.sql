-- Created by EntityToSQLGenerator
-- Table structure for tk_v3_A

-- !Ups

drop table if exists tk_v3_A;
create table tk_v3_A (
  id bigint not null auto_increment,
  description varchar(255),
  date datetime(6),
  constraint pk_tk_v3_A primary key (id)
);

-- !Downs

