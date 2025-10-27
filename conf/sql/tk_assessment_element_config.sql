-- Created by EntityToSQLGenerator
-- Table structure for tk_assessment_element_config

-- !Ups

drop table if exists tk_assessment_element_config;
create table tk_assessment_element_config (
  id bigint not null auto_increment,
  category_id integer,
  element_name varchar(255),
  element_code varchar(255),
  content_description varchar(255),
  evaluation_criteria varchar(255),
  max_score double,
  weight double,
  calculation_method varchar(255),
  is_active tinyint(1) default 0,
  display_order integer,
  constraint pk_tk_assessment_element_config primary key (id)
);

-- !Downs

