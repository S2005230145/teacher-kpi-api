-- Created by EntityToSQLGenerator
-- Table structure for tk_assessment_detail

-- !Ups

drop table if exists tk_assessment_detail;
create table tk_assessment_detail (
  id bigint not null auto_increment,
  assessment_id bigint,
  category_id integer,
  evaluation_element varchar(255),
  evaluation_content varchar(255),
  evaluation_standard varchar(255),
  score double,
  max_score double,
  evidence_description varchar(255),
  evaluator varchar(255),
  evaluate_date datetime(6),
  constraint pk_tk_assessment_detail primary key (id)
);

-- !Downs

