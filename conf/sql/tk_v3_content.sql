-- Created by EntityToSQLGenerator
-- Table structure for tk_v3_content

-- !Ups

drop table if exists tk_v3_content;
create table tk_v3_content (
  id bigint not null auto_increment,
  element_id bigint,
  content varchar(255),
  constraint pk_tk_v3_content primary key (id)
);

-- !Downs

