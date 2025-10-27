-- Created by EntityToSQLGenerator
-- Table structure for personal_development_assessment

-- !Ups

drop table if exists personal_development_assessment;
create table personal_development_assessment (
  id bigint not null auto_increment,
  teacher_id bigint,
  assessment_id bigint,
  academic_year varchar(255),
  demonstration_classes_count integer,
  observation_classes_count integer,
  special_lectures_count integer,
  class_observation_activities integer,
  regional_exchange_activities integer,
  activity_levels varchar(255),
  public_teaching_score double,
  education_papers_count integer,
  paper_titles varchar(255),
  paper_types varchar(255),
  award_status varchar(255),
  compilation_level varchar(255),
  publish_date datetime(6),
  papers_score double,
  research_projects varchar(255),
  research_levels varchar(255),
  research_roles varchar(255),
  subject_proposition_work tinyint(1) default 0,
  continuing_education_completion tinyint(1) default 0,
  continuing_education_hours integer,
  research_participation_score double,
  teaching_special_awards varchar(255),
  comprehensive_honors varchar(255),
  teaching_competition_awards varchar(255),
  excellent_teacher_certification varchar(255),
  award_dates varchar(255),
  personal_awards_score double,
  total_personal_development_score double,
  constraint pk_personal_development_assessment primary key (id)
);

-- !Downs

