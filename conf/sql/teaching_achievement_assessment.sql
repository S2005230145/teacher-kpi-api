-- Created by EntityToSQLGenerator
-- Table structure for teaching_achievement_assessment

-- !Ups

drop table if exists teaching_achievement_assessment;
create table teaching_achievement_assessment (
  id bigint not null auto_increment,
  teacher_id bigint,
  assessment_id bigint,
  academic_year varchar(255),
  student_academic_development varchar(255),
  exam_types varchar(255),
  average_score double,
  pass_rate double,
  excellence_rate double,
  score_improvement double,
  city_ranking integer,
  school_ranking integer,
  academic_score double,
  mentorship_agreement_fulfillment tinyint(1) default 0,
  guidance_effectiveness varchar(255),
  research_group_work varchar(255),
  master_studio_participation tinyint(1) default 0,
  training_instructor_role tinyint(1) default 0,
  rural_teaching_activities tinyint(1) default 0,
  demonstration_score double,
  class_meeting_awards varchar(255),
  team_activity_awards varchar(255),
  single_competition_awards varchar(255),
  comprehensive_honors varchar(255),
  award_levels varchar(255),
  class_awards_score double,
  subject_competition_guidance varchar(255),
  innovation_competition_guidance varchar(255),
  sports_league_guidance varchar(255),
  art_performance_guidance varchar(255),
  other_competition_guidance varchar(255),
  student_awards_levels varchar(255),
  student_guidance_score double,
  school_based_curriculum_development tinyint(1) default 0,
  research_learning_guidance tinyint(1) default 0,
  comprehensive_practice_organization tinyint(1) default 0,
  social_practice_organization tinyint(1) default 0,
  club_activities_guidance tinyint(1) default 0,
  extracurricular_tutor_role tinyint(1) default 0,
  school_activities_score double,
  teaching_research_participation tinyint(1) default 0,
  collective_preparation_participation tinyint(1) default 0,
  resource_sharing tinyint(1) default 0,
  post_training_participation tinyint(1) default 0,
  training_learning_participation tinyint(1) default 0,
  class_observation_completion tinyint(1) default 0,
  team_cooperation_score double,
  student_satisfaction_rate double,
  parent_satisfaction_rate double,
  evaluation_participant_count integer,
  satisfaction_score double,
  total_teaching_achievement_score double,
  constraint pk_teaching_achievement_assessment primary key (id)
);

-- !Downs

