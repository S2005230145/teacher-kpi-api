-- Created by Ebean DDL
-- To stop Ebean DDL generation, remove this comment (both lines) and start using Evolutions

-- !Ups

-- init script create procs
-- Inital script to create stored procedures etc for mysql platform
DROP PROCEDURE IF EXISTS usp_ebean_drop_foreign_keys;

delimiter $$
--
-- PROCEDURE: usp_ebean_drop_foreign_keys TABLE, COLUMN
-- deletes all constraints and foreign keys referring to TABLE.COLUMN
--
CREATE PROCEDURE usp_ebean_drop_foreign_keys(IN p_table_name VARCHAR(255), IN p_column_name VARCHAR(255))
BEGIN
DECLARE done INT DEFAULT FALSE;
DECLARE c_fk_name CHAR(255);
DECLARE curs CURSOR FOR SELECT CONSTRAINT_NAME from information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = DATABASE() and TABLE_NAME = p_table_name and COLUMN_NAME = p_column_name
AND REFERENCED_TABLE_NAME IS NOT NULL;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

OPEN curs;

read_loop: LOOP
FETCH curs INTO c_fk_name;
IF done THEN
LEAVE read_loop;
END IF;
SET @sql = CONCAT('ALTER TABLE `', p_table_name, '` DROP FOREIGN KEY ', c_fk_name);
PREPARE stmt FROM @sql;
EXECUTE stmt;
END LOOP;

CLOSE curs;
END
$$

DROP PROCEDURE IF EXISTS usp_ebean_drop_column;

delimiter $$
--
-- PROCEDURE: usp_ebean_drop_column TABLE, COLUMN
-- deletes the column and ensures that all indices and constraints are dropped first
--
CREATE PROCEDURE usp_ebean_drop_column(IN p_table_name VARCHAR(255), IN p_column_name VARCHAR(255))
BEGIN
CALL usp_ebean_drop_foreign_keys(p_table_name, p_column_name);
SET @sql = CONCAT('ALTER TABLE `', p_table_name, '` DROP COLUMN `', p_column_name, '`');
PREPARE stmt FROM @sql;
EXECUTE stmt;
END
$$
-- apply changes
create table cp_system_action (
  id                            varchar(255) auto_increment not null,
  action_name                   varchar(255),
  action_desc                   varchar(255),
  module_name                   varchar(255),
  module_desc                   varchar(255),
  need_show                     tinyint(1) default 0 not null,
  display_order                 integer not null,
  create_time                   bigint not null,
  constraint pk_cp_system_action primary key (id)
);

create table v1_activity (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  org_name                      varchar(255),
  shop_id                       bigint not null,
  shop_name                     varchar(255),
  begin_time                    bigint not null,
  end_time                      bigint not null,
  begin_hour                    integer not null,
  end_hour                      integer not null,
  title                         varchar(255),
  sub_title                     varchar(255),
  head_pic                      varchar(255),
  activity_type                 integer not null,
  page_url                      varchar(255),
  rules_url                     varchar(255),
  bg_color                      varchar(255),
  status                        integer not null,
  create_time                   bigint not null,
  update_time                   bigint not null,
  constraint pk_v1_activity primary key (id)
);

create table v1_activity_category (
  id                            bigint auto_increment not null,
  activity_id                   bigint not null,
  org_id                        bigint not null,
  parent_id                     bigint not null,
  name                          varchar(255),
  img_url                       varchar(255),
  poster                        varchar(255),
  path                          varchar(255),
  pinyin_abbr                   varchar(255),
  path_name                     varchar(255),
  is_shown                      integer not null,
  sort                          integer not null,
  sold_amount                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_activity_category primary key (id)
);

create table v1_admin_config (
  id                            integer auto_increment not null,
  config_key                    varchar(255),
  config_value                  varchar(255),
  note                          varchar(255),
  enable                        tinyint(1) default 0 not null,
  is_encrypt                    tinyint(1) default 0 not null,
  update_time                   bigint not null,
  constraint pk_v1_admin_config primary key (id)
);

create table cp_member (
  id                            bigint auto_increment not null,
  username                      varchar(255),
  realname                      varchar(255),
  avatar                        varchar(255),
  password                      varchar(255),
  create_time                   bigint not null,
  last_time                     bigint not null,
  last_ip                       varchar(255),
  phone_number                  varchar(255),
  org_id                        bigint not null,
  org_name                      varchar(255),
  pinyin_abbr                   varchar(255),
  status                        integer not null,
  bg_img_url                    varchar(255),
  area_id                       bigint not null,
  constraint pk_cp_member primary key (id)
);

create table v1_agreement (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  agreement                     varchar(255),
  sign_img_url                  varchar(255),
  membership_id                 bigint not null,
  membership_title              varchar(255),
  membership_level              integer not null,
  create_time                   bigint not null,
  constraint pk_v1_agreement primary key (id)
);

create table v1_announcement (
  id                            bigint auto_increment not null,
  title                         varchar(255),
  content                       varchar(255),
  type                          varchar(255),
  area_id                       bigint not null,
  status                        varchar(255),
  constraint pk_v1_announcement primary key (id)
);

create table v1_area (
  id                            bigint auto_increment not null,
  status                        integer not null,
  name                          varchar(255),
  digest                        varchar(255),
  contact_number                varchar(255),
  contact_name                  varchar(255),
  address                       varchar(255),
  description                   varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  lat                           double not null,
  lon                           double not null,
  avatar                        varchar(255),
  rect_logo                     varchar(255),
  constraint pk_v1_area primary key (id)
);

create table v1_article (
  id                            bigint auto_increment not null,
  title                         varchar(255),
  author                        varchar(255),
  source                        varchar(255),
  link_url                      varchar(255),
  cate_id                       bigint not null,
  category_name                 varchar(255),
  publish_time                  bigint not null,
  status                        integer not null,
  is_top                        tinyint(1) default 0 not null,
  is_recommend                  tinyint(1) default 0 not null,
  sort                          integer not null,
  content                       varchar(255),
  digest                        varchar(255),
  head_pic                      varchar(255),
  tags                          varchar(255),
  product_id_list               varchar(255),
  views                         bigint not null,
  favs                          bigint not null,
  comments                      bigint not null,
  shares                        bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  org_id                        bigint not null,
  constraint pk_v1_article primary key (id)
);

create table v1_article_category (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  sort                          integer not null,
  status                        integer not null,
  cate_type                     integer not null,
  org_id                        bigint not null,
  note                          varchar(255),
  head_pic                      varchar(255),
  icon                          varchar(255),
  display_mode                  varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_article_category primary key (id)
);

create table v1_article_comment (
  id                            bigint auto_increment not null,
  content                       varchar(255),
  article_id                    bigint not null,
  uid                           bigint not null,
  likes                         bigint not null,
  create_time                   bigint not null,
  org_id                        bigint not null,
  constraint pk_v1_article_comment primary key (id)
);

create table v1_article_comment_like (
  id                            bigint auto_increment not null,
  comment_id                    bigint not null,
  has_like                      tinyint(1) default 0 not null,
  uid                           bigint not null,
  org_id                        bigint not null,
  author_uid                    bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_article_comment_like primary key (id)
);

create table v1_article_comment_reply (
  id                            bigint auto_increment not null,
  content                       varchar(255),
  article_comment_id            bigint not null,
  at_uid                        bigint not null,
  uid                           bigint not null,
  org_id                        bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_article_comment_reply primary key (id)
);

create table v1_article_fav (
  id                            bigint auto_increment not null,
  article_id                    bigint not null,
  uid                           bigint not null,
  org_id                        bigint not null,
  head_pic                      varchar(255),
  author                        varchar(255),
  title                         varchar(255),
  enable                        tinyint(1) default 0 not null,
  article_time                  bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_article_fav primary key (id)
);

create table v1_article_read_log (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  article_id                    bigint not null,
  enable                        tinyint(1) default 0 not null,
  count                         bigint not null,
  org_id                        bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_article_read_log primary key (id)
);

create table assessment_plan (
  id                            bigint auto_increment not null,
  plan_name                     varchar(255),
  academic_year                 varchar(255),
  start_date                    date,
  end_date                      date,
  status                        varchar(9),
  total_score_weight            integer,
  create_time                   datetime(6),
  update_time                   datetime(6),
  constraint pk_assessment_plan primary key (id)
);

create table attendance_record (
  id                            bigint auto_increment not null,
  teacher_id                    bigint,
  record_date                   date,
  sick_leave_days               integer,
  personal_leave_days           integer,
  late_times                    integer,
  absence_times                 integer,
  political_study_attendance    tinyint(1),
  teaching_research_attendance  tinyint(1),
  school_meeting_attendance     tinyint(1),
  other_activity_attendance     tinyint(1),
  attendance_score              double,
  constraint pk_attendance_record primary key (id)
);

create table v1_balance_log (
  id                            bigint auto_increment not null,
  order_no                      varchar(255),
  uid                           bigint not null,
  member_no                     varchar(255),
  member_name                   varchar(255),
  item_id                       integer not null,
  balance_type                  integer not null,
  left_balance                  bigint not null,
  freeze_balance                bigint not null,
  total_balance                 bigint not null,
  change_amount                 bigint not null,
  biz_type                      integer not null,
  note                          varchar(255),
  freeze_status                 integer not null,
  org_id                        bigint not null,
  shop_id                       bigint not null,
  shop_name                     varchar(255),
  operator_id                   bigint not null,
  operator_name                 varchar(255),
  staff_id                      bigint not null,
  staff_name                    varchar(255),
  create_time                   bigint not null,
  constraint pk_v1_balance_log primary key (id)
);

create table v1_birthday_log (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  birthday                      bigint not null,
  org_id                        bigint not null,
  year                          integer not null,
  update_time                   bigint not null,
  constraint pk_v1_birthday_log primary key (id)
);

create table v1_brand (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  url                           varchar(255),
  logo                          varchar(255),
  poster                        varchar(255),
  content                       varchar(255),
  sort                          integer not null,
  status                        integer not null,
  seo_title                     varchar(255),
  seo_keywords                  varchar(255),
  seo_description               varchar(255),
  show_at_home                  tinyint(1) default 0 not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_brand primary key (id)
);

create table v1_browse_log (
  id                            bigint auto_increment not null,
  product_id                    bigint not null,
  uid                           bigint not null,
  user_name                     varchar(255),
  avatar                        varchar(255),
  phone_number                  varchar(255),
  uuid                          varchar(255),
  shop_id                       bigint not null,
  create_time                   bigint not null,
  update_time                   bigint not null,
  constraint pk_v1_browse_log primary key (id)
);

create table v1_cabinet (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  area_id                       bigint not null,
  shop_name                     varchar(255),
  name                          varchar(255),
  cabinet_sn                    varchar(255),
  cabinet_app_key               varchar(255),
  cabinet_secret_key            varchar(255),
  box_count                     integer not null,
  note                          varchar(255),
  filter                        varchar(255),
  enable                        tinyint(1) default 0 not null,
  update_time                   bigint not null,
  sort                          integer not null,
  constraint pk_v1_cabinet primary key (id)
);

create table v1_cabinet_box (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  shop_id                       bigint not null,
  shop_name                     varchar(255),
  cabinet_id                    bigint not null,
  cabinet_name                  varchar(255),
  box_no                        integer not null,
  enable                        tinyint(1) default 0 not null,
  status                        varchar(255),
  constraint pk_v1_cabinet_box primary key (id)
);

create table v1_cabinet_callback (
  id                            bigint auto_increment not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  content                       varchar(255),
  constraint pk_v1_cabinet_callback primary key (id)
);

create table v1_cabinet_log (
  id                            bigint auto_increment not null,
  area_id                       bigint not null,
  order_id                      bigint not null,
  order_no                      varchar(255),
  uid                           bigint not null,
  driver_id                     bigint not null,
  source                        integer not null,
  cabinet_status                integer not null,
  cabinet_id                    bigint not null,
  box_no                        integer not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_cabinet_log primary key (id)
);

create table v1_card_coupon_config (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  shop_id                       bigint not null,
  title                         varchar(255),
  content                       varchar(255),
  digest                        varchar(255),
  img_url                       varchar(255),
  filter                        varchar(255),
  contact_phone_number          varchar(255),
  contact_address               varchar(255),
  update_time                   bigint not null,
  days                          bigint not null,
  expire_time                   bigint not null,
  no_limit_count                tinyint(1) default 0 not null,
  create_time                   bigint not null,
  product_id                    bigint not null,
  sku_id                        bigint not null,
  lat                           double not null,
  lng                           double not null,
  give_count                    integer not null,
  status                        integer not null,
  card_type                     integer not null,
  discount                      bigint not null,
  price                         bigint not null,
  begin_use_time                bigint not null,
  only_pick_up                  tinyint(1) default 0 not null,
  constraint pk_v1_card_coupon_config primary key (id)
);

create table v1_category (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  parent_id                     bigint not null,
  name                          varchar(255),
  img_url                       varchar(255),
  poster                        varchar(255),
  path                          varchar(255),
  pinyin_abbr                   varchar(255),
  path_name                     varchar(255),
  is_shown                      integer not null,
  exclude                       tinyint(1) default 0 not null,
  need_time_to_make             tinyint(1) default 0 not null,
  discount_json                 varchar(255),
  staff_award_ratio             double not null,
  sort                          integer not null,
  category_type                 integer not null,
  sold_amount                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_category primary key (id)
);

create table v1_category_attr (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  sort                          integer not null,
  constraint pk_v1_category_attr primary key (id)
);

create table v1_category_classify (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  category_id                   bigint not null,
  category_name                 varchar(255),
  classify_id                   bigint not null,
  classify_name                 varchar(255),
  cover_img                     varchar(255),
  sort                          bigint not null,
  constraint pk_v1_category_classify primary key (id)
);

create table v1_product_classify (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  classify_code                 varchar(255),
  sub_title                     varchar(255),
  head_pic                      varchar(255),
  icon                          varchar(255),
  bg_color                      varchar(255),
  font_color                    varchar(255),
  link_url                      varchar(255),
  status                        integer not null,
  classify_type                 integer not null,
  height                        integer not null,
  item_size                     integer not null,
  sort                          bigint not null,
  product_count                 integer not null,
  parent_id                     bigint not null,
  is_parent                     tinyint(1) default 0 not null,
  activity_type                 integer not null,
  product_id_list               varchar(255),
  constraint pk_v1_product_classify primary key (id)
);

create table classroom_teaching_evaluation (
  id                            bigint auto_increment not null,
  teacher_id                    bigint,
  evaluator_id                  bigint,
  evaluation_date               date,
  teaching_plan_score           integer,
  group_management_score        integer,
  classroom_management_score    integer,
  teaching_effectiveness_score  integer,
  moral_education_score         integer,
  tech_integration_score        integer,
  overall_score                 double,
  constraint pk_classroom_teaching_evaluation primary key (id)
);

create table v1_client_log (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  device_id                     varchar(255),
  system_no                     varchar(255),
  platform                      varchar(255),
  brand                         varchar(255),
  model                         varchar(255),
  version_number                bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_client_log primary key (id)
);

create table v1_contact_detail (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  name                          varchar(255),
  storage_place_id              bigint not null,
  address                       varchar(255),
  telephone                     varchar(255),
  note                          varchar(255),
  is_default                    integer not null,
  update_time                   bigint not null,
  lat                           double not null,
  lng                           double not null,
  create_time                   bigint not null,
  constraint pk_v1_contact_detail primary key (id)
);

create table v1_coupon_config (
  id                            bigint auto_increment not null,
  coupon_title                  varchar(255),
  coupon_content                varchar(255),
  org_id                        bigint not null,
  area_id                       bigint not null,
  amount                        integer not null,
  type                          integer not null,
  status                        integer not null,
  need_show                     tinyint(1) default 0 not null,
  claim_per_member              integer not null,
  total_amount                  integer not null,
  claim_amount                  integer not null,
  id_type                       integer not null,
  rule_content                  varchar(255),
  type_rules                    varchar(255),
  use_rules                     varchar(255),
  upto                          bigint not null,
  free                          bigint not null,
  merchant_ids                  varchar(255),
  shop_category_ids             varchar(255),
  img_url                       varchar(255),
  begin_use_time                bigint not null,
  begin_time                    bigint not null,
  end_time                      bigint not null,
  expire_days                   bigint not null,
  expire_time                   bigint not null,
  old_price                     integer not null,
  current_price                 integer not null,
  update_time                   bigint not null,
  source                        integer not null,
  combo_coupon_id_list          varchar(255),
  combo_coupon_title_list       varchar(255),
  constraint pk_v1_coupon_config primary key (id)
);

create table v1_default_product_param (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  sys_cate_type_id              bigint not null,
  method                        varchar(255),
  value                         varchar(255),
  sort                          integer not null,
  constraint pk_v1_default_product_param primary key (id)
);

create table v1_deliver_fee_change_apply (
  id                            bigint auto_increment not null,
  reason                        varchar(255),
  change_amount                 bigint not null,
  area_id                       bigint not null,
  begin_time                    bigint not null,
  end_time                      bigint not null,
  status                        integer not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_deliver_fee_change_apply primary key (id)
);

create table v1_deliver_fee_change_confirm_log (
  id                            bigint auto_increment not null,
  apply_id                      bigint not null,
  reason                        varchar(255),
  change_amount                 bigint not null,
  area_id                       bigint not null,
  begin_time                    bigint not null,
  end_time                      bigint not null,
  status                        integer not null,
  org_id                        bigint not null,
  org_name                      varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_deliver_fee_change_confirm_log primary key (id)
);

create table v1_delivery_fee (
  id                            bigint auto_increment not null,
  area_id                       bigint not null,
  shop_place_id                 bigint not null,
  shop_place_name               varchar(255),
  storage_place_id              bigint not null,
  storage_place_name            varchar(255),
  default_fee                   bigint not null,
  driver_fee_json               varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_delivery_fee primary key (id)
);

create table v1_deposit (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  trade_no                      varchar(255),
  order_no                      varchar(255),
  uid                           bigint not null,
  config_id                     bigint not null,
  user_name                     varchar(255),
  member_no                     varchar(255),
  amount                        bigint not null,
  give                          bigint not null,
  real_pay                      bigint not null,
  status                        integer not null,
  pay_tx                        varchar(255),
  account_settle                integer not null,
  balance_time                  bigint not null,
  pay_method                    integer not null,
  pay_method_name               varchar(255),
  friend_id                     bigint not null,
  operator_id                   bigint not null,
  operator_name                 varchar(255),
  shop_id                       bigint not null,
  shop_name                     varchar(255),
  update_time                   bigint not null,
  source                        integer not null,
  mp_status                     integer not null,
  staff_id                      bigint not null,
  member_level                  integer not null,
  staff_name                    varchar(255),
  staff_award                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_deposit primary key (id)
);

create table v1_charge_config (
  id                            bigint auto_increment not null,
  upto                          bigint not null,
  give                          bigint not null,
  org_id                        bigint not null,
  only_pos                      tinyint(1) default 0 not null,
  only_give                     tinyint(1) default 0 not null,
  allow_free_order              tinyint(1) default 0 not null,
  sort                          bigint not null,
  coupon                        varchar(255),
  card_wallet                   varchar(255),
  begin_time                    bigint not null,
  end_time                      bigint not null,
  level_id                      bigint not null,
  constraint pk_v1_charge_config primary key (id)
);

create table v1_dict (
  id                            bigint auto_increment not null,
  parent_id                     bigint not null,
  sort                          integer not null,
  path                          varchar(255),
  name                          varchar(255),
  attr                          varchar(255),
  attr_value                    varchar(255),
  pinyin_abbr                   varchar(255),
  lat                           double not null,
  lng                           double not null,
  need_show                     tinyint(1) default 0 not null,
  create_time                   bigint not null,
  constraint pk_v1_dict primary key (id)
);

create table v1_district (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  deliver_fee                   bigint not null,
  area_id                       bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_district primary key (id)
);

create table v1_driver (
  id                            bigint auto_increment not null,
  username                      varchar(255),
  realname                      varchar(255),
  avatar                        varchar(255),
  password                      varchar(255),
  create_time                   bigint not null,
  last_time                     bigint not null,
  last_ip                       varchar(255),
  phone_number                  varchar(255),
  status                        integer not null,
  id_card_number                varchar(255),
  id_card_pic_front             varchar(255),
  id_card_pic_back              varchar(255),
  is_admin                      tinyint(1) default 0 not null,
  enable                        integer not null,
  type                          integer not null,
  area_id                       bigint not null,
  constraint pk_v1_driver primary key (id)
);

create table v1_driver_audit (
  id                            bigint auto_increment not null,
  audit_status                  integer not null,
  audit_time                    bigint not null,
  audit_remark                  varchar(255),
  driver_id                     bigint not null,
  driver_name                   varchar(255),
  operator_id                   bigint not null,
  operator_name                 varchar(255),
  constraint pk_v1_driver_audit primary key (id)
);

create table v1_driver_balance (
  id                            bigint auto_increment not null,
  item_id                       integer not null,
  left_balance                  bigint not null,
  freeze_balance                bigint not null,
  total_balance                 bigint not null,
  real_pay                      bigint not null,
  org_id                        bigint not null,
  give                          bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_driver_balance primary key (id)
);

create table v1_driver_feedback_log (
  id                            bigint auto_increment not null,
  area_id                       bigint not null,
  create_time                   bigint not null,
  update_time                   bigint not null,
  orderId                       bigint not null,
  driverUid                     bigint not null,
  content                       varchar(255),
  reason                        varchar(255),
  attach                        varchar(255),
  constraint pk_v1_driver_feedback_log primary key (id)
);

create table v1_everyday_sign_in (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_everyday_sign_in primary key (id)
);

create table v1_upgrade_grey_user (
  id                            bigint auto_increment not null,
  user_name                     varchar(255),
  uid                           bigint not null,
  note                          varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_upgrade_grey_user primary key (id)
);

create table cp_group (
  id                            integer auto_increment not null,
  name                          varchar(255),
  is_admin                      tinyint(1) default 0 not null,
  description                   varchar(255),
  create_time                   bigint not null,
  constraint pk_cp_group primary key (id)
);

create table cp_group_action (
  id                            integer auto_increment not null,
  group_id                      integer not null,
  system_action_id              varchar(255),
  constraint pk_cp_group_action primary key (id)
);

create table cp_group_menu (
  id                            integer auto_increment not null,
  menu_id                       integer not null,
  group_id                      integer not null,
  constraint pk_cp_group_menu primary key (id)
);

create table cp_group_user (
  id                            bigint auto_increment not null,
  group_id                      integer not null,
  group_name                    varchar(255),
  member_id                     bigint not null,
  realname                      varchar(255),
  create_time                   bigint not null,
  constraint pk_cp_group_user primary key (id)
);

create table v1_item (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  img_url                       varchar(255),
  is_shown                      integer not null,
  create_time                   bigint not null,
  update_time                   bigint not null,
  constraint pk_v1_item primary key (id)
);

create table cp_log (
  log_id                        bigint auto_increment not null,
  log_unique                    varchar(255),
  log_sym_id                    varchar(255),
  log_mer_id                    integer not null,
  log_param                     varchar(255),
  log_created                   bigint not null,
  constraint pk_cp_log primary key (log_id)
);

create table v1_login_log (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  org_id                        bigint not null,
  ip                            varchar(255),
  place                         varchar(255),
  create_time                   bigint not null,
  constraint pk_v1_login_log primary key (id)
);

create table v1_logistics (
  id                            bigint auto_increment not null,
  order_id                      bigint not null,
  express_no                    varchar(255),
  consignee_realname            varchar(255),
  consignee_phone_number        varchar(255),
  consignee_phone_number2       varchar(255),
  consignee_address             varchar(255),
  consignee_postcode            varchar(255),
  logistics_type                integer not null,
  logistics_fee                 bigint not null,
  logistics_status              integer not null,
  logistics_settlement_status   integer not null,
  logistics_last_desc           varchar(255),
  logistics_desc                varchar(255),
  settlement_time               bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_logistics primary key (id)
);

create table v1_mail_fee_config (
  id                            integer auto_increment not null,
  region_code                   varchar(255),
  region_name                   varchar(255),
  first_weight_fee              bigint not null,
  org_id                        bigint not null,
  next_weight_fee               bigint not null,
  first_amount_fee              bigint not null,
  next_amount_fee               bigint not null,
  upto_money_free               bigint not null,
  upto_amount_free              bigint not null,
  title                         varchar(255),
  method                        integer not null,
  first_km_fee                  bigint not null,
  next_km_fee                   bigint not null,
  update_time                   bigint not null,
  constraint pk_v1_mail_fee_config primary key (id)
);

create table management_duty (
  id                            bigint auto_increment not null,
  teacher_id                    bigint,
  duty_type                     varchar(255),
  duty_name                     varchar(255),
  start_date                    date,
  end_date                      date,
  performance_evaluation        varchar(255),
  duty_score                    double,
  constraint pk_management_duty primary key (id)
);

create table v1_member (
  id                            bigint auto_increment not null,
  login_password                varchar(255),
  pay_password                  varchar(255),
  status                        integer not null,
  real_name                     varchar(255),
  nick_name                     varchar(255),
  phone_number                  varchar(255),
  contact_number                varchar(255),
  create_time                   bigint not null,
  dealer_id                     bigint not null,
  dealer_type                   bigint not null,
  level                         integer not null,
  shop_id                       bigint not null,
  shop_name                     varchar(255),
  org_id                        bigint not null,
  org_name                      varchar(255),
  avatar                        varchar(255),
  user_type                     integer not null,
  member_no                     varchar(255),
  birthday                      bigint not null,
  birthday_month                integer not null,
  birthday_day                  integer not null,
  barcode_img_url               varchar(255),
  open_id                       varchar(255),
  session_key                   varchar(255),
  union_id                      varchar(255),
  id_card_no                    varchar(255),
  filter                        varchar(255),
  note                          varchar(255),
  user_note                     varchar(255),
  inside_number                 varchar(255),
  dealer_level                  bigint not null,
  sex                           integer not null,
  constraint pk_v1_member primary key (id)
);

create table v1_member_balance (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  item_id                       integer not null,
  left_balance                  bigint not null,
  freeze_balance                bigint not null,
  total_balance                 bigint not null,
  real_pay                      bigint not null,
  org_id                        bigint not null,
  give                          bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_member_balance primary key (id)
);

create table v1_member_card_coupon (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  user_name                     varchar(255),
  card_coupon_id                bigint not null,
  shop_id                       bigint not null,
  order_id                      bigint not null,
  title                         varchar(255),
  content                       varchar(255),
  digest                        varchar(255),
  shop_name                     varchar(255),
  shop_address                  varchar(255),
  shop_avatar                   varchar(255),
  end_time                      bigint not null,
  status                        bigint not null,
  code                          varchar(255),
  tx_id                         varchar(255),
  sub_id                        varchar(255),
  third_tx_id                   varchar(255),
  barcode                       varchar(255),
  pay_type                      integer not null,
  real_pay                      integer not null,
  update_time                   bigint not null,
  use_time                      bigint not null,
  product_id                    bigint not null,
  sku_id                        bigint not null,
  no_limit_count                tinyint(1) default 0 not null,
  use_count                     bigint not null,
  discount                      bigint not null,
  begin_time                    bigint not null,
  only_pick_up                  tinyint(1) default 0 not null,
  constraint pk_v1_member_card_coupon primary key (id)
);

create table v1_member_coupon (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  user_name                     varchar(255),
  coupon_id                     bigint not null,
  org_id                        bigint not null,
  coupon_name                   varchar(255),
  amount                        integer not null,
  rule_content                  varchar(255),
  begin_time                    bigint not null,
  end_time                      bigint not null,
  status                        bigint not null,
  code                          varchar(255),
  tx_id                         varchar(255),
  sub_id                        varchar(255),
  pay_type                      integer not null,
  real_pay                      integer not null,
  update_time                   bigint not null,
  use_time                      bigint not null,
  type_rules                    varchar(255),
  use_rules                     varchar(255),
  area_id                       bigint not null,
  source                        integer not null,
  constraint pk_v1_member_coupon primary key (id)
);

create table v1_member_level (
  id                            integer auto_increment not null,
  need_score                    bigint not null,
  level                         integer not null,
  level_name                    varchar(255),
  sketch                        varchar(255),
  order_discount                integer not null,
  award_ratio                   double not null,
  coupon                        varchar(255),
  mail_free                     integer not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  org_id                        bigint not null,
  constraint pk_v1_member_level primary key (id)
);

create table v1_member_score_config (
  id                            bigint auto_increment not null,
  type                          integer not null,
  description                   varchar(255),
  score                         bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_member_score_config primary key (id)
);

create table cp_menu (
  id                            integer auto_increment not null,
  sort                          integer not null,
  parent_id                     integer not null,
  enable                        tinyint(1) default 0 not null,
  hidden                        tinyint(1) default 0 not null,
  path                          varchar(255),
  name                          varchar(255),
  component                     varchar(255),
  redirect                      varchar(255),
  title                         varchar(255),
  icon                          varchar(255),
  no_cache                      tinyint(1) default 0 not null,
  relative_path                 varchar(255),
  active_menu                   varchar(255),
  create_time                   bigint not null,
  constraint pk_cp_menu primary key (id)
);

create table teacher_moral_ethics (
  id                            bigint auto_increment not null,
  teacher_id                    bigint,
  assessment_plan_id            bigint,
  ten_criteria_compliance       tinyint(1),
  has_violation_behavior        tinyint(1),
  has_negative_list_behavior    tinyint(1),
  evaluation_level              varchar(11),
  is_qualified                  tinyint(1),
  evaluation_date               date,
  evaluator                     varchar(255),
  constraint pk_teacher_moral_ethics primary key (id)
);

create table v1_msg (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  item_id                       integer not null,
  title                         varchar(255),
  content                       varchar(255),
  link_url                      varchar(255),
  msg_type                      integer not null,
  category_id                   integer not null,
  area_id                       bigint not null,
  target_id                     bigint not null,
  status                        integer not null,
  change_amount                 bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_msg primary key (id)
);

create table v1_news_search (
  id                            bigint auto_increment not null,
  keyword                       varchar(255),
  views                         bigint not null,
  org_id                        bigint not null,
  constraint pk_v1_news_search primary key (id)
);

create table v1_operation_log (
  id                            bigint auto_increment not null,
  admin_id                      bigint not null,
  admin_name                    varchar(255),
  ip                            varchar(255),
  place                         varchar(255),
  note                          varchar(255),
  create_time                   bigint not null,
  constraint pk_v1_operation_log primary key (id)
);

create table v1_order (
  id                            bigint auto_increment not null,
  area_id                       bigint not null,
  org_id                        bigint not null,
  org_name                      varchar(255),
  uid                           bigint not null,
  user_name                     varchar(255),
  order_no                      varchar(255),
  status                        integer not null,
  post_service_status           integer not null,
  mp_status                     integer not null,
  product_count                 integer not null,
  total_money                   bigint not null,
  total_return_money            bigint not null,
  total_return_number           bigint not null,
  real_pay                      bigint not null,
  discount_money                bigint not null,
  mail_fee                      bigint not null,
  packing_fee                   bigint not null,
  pay_method                    integer not null,
  print_count                   integer not null,
  pay_method_name               varchar(255),
  barcode                       varchar(255),
  pickup_code                   varchar(255),
  update_time                   bigint not null,
  pay_time                      bigint not null,
  create_time                   bigint not null,
  note                          varchar(255),
  phone_number                  varchar(255),
  errand_status                 integer not null,
  print_status                  integer not null,
  delivery_type                 integer not null,
  schedule_time                 bigint not null,
  order_type                    integer not null,
  paid_up                       bigint not null,
  description                   varchar(255),
  filter                        varchar(255),
  driver_take_task_time         bigint not null,
  driver_pickup_time            bigint not null,
  driver_delivered_time         bigint not null,
  shop_place_id                 bigint not null,
  shop_place                    varchar(255),
  storage_place_Id              bigint not null,
  storage_place                 varchar(255),
  cabinet_code                  varchar(255),
  cabinet_id                    bigint not null,
  cabinet_box                   integer not null,
  packing_box_use               integer not null,
  packing_box_money             bigint not null,
  packing_bag_money             bigint not null,
  packing_box_price             bigint not null,
  constraint pk_v1_order primary key (id)
);

create table v1_order_commission (
  id                            bigint auto_increment not null,
  area_id                       bigint not null,
  org_id                        bigint not null,
  org_name                      varchar(255),
  order_id                      bigint not null,
  order_no                      varchar(255),
  total_money                   bigint not null,
  order_total                   bigint not null,
  products                      bigint not null,
  real_pay                      bigint not null,
  addition_fee                  bigint not null,
  shop_deliver_fee              bigint not null,
  shop_receive_deliver_fee      bigint not null,
  packing_fee                   bigint not null,
  shop_favor                    bigint not null,
  platform_favor                bigint not null,
  status                        integer not null,
  driver_deliver_fee            bigint not null,
  commission                    bigint not null,
  fix_fee                       bigint not null,
  income                        bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_order_commission primary key (id)
);

create table v1_order_coupon (
  id                            bigint auto_increment not null,
  order_id                      bigint not null,
  uid                           bigint not null,
  member_coupon_id              bigint not null,
  coupon_name                   varchar(255),
  coupon_free                   varchar(255),
  status                        integer not null,
  create_time                   bigint not null,
  constraint pk_v1_order_coupon primary key (id)
);

create table v1_order_dealer (
  id                            bigint auto_increment not null,
  order_id                      bigint not null,
  order_no                      varchar(255),
  status                        integer not null,
  award_type                    integer not null,
  real_pay                      bigint not null,
  award_amount                  bigint not null,
  dealer_id                     bigint not null,
  dealer_name                   varchar(255),
  user_id                       bigint not null,
  user_name                     varchar(255),
  order_settlement_time         bigint not null,
  commission_handled            tinyint(1) default 0 not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_order_dealer primary key (id)
);

create table v1_order_delivery (
  id                            bigint auto_increment not null,
  order_id                      bigint not null,
  order_no                      varchar(255),
  delivery_no                   varchar(255),
  express_no                    varchar(255),
  express_company               varchar(255),
  contact_name                  varchar(255),
  contact_address               varchar(255),
  contact_phone_number          varchar(255),
  uid                           bigint not null,
  user_name                     varchar(255),
  operator_id                   bigint not null,
  operator_name                 varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_order_delivery primary key (id)
);

create table v1_order_delivery_detail (
  id                            bigint auto_increment not null,
  order_delivery_id             bigint not null,
  order_id                      bigint not null,
  order_detail_id               bigint not null,
  product_name                  varchar(255),
  sku_name                      varchar(255),
  product_img_url               varchar(255),
  product_price                 bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_order_delivery_detail primary key (id)
);

create table v1_order_delivery_form (
  id                            bigint auto_increment not null,
  delivery_no                   varchar(255),
  order_id                      bigint not null,
  org_id                        bigint not null,
  order_no                      varchar(255),
  uid                           bigint not null,
  user_name                     varchar(255),
  operator_id                   bigint not null,
  operator_name                 varchar(255),
  express_no                    varchar(255),
  express_company               varchar(255),
  contact_name                  varchar(255),
  contact_address               varchar(255),
  contact_phone_number          varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_order_delivery_form primary key (id)
);

create table v1_order_detail (
  id                            bigint auto_increment not null,
  order_id                      bigint not null,
  org_id                        bigint not null,
  shop_id                       bigint not null,
  uid                           bigint not null,
  product_id                    bigint not null,
  product_name                  varchar(255),
  category_id                   bigint not null,
  category_name                 varchar(255),
  purchasing_category_id        bigint not null,
  purchasing_category_name      varchar(255),
  old_price                     bigint not null,
  weight                        double not null,
  product_price                 bigint not null,
  purchasing_price              bigint not null,
  result_price                  bigint not null,
  sku_id                        bigint not null,
  sku_type                      integer not null,
  sku_name                      varchar(255),
  unit                          varchar(255),
  product_img_url               varchar(255),
  product_mode_desc             varchar(255),
  product_mode_params           varchar(255),
  discount_rate                 integer not null,
  discount_amount               bigint not null,
  number                        bigint not null,
  storage_number                bigint not null,
  sub_total                     bigint not null,
  already_delivery_number       bigint not null,
  is_product_available          tinyint(1) default 0 not null,
  exclude_discount              tinyint(1) default 0 not null,
  remark                        varchar(255),
  source                        integer not null,
  delivery_method               integer not null,
  return_status                 bigint not null,
  sub_return                    bigint not null,
  return_number                 bigint not null,
  update_time                   bigint not null,
  take_time                     bigint not null,
  create_time                   bigint not null,
  sku_quantity                  bigint not null,
  staff_id                      bigint not null,
  status                        bigint not null,
  supplier_id                   bigint not null,
  bid_price                     bigint not null,
  constraint pk_v1_order_detail primary key (id)
);

create table v1_order_favor_detail (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  order_id                      bigint not null,
  score_gave                    bigint not null,
  score_use                     bigint not null,
  score_to_money                bigint not null,
  coupon_id                     bigint not null,
  member_card_coupon_id         bigint not null,
  coupon_free                   bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_order_favor_detail primary key (id)
);

create table v1_order_gift (
  id                            bigint auto_increment not null,
  upto                          bigint not null,
  product_id                    bigint not null,
  sku_id                        bigint not null,
  amount                        integer not null,
  gave_amount                   bigint not null,
  total_amount                  bigint not null,
  require_first_order           tinyint(1) default 0 not null,
  enable                        tinyint(1) default 0 not null,
  title                         varchar(255),
  begin_time                    bigint not null,
  end_time                      bigint not null,
  constraint pk_v1_order_gift primary key (id)
);

create table v1_order_log (
  id                            bigint auto_increment not null,
  order_id                      bigint not null,
  org_id                        bigint not null,
  old_status                    integer not null,
  new_status                    integer not null,
  operator_name                 varchar(255),
  note                          varchar(255),
  create_time                   bigint not null,
  constraint pk_v1_order_log primary key (id)
);

create table v1_order_notify (
  id                            bigint auto_increment not null,
  order_no                      varchar(255),
  order_id                      bigint not null,
  order_time                    bigint not null,
  shop_id                       bigint not null,
  shop_name                     varchar(255),
  type                          varchar(255),
  have_notify                   tinyint(1) default 0 not null,
  create_time                   bigint not null,
  constraint pk_v1_order_notify primary key (id)
);

create table v1_order_pay_detail (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  order_id                      bigint not null,
  org_id                        bigint not null,
  shop_id                       bigint not null,
  pay_method                    integer not null,
  pay_method_name               varchar(255),
  out_trade_no                  varchar(255),
  pay_tx_no                     varchar(255),
  pay_time                      bigint not null,
  refund_tx_id                  varchar(255),
  pay_detail                    varchar(255),
  paid_up                       bigint not null,
  account_settle                integer not null,
  cost                          bigint not null,
  refund_status                 integer not null,
  refund_operator_id            bigint not null,
  refund_operator_name          varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_order_pay_detail primary key (id)
);

create table v1_order_print_detail (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  order_id                      bigint not null,
  order_no                      varchar(255),
  uid                           bigint not null,
  source                        integer not null,
  print_status                  integer not null,
  print_result_data             varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_order_print_detail primary key (id)
);

create table v1_order_read_status (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  order_id                      bigint not null,
  has_read                      tinyint(1) default 0 not null,
  uid                           bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_order_read_status primary key (id)
);

create table v1_order_receive_detail (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  order_id                      bigint not null,
  org_id                        bigint not null,
  contact_detail_id             bigint not null,
  address                       varchar(255),
  phone_number                  varchar(255),
  name                          varchar(255),
  delivery_time                 bigint not null,
  express_no                    varchar(255),
  express_company               varchar(255),
  delivery_type                 integer not null,
  pickup_note                   varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  location                      varchar(255),
  schedule_time                 varchar(255),
  pick_code                     varchar(255),
  table_no                      varchar(255),
  dine_in_numbers               varchar(255),
  operator_id                   bigint not null,
  need_bag                      tinyint(1) default 0 not null,
  operator_name                 varchar(255),
  distance                      bigint not null,
  constraint pk_v1_order_receive_detail primary key (id)
);

create table v1_order_return_delivery_form (
  id                            bigint auto_increment not null,
  delivery_no                   varchar(255),
  order_id                      bigint not null,
  order_no                      varchar(255),
  return_apply_id               bigint not null,
  return_no                     varchar(255),
  uid                           bigint not null,
  user_name                     varchar(255),
  operator_id                   bigint not null,
  operator_name                 varchar(255),
  express_no                    varchar(255),
  express_company               varchar(255),
  contact_name                  varchar(255),
  contact_address               varchar(255),
  contact_phone_number          varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_order_return_delivery_form primary key (id)
);

create table v1_order_return_refund_form (
  id                            bigint auto_increment not null,
  refund_no                     varchar(255),
  order_id                      bigint not null,
  order_no                      varchar(255),
  return_apply_id               bigint not null,
  return_no                     varchar(255),
  uid                           bigint not null,
  user_name                     varchar(255),
  operator_id                   bigint not null,
  operator_name                 varchar(255),
  refund_money                  bigint not null,
  refund_method                 bigint not null,
  status                        integer not null,
  pay_method                    integer not null,
  tx_data                       varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_order_return_refund_form primary key (id)
);

create table v1_order_returns (
  id                            bigint auto_increment not null,
  returns_no                    varchar(255),
  return_tx                     varchar(255),
  shop_id                       bigint not null,
  order_id                      bigint not null,
  org_id                        bigint not null,
  order_no                      varchar(255),
  order_detail_id               bigint not null,
  express_no                    varchar(255),
  consignee_realname            varchar(255),
  consignee_phone_number        varchar(255),
  consignee_address             varchar(255),
  consignee_postcode            varchar(255),
  logis_name                    varchar(255),
  state                         integer not null,
  pre_status                    integer not null,
  status                        integer not null,
  uid                           bigint not null,
  operator_id                   bigint not null,
  operator_name                 varchar(255),
  logistics_last_desc           varchar(255),
  logistics_desc                varchar(255),
  return_type                   integer not null,
  handling_way                  varchar(255),
  return_money                  bigint not null,
  return_submit_time            bigint not null,
  handling_return_time          bigint not null,
  return_submit_end_time        bigint not null,
  handling_apply_end_time       bigint not null,
  handling_return_end_time      bigint not null,
  handling_refund_end_time      bigint not null,
  handling_refund_time          bigint not null,
  refund_resp                   varchar(255),
  reason                        varchar(255),
  remark                        varchar(255),
  audit_reason                  varchar(255),
  audit_remark                  varchar(255),
  total_return_number           bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_order_returns primary key (id)
);

create table v1_order_returns_apply (
  id                            bigint auto_increment not null,
  order_no                      varchar(255),
  order_detail_id               bigint not null,
  returns_no                    varchar(255),
  uid                           bigint not null,
  org_id                        bigint not null,
  state                         integer not null,
  delivery_status               integer not null,
  reason                        varchar(255),
  status                        integer not null,
  return_submit_time            bigint not null,
  audit_time                    bigint not null,
  audit_content                 varchar(255),
  note                          varchar(255),
  operator_id                   bigint not null,
  operator_name                 varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_order_returns_apply primary key (id)
);

create table v1_order_returns_detail (
  id                            bigint auto_increment not null,
  return_id                     bigint not null,
  order_detail_id               bigint not null,
  old_price                     bigint not null,
  product_price                 bigint not null,
  return_amount                 bigint not null,
  product_name                  varchar(255),
  sku_name                      varchar(255),
  product_img_url               varchar(255),
  create_time                   bigint not null,
  constraint pk_v1_order_returns_detail primary key (id)
);

create table v1_order_returns_img (
  id                            bigint auto_increment not null,
  order_id                      bigint not null,
  order_detail_id               bigint not null,
  return_apply_id               bigint not null,
  uid                           bigint not null,
  org_id                        bigint not null,
  img_url                       varchar(255),
  create_time                   bigint not null,
  constraint pk_v1_order_returns_img primary key (id)
);

create table v1_org (
  id                            bigint auto_increment not null,
  status                        integer not null,
  area_id                       bigint not null,
  score                         integer not null,
  sold_amount                   bigint not null,
  min_to_send                   integer not null,
  delivery_target               varchar(255),
  tag                           varchar(255),
  area_name                     varchar(255),
  name                          varchar(255),
  digest                        varchar(255),
  contact_number                varchar(255),
  contact_name                  varchar(255),
  contact_address               varchar(255),
  license_number                varchar(255),
  license_img                   varchar(255),
  description                   varchar(255),
  shop_amount                   bigint not null,
  shop_max_amount               bigint not null,
  sort                          bigint not null,
  create_uid                    bigint not null,
  create_name                   bigint not null,
  charge_time                   bigint not null,
  renew_time                    bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  filter                        varchar(255),
  avatar                        varchar(255),
  rect_logo                     varchar(255),
  company_name                  varchar(255),
  company_type                  integer not null,
  license_type                  integer not null,
  business_items                varchar(255),
  open_time                     integer not null,
  close_time                    integer not null,
  open_time_minute              integer not null,
  close_time_minute             integer not null,
  shop_place_id                 bigint not null,
  lat                           double not null,
  lon                           double not null,
  shop_place_name               varchar(255),
  floor                         varchar(255),
  recommend                     integer not null,
  packing_fee                   integer not null,
  packing_box_price             bigint not null,
  delivery_fee                  integer not null,
  time_to_deliver               bigint not null,
  min_deliver_fee               integer not null,
  announcement                  varchar(255),
  enable                        integer not null,
  bank_name                     varchar(255),
  bank_account_name             varchar(255),
  bank_card                     varchar(255),
  commission_percent            double not null,
  delivery_type                 integer not null,
  service_fee                   bigint not null,
  same_district_deliver_fee     bigint not null,
  diff_district_deliver_fee     bigint not null,
  receiver_id                   varchar(255),
  profit_share                  tinyint(1) default 0 not null,
  constraint pk_v1_org primary key (id)
);

create table v1_org_charge (
  id                            bigint auto_increment not null,
  charge_time                   bigint not null,
  charge_amount                 bigint not null,
  renew_time                    bigint not null,
  operator_name                 bigint not null,
  proof                         varchar(255),
  create_time                   bigint not null,
  constraint pk_v1_org_charge primary key (id)
);

create table v1_print_template (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  org_name                      varchar(255),
  title                         varchar(255),
  template                      varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_print_template primary key (id)
);

create table v1_out_stock_reg (
  id                            bigint auto_increment not null,
  product_id                    bigint not null,
  sku_id                        bigint not null,
  org_id                        bigint not null,
  uid                           bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_out_stock_reg primary key (id)
);

create table v1_system_config (
  id                            bigint auto_increment not null,
  config_key                    varchar(255),
  config_value                  varchar(255),
  note                          varchar(255),
  enable                        tinyint(1) default 0 not null,
  org_id                        bigint not null,
  tab_name                      varchar(255),
  org_name                      varchar(255),
  is_encrypt                    tinyint(1) default 0 not null,
  content_type                  integer not null,
  update_time                   bigint not null,
  constraint pk_v1_system_config primary key (id)
);

create table v1_system_config_template (
  id                            bigint auto_increment not null,
  config_key                    varchar(255),
  config_value                  varchar(255),
  note                          varchar(255),
  tab_name                      varchar(255),
  enable                        tinyint(1) default 0 not null,
  content_type                  integer not null,
  is_encrypt                    tinyint(1) default 0 not null,
  update_time                   bigint not null,
  constraint pk_v1_system_config_template primary key (id)
);

create table parent_contact_record (
  id                            bigint auto_increment not null,
  teacher_id                    bigint,
  contact_date                  date,
  contact_type                  varchar(255),
  contact_content               varchar(255),
  parent_feedback               varchar(255),
  effectiveness_evaluation      integer,
  contact_score                 double,
  constraint pk_parent_contact_record primary key (id)
);

create table v1_pay_method (
  id                            bigint auto_increment not null,
  sort                          integer not null,
  name                          varchar(255),
  value                         integer not null,
  pinyin_abbr                   varchar(255),
  need_show                     tinyint(1) default 0 not null,
  free_pay                      tinyint(1) default 0 not null,
  org_id                        bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_pay_method primary key (id)
);

create table v1_poster (
  id                            bigint auto_increment not null,
  img_url                       varchar(255),
  publish_date                  bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_poster primary key (id)
);

create table v1_product (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  area_id                       bigint not null,
  org_id                        bigint not null,
  org_name                      varchar(255),
  category_id                   bigint not null,
  activity_category_id          bigint not null,
  purchasing_category_id        bigint not null,
  type_id                       bigint not null,
  mail_fee_id                   bigint not null,
  sketch                        varchar(255),
  details                       varchar(255),
  keywords                      varchar(255),
  tag                           varchar(255),
  marque                        varchar(255),
  barcode                       varchar(255),
  price                         bigint not null,
  old_price                     bigint not null,
  min_price                     bigint not null,
  min_order_amount              bigint not null,
  max_price                     bigint not null,
  sold_amount                   bigint not null,
  virtual_amount                bigint not null,
  weight                        double not null,
  volume                        double not null,
  cover_img_url                 varchar(255),
  poster                        varchar(255),
  status                        integer not null,
  sort                          integer not null,
  stock                         bigint not null,
  stock_mode                    integer not null,
  warning_stock                 bigint not null,
  cover_frame_id                bigint not null,
  cover_frame_url               varchar(255),
  unit                          varchar(255),
  delivery_methods              varchar(255),
  deleted_at                    bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  enable_mail                   tinyint(1) default 0 not null,
  enable_all_region             tinyint(1) default 0 not null,
  only_pos                      tinyint(1) default 0 not null,
  enable_free_mail              tinyint(1) default 0 not null,
  sku                           varchar(255),
  param                         varchar(255),
  attr                          varchar(255),
  album                         varchar(255),
  dealer_award_json             varchar(255),
  calc_by                       integer not null,
  packing_fee                   integer not null,
  packing_use                   double not null,
  constraint pk_v1_product primary key (id)
);

create table v1_product_cover_frame (
  id                            integer auto_increment not null,
  img_url                       varchar(255),
  name                          varchar(255),
  constraint pk_v1_product_cover_frame primary key (id)
);

create table v1_product_default_recommend (
  id                            bigint auto_increment not null,
  product_id                    bigint not null,
  constraint pk_v1_product_default_recommend primary key (id)
);

create table v1_product_fav (
  id                            bigint auto_increment not null,
  product_id                    bigint not null,
  sku_id                        bigint not null,
  uid                           bigint not null,
  enable                        tinyint(1) default 0 not null,
  create_time                   bigint not null,
  constraint pk_v1_product_fav primary key (id)
);

create table v1_product_recommend (
  id                            bigint auto_increment not null,
  product_id                    bigint not null,
  recommend_product_id          bigint not null,
  constraint pk_v1_product_recommend primary key (id)
);

create table v1_product_relate (
  id                            bigint auto_increment not null,
  combo_product_id              bigint not null,
  relate_product_id             bigint not null,
  constraint pk_v1_product_relate primary key (id)
);

create table v1_product_search (
  id                            bigint auto_increment not null,
  keyword                       varchar(255),
  views                         bigint not null,
  shop_id                       bigint not null,
  constraint pk_v1_product_search primary key (id)
);

create table v1_tab (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  tab_name                      varchar(255),
  head_pic                      varchar(255),
  bg_color                      varchar(255),
  sort                          integer not null,
  enable                        tinyint(1) default 0 not null,
  is_default                    tinyint(1) default 0 not null,
  product_id_list               varchar(255),
  constraint pk_v1_tab primary key (id)
);

create table v1_product_tab_classify (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  product_tab_id                bigint not null,
  classify_id                   bigint not null,
  classify_cover_img_url        varchar(255),
  classify_name                 varchar(255),
  sort                          integer not null,
  constraint pk_v1_product_tab_classify primary key (id)
);

create table v1_product_tag (
  id                            bigint auto_increment not null,
  product_id                    bigint not null,
  tag                           varchar(255),
  constraint pk_v1_product_tag primary key (id)
);

create table v1_product_uni_img (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  rules                         varchar(255),
  constraint pk_v1_product_uni_img primary key (id)
);

create table v1_product_views (
  id                            bigint auto_increment not null,
  product_id                    bigint not null,
  views                         bigint not null,
  constraint pk_v1_product_views primary key (id)
);

create table v1_region (
  id                            integer auto_increment not null,
  region_code                   varchar(255),
  region_name                   varchar(255),
  parent_id                     integer not null,
  region_level                  integer not null,
  region_order                  integer not null,
  region_name_en                varchar(255),
  region_short_name_en          varchar(255),
  constraint pk_v1_region primary key (id)
);

create table v1_return_contact_detail (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  name                          varchar(255),
  province                      varchar(255),
  province_code                 varchar(255),
  city                          varchar(255),
  city_code                     varchar(255),
  area                          varchar(255),
  area_code                     varchar(255),
  details                       varchar(255),
  postcode                      varchar(255),
  telephone                     varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_return_contact_detail primary key (id)
);

create table v1_score_category (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  parent_id                     bigint not null,
  name                          varchar(255),
  img_url                       varchar(255),
  poster                        varchar(255),
  path                          varchar(255),
  pinyin_abbr                   varchar(255),
  path_name                     varchar(255),
  is_shown                      integer not null,
  exclude                       tinyint(1) default 0 not null,
  need_time_to_make             tinyint(1) default 0 not null,
  sort                          integer not null,
  category_type                 integer not null,
  sold_amount                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_score_category primary key (id)
);

create table v1_score_product (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  org_id                        bigint not null,
  org_name                      varchar(255),
  category_id                   bigint not null,
  purchasing_category_id        bigint not null,
  type_id                       bigint not null,
  mail_fee_id                   bigint not null,
  is_virtual                    tinyint(1) default 0 not null,
  coupon                        varchar(255),
  sketch                        varchar(255),
  details                       varchar(255),
  keywords                      varchar(255),
  tag                           varchar(255),
  marque                        varchar(255),
  barcode                       varchar(255),
  score                         bigint not null,
  convert_amount                bigint not null,
  old_score                     bigint not null,
  min_score                     bigint not null,
  max_score                     bigint not null,
  sold_amount                   bigint not null,
  virtual_amount                bigint not null,
  weight                        double not null,
  volume                        double not null,
  cover_img_url                 varchar(255),
  poster                        varchar(255),
  status                        integer not null,
  sort                          integer not null,
  stock                         bigint not null,
  warning_stock                 bigint not null,
  cover_frame_id                bigint not null,
  cover_frame_url               varchar(255),
  unit                          varchar(255),
  delivery_methods              varchar(255),
  deleted_at                    bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  inner_use                     tinyint(1) default 0 not null,
  enable_mail                   tinyint(1) default 0 not null,
  enable_all_region             tinyint(1) default 0 not null,
  enable_free_mail              tinyint(1) default 0 not null,
  sku                           varchar(255),
  param                         varchar(255),
  attr                          varchar(255),
  album                         varchar(255),
  constraint pk_v1_score_product primary key (id)
);

create table v1_search_keyword (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  source                        integer not null,
  enable                        tinyint(1) default 0 not null,
  keyword                       varchar(255),
  sort                          bigint not null,
  constraint pk_v1_search_keyword primary key (id)
);

create table v1_search_log (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  keyword                       varchar(255),
  views                         bigint not null,
  constraint pk_v1_search_log primary key (id)
);

create table v1_shop (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  org_name                      varchar(255),
  status                        integer not null,
  run_type                      integer not null,
  shop_level                    integer not null,
  name                          varchar(255),
  digest                        varchar(255),
  contact_number                varchar(255),
  contact_name                  varchar(255),
  contact_address               varchar(255),
  license_number                varchar(255),
  license_img                   varchar(255),
  description                   varchar(255),
  approve_note                  varchar(255),
  log                           varchar(255),
  creator_id                    bigint not null,
  approver_id                   bigint not null,
  lat                           double not null,
  lon                           double not null,
  open_time                     integer not null,
  close_time                    integer not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  auth_sec                      varchar(255),
  filter                        varchar(255),
  business_time                 varchar(255),
  avatar                        varchar(255),
  rect_logo                     varchar(255),
  product_counts                bigint not null,
  views                         bigint not null,
  tags                          varchar(255),
  images                        varchar(255),
  discount_str                  varchar(255),
  branches                      varchar(255),
  discount                      integer not null,
  bid_discount                  integer not null,
  average_consumption           integer not null,
  order_count                   bigint not null,
  sort                          integer not null,
  place_top                     tinyint(1) default 0 not null,
  bulletin                      varchar(255),
  env_images                    varchar(255),
  open_time_minute              integer not null,
  close_time_minute             integer not null,
  constraint pk_v1_shop primary key (id)
);

create table v1_shop_admin (
  id                            bigint auto_increment not null,
  username                      varchar(255),
  realname                      varchar(255),
  avatar                        varchar(255),
  password                      varchar(255),
  create_time                   bigint not null,
  last_time                     bigint not null,
  last_ip                       varchar(255),
  phone_number                  varchar(255),
  is_admin                      tinyint(1) default 0 not null,
  org_id                        bigint not null,
  area_id                       bigint not null,
  org_name                      varchar(255),
  shop_id                       bigint not null,
  shop_name                     varchar(255),
  rules                         varchar(255),
  pinyin_abbr                   varchar(255),
  status                        integer not null,
  bg_img_url                    varchar(255),
  constraint pk_v1_shop_admin primary key (id)
);

create table v1_shop_balance (
  id                            bigint auto_increment not null,
  item_id                       integer not null,
  left_balance                  bigint not null,
  freeze_balance                bigint not null,
  total_balance                 bigint not null,
  real_pay                      bigint not null,
  org_id                        bigint not null,
  give                          bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_shop_balance primary key (id)
);

create table v1_shop_delivered_log (
  id                            bigint auto_increment not null,
  order_id                      bigint not null,
  org_id                        bigint not null,
  shop_admin_id                 bigint not null,
  member_id                     bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_shop_delivered_log primary key (id)
);

create table v1_shop_fav (
  id                            bigint auto_increment not null,
  shop_id                       bigint not null,
  uid                           bigint not null,
  enable                        tinyint(1) default 0 not null,
  create_time                   bigint not null,
  constraint pk_v1_shop_fav primary key (id)
);

create table v1_shop_finish_meal (
  id                            bigint auto_increment not null,
  order_id                      bigint not null,
  org_id                        bigint not null,
  shop_admin_id                 bigint not null,
  member_id                     bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_shop_finish_meal primary key (id)
);

create table v1_shop_place (
  id                            bigint auto_increment not null,
  status                        integer not null,
  area_id                       bigint not null,
  area_name                     varchar(255),
  name                          varchar(255),
  contact_number                varchar(255),
  contact_name                  varchar(255),
  address                       varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  lat                           double not null,
  lon                           double not null,
  district                      varchar(255),
  district_id                   bigint not null,
  constraint pk_v1_shop_place primary key (id)
);

create table v1_shop_printer (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  shop_id                       bigint not null,
  printer_type                  integer not null,
  shop_name                     varchar(255),
  printer_sn                    varchar(255),
  printer_key                   varchar(255),
  note                          varchar(255),
  filter                        varchar(255),
  enable                        tinyint(1) default 0 not null,
  update_time                   bigint not null,
  sort                          integer not null,
  only_products                 tinyint(1) default 0 not null,
  category_list                 varchar(255),
  constraint pk_v1_shop_printer primary key (id)
);

create table v1_shop_category (
  id                            bigint auto_increment not null,
  shop_id                       bigint not null,
  parent_id                     bigint not null,
  name                          varchar(255),
  img_url                       varchar(255),
  poster                        varchar(255),
  path                          varchar(255),
  pinyin_abbr                   varchar(255),
  is_shown                      integer not null,
  sort                          integer not null,
  sold_amount                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_shop_category primary key (id)
);

create table v1_shop_search (
  id                            bigint auto_increment not null,
  keyword                       varchar(255),
  views                         bigint not null,
  constraint pk_v1_shop_search primary key (id)
);

create table v1_shop_search_log (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  keyword                       varchar(255),
  views                         bigint not null,
  constraint pk_v1_shop_search_log primary key (id)
);

create table v1_shop_self_took_log (
  id                            bigint auto_increment not null,
  order_id                      bigint not null,
  org_id                        bigint not null,
  shop_admin_id                 bigint not null,
  member_id                     bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_shop_self_took_log primary key (id)
);

create table v1_shop_sku_attr (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  sku_template_id               bigint not null,
  shop_id                       bigint not null,
  sort                          integer not null,
  constraint pk_v1_shop_sku_attr primary key (id)
);

create table v1_shop_sku_attr_option (
  id                            bigint auto_increment not null,
  attr_id                       bigint not null,
  option_id                     bigint not null,
  name                          varchar(255),
  sort                          integer not null,
  constraint pk_v1_shop_sku_attr_option primary key (id)
);

create table v1_shop_sku_template (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  org_id                        bigint not null,
  attr                          varchar(255),
  params                        varchar(255),
  sort                          integer not null,
  constraint pk_v1_shop_sku_template primary key (id)
);

create table v1_shop_tag (
  id                            bigint auto_increment not null,
  shop_id                       bigint not null,
  tag                           varchar(255),
  constraint pk_v1_shop_tag primary key (id)
);

create table v1_shop_take_order_log (
  id                            bigint auto_increment not null,
  order_id                      bigint not null,
  org_id                        bigint not null,
  shop_admin_id                 bigint not null,
  member_id                     bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_shop_take_order_log primary key (id)
);

create table v1_shopping_cart (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  product_id                    bigint not null,
  sku_id                        bigint not null,
  amount                        bigint not null,
  enable                        tinyint(1) default 0 not null,
  update_time                   bigint not null,
  org_id                        bigint not null,
  source                        integer not null,
  create_time                   bigint not null,
  constraint pk_v1_shopping_cart primary key (id)
);

create table v1_show_case (
  id                            integer auto_increment not null,
  title                         varchar(255),
  tags                          varchar(255),
  images                        varchar(255),
  image_count                   bigint not null,
  shop_name                     varchar(255),
  shop_id                       bigint not null,
  constraint pk_v1_show_case primary key (id)
);

create table v1_sku_category_type (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  attr_count                    integer not null,
  param_count                   integer not null,
  sort                          integer not null,
  constraint pk_v1_sku_category_type primary key (id)
);

create table v1_sms_log (
  id                            bigint auto_increment not null,
  msg_id                        varchar(255),
  phone_number                  varchar(255),
  content                       varchar(255),
  extno                         varchar(255),
  req_status                    varchar(255),
  resp_status                   varchar(255),
  org_id                        bigint not null,
  req_time                      bigint not null,
  resp_time                     bigint not null,
  query_resp_time               bigint not null,
  constraint pk_v1_sms_log primary key (id)
);

create table v1_sms_template (
  id                            bigint auto_increment not null,
  sort                          integer not null,
  enable                        tinyint(1) default 0 not null,
  content                       varchar(255),
  template_id                   varchar(255),
  constraint pk_v1_sms_template primary key (id)
);

create table v1_staff_month_salary (
  id                            bigint auto_increment not null,
  username                      varchar(255),
  uid                           bigint not null,
  phone_number                  varchar(255),
  org_id                        bigint not null,
  org_name                      varchar(255),
  shop_id                       bigint not null,
  shop_name                     varchar(255),
  work_days                     bigint not null,
  enroll_time                   bigint not null,
  department                    varchar(255),
  basic_salary                  bigint not null,
  fix_salary                    bigint not null,
  dine_allowance                bigint not null,
  social_assurance_allowance    bigint not null,
  traffic_allowance             bigint not null,
  salary_sub_total              bigint not null,
  full_attendance_award         bigint not null,
  age_award                     bigint not null,
  member_award                  bigint not null,
  product_award                 bigint not null,
  award_sub_total               bigint not null,
  payroll_salary                bigint not null,
  social_assurance_deduction    bigint not null,
  attendance_deduction          bigint not null,
  other_deduction               bigint not null,
  broken_deduction              bigint not null,
  total_deduction               bigint not null,
  pay_advance_salaray           bigint not null,
  bonus                         bigint not null,
  net_salary                    bigint not null,
  bank_no                       varchar(255),
  bank_name                     varchar(255),
  month                         varchar(255),
  create_time                   bigint not null,
  update_time                   bigint not null,
  constraint pk_v1_staff_month_salary primary key (id)
);

create table v1_staff_month_work_day (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  org_name                      varchar(255),
  month                         varchar(255),
  work_day                      integer not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_staff_month_work_day primary key (id)
);

create table v1_staff_salary (
  id                            bigint auto_increment not null,
  username                      varchar(255),
  uid                           bigint not null,
  phone_number                  varchar(255),
  org_id                        bigint not null,
  org_name                      varchar(255),
  shop_id                       bigint not null,
  shop_name                     varchar(255),
  enroll_time                   bigint not null,
  department                    varchar(255),
  basic_salary                  bigint not null,
  fix_salary                    bigint not null,
  dine_allowance                bigint not null,
  social_assurance_allowance    bigint not null,
  traffic_allowance             bigint not null,
  full_attendance_award         bigint not null,
  age_award                     bigint not null,
  bank_no                       varchar(255),
  bank_name                     varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_staff_salary primary key (id)
);

create table v1_stat_area_day (
  id                            bigint auto_increment not null,
  day                           varchar(255),
  area_id                       bigint not null,
  area_name                     varchar(255),
  products                      bigint not null,
  orders                        bigint not null,
  total_money                   bigint not null,
  order_total                   bigint not null,
  real_pay                      bigint not null,
  addition_fee                  bigint not null,
  shop_deliver_fee              bigint not null,
  shop_receive_deliver_fee      bigint not null,
  packing_fee                   bigint not null,
  shop_favor                    bigint not null,
  platform_favor                bigint not null,
  driver_deliver_fee            bigint not null,
  commission                    bigint not null,
  fix_fee                       bigint not null,
  shop_income                   bigint not null,
  platform_income               bigint not null,
  reg_count                     bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_stat_area_day primary key (id)
);

create table v1_stat_area_month (
  id                            bigint auto_increment not null,
  month                         varchar(255),
  area_id                       bigint not null,
  area_name                     varchar(255),
  products                      bigint not null,
  orders                        bigint not null,
  total_money                   bigint not null,
  order_total                   bigint not null,
  real_pay                      bigint not null,
  addition_fee                  bigint not null,
  shop_deliver_fee              bigint not null,
  shop_receive_deliver_fee      bigint not null,
  packing_fee                   bigint not null,
  shop_favor                    bigint not null,
  platform_favor                bigint not null,
  driver_deliver_fee            bigint not null,
  commission                    bigint not null,
  fix_fee                       bigint not null,
  shop_income                   bigint not null,
  platform_income               bigint not null,
  reg_count                     bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_stat_area_month primary key (id)
);

create table v1_stat_platform_day (
  id                            bigint auto_increment not null,
  day                           varchar(255),
  products                      bigint not null,
  orders                        bigint not null,
  total_money                   bigint not null,
  order_total                   bigint not null,
  real_pay                      bigint not null,
  addition_fee                  bigint not null,
  shop_deliver_fee              bigint not null,
  shop_receive_deliver_fee      bigint not null,
  packing_fee                   bigint not null,
  shop_favor                    bigint not null,
  platform_favor                bigint not null,
  driver_deliver_fee            bigint not null,
  commission                    bigint not null,
  fix_fee                       bigint not null,
  shop_income                   bigint not null,
  platform_income               bigint not null,
  reg_count                     bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_stat_platform_day primary key (id)
);

create table v1_stat_platform_month (
  id                            bigint auto_increment not null,
  month                         varchar(255),
  products                      bigint not null,
  orders                        bigint not null,
  total_money                   bigint not null,
  order_total                   bigint not null,
  real_pay                      bigint not null,
  addition_fee                  bigint not null,
  shop_deliver_fee              bigint not null,
  shop_receive_deliver_fee      bigint not null,
  packing_fee                   bigint not null,
  shop_favor                    bigint not null,
  platform_favor                bigint not null,
  driver_deliver_fee            bigint not null,
  commission                    bigint not null,
  fix_fee                       bigint not null,
  shop_income                   bigint not null,
  platform_income               bigint not null,
  reg_count                     bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_stat_platform_month primary key (id)
);

create table v1_stat_shop_day (
  id                            bigint auto_increment not null,
  day                           varchar(255),
  area_id                       bigint not null,
  area_name                     varchar(255),
  org_id                        bigint not null,
  org_name                      varchar(255),
  products                      bigint not null,
  orders                        bigint not null,
  total_money                   bigint not null,
  order_total                   bigint not null,
  real_pay                      bigint not null,
  addition_fee                  bigint not null,
  shop_deliver_fee              bigint not null,
  shop_receive_deliver_fee      bigint not null,
  packing_fee                   bigint not null,
  shop_favor                    bigint not null,
  platform_favor                bigint not null,
  driver_deliver_fee            bigint not null,
  commission                    bigint not null,
  fix_fee                       bigint not null,
  shop_income                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_stat_shop_day primary key (id)
);

create table v1_stat_shop_month (
  id                            bigint auto_increment not null,
  month                         varchar(255),
  area_id                       bigint not null,
  area_name                     varchar(255),
  org_id                        bigint not null,
  org_name                      varchar(255),
  products                      bigint not null,
  orders                        bigint not null,
  total_money                   bigint not null,
  order_total                   bigint not null,
  real_pay                      bigint not null,
  addition_fee                  bigint not null,
  shop_deliver_fee              bigint not null,
  shop_receive_deliver_fee      bigint not null,
  packing_fee                   bigint not null,
  shop_favor                    bigint not null,
  platform_favor                bigint not null,
  driver_deliver_fee            bigint not null,
  commission                    bigint not null,
  fix_fee                       bigint not null,
  shop_income                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_stat_shop_month primary key (id)
);

create table v1_storage_place (
  id                            bigint auto_increment not null,
  status                        integer not null,
  area_id                       bigint not null,
  area_name                     varchar(255),
  name                          varchar(255),
  contact_number                varchar(255),
  contact_name                  varchar(255),
  address                       varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  lat                           double not null,
  sort                          bigint not null,
  lon                           double not null,
  cabinet_id                    bigint not null,
  district                      varchar(255),
  district_id                   bigint not null,
  constraint pk_v1_storage_place primary key (id)
);

create table student_guidance_record (
  id                            bigint auto_increment not null,
  teacher_id                    bigint,
  record_date                   date,
  homework_grading_score        integer,
  personalized_guidance_count   integer,
  psychological_guidance_score  integer,
  learning_support_score        integer,
  all_round_education_score     integer,
  learn_action_customer         integer,
  guidance_score                double,
  constraint pk_student_guidance_record primary key (id)
);

create table v1_suggestion (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  uid                           bigint not null,
  org_id                        bigint not null,
  status                        integer not null,
  content                       varchar(255),
  note                          varchar(255),
  create_time                   bigint not null,
  constraint pk_v1_suggestion primary key (id)
);

create table v1_system_attr (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  sys_cate_type_id              bigint not null,
  sort                          integer not null,
  constraint pk_v1_system_attr primary key (id)
);

create table v1_system_attr_option (
  id                            bigint auto_increment not null,
  attr_id                       bigint not null,
  option_id                     bigint not null,
  name                          varchar(255),
  sort                          integer not null,
  constraint pk_v1_system_attr_option primary key (id)
);

create table v1_system_carousel (
  id                            integer auto_increment not null,
  name                          varchar(255),
  img_url                       varchar(255),
  link_url                      varchar(255),
  mobile_img_url                varchar(255),
  mobile_link_url               varchar(255),
  client_type                   integer not null,
  biz_type                      integer not null,
  sort                          integer not null,
  need_show                     tinyint(1) default 0 not null,
  title1                        varchar(255),
  title2                        varchar(255),
  note                          varchar(255),
  region_code                   varchar(255),
  region_name                   varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_system_carousel primary key (id)
);

create table v1_system_category_type (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  attr_count                    integer not null,
  param_count                   integer not null,
  sort                          integer not null,
  constraint pk_v1_system_category_type primary key (id)
);

create table v1_system_link (
  id                            integer auto_increment not null,
  name                          varchar(255),
  url                           varchar(255),
  sort                          integer not null,
  status                        integer not null,
  note                          varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_system_link primary key (id)
);

create table v1_system_param (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  sys_cate_type_id              bigint not null,
  method                        varchar(255),
  value                         varchar(255),
  sort                          integer not null,
  constraint pk_v1_system_param primary key (id)
);

create table teacher_assessment (
  id                            bigint auto_increment not null,
  teacher_id                    bigint,
  assessment_plan_id            bigint,
  assessment_year               varchar(255),
  moral_ethics_evaluation_id    bigint,
  moral_ethics_qualified        tinyint(1),
  attendance_score              double,
  workload_score                double,
  management_duty_score         double,
  teaching_score                double,
  student_guidance_score        double,
  parent_contact_score          double,
  total_score                   double,
  final_grade                   varchar(11),
  assessment_date               date,
  assessor                      varchar(255),
  constraint pk_teacher_assessment primary key (id)
);

create table v1_upgrade_config (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  version_number                integer not null,
  enable                        tinyint(1) default 0 not null,
  force_update_rule             varchar(255),
  normal_update_rule            varchar(255),
  update_url                    varchar(255),
  update_type                   integer not null,
  version_type                  integer not null,
  platform                      varchar(255),
  note                          varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_upgrade_config primary key (id)
);

create table v1_user_dict (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  count                         bigint not null,
  dict_name                     varchar(255),
  cate_name                     varchar(255),
  pinyin_abbr                   varchar(255),
  constraint pk_v1_user_dict primary key (id)
);

create table v1_voucher_category (
  id                            bigint auto_increment not null,
  org_id                        bigint not null,
  parent_id                     bigint not null,
  name                          varchar(255),
  img_url                       varchar(255),
  poster                        varchar(255),
  path                          varchar(255),
  pinyin_abbr                   varchar(255),
  path_name                     varchar(255),
  is_shown                      integer not null,
  sort                          integer not null,
  category_type                 integer not null,
  sold_amount                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_voucher_category primary key (id)
);

create table v1_voucher_log (
  id                            bigint auto_increment not null,
  category_name                 varchar(255),
  category_id                   bigint not null,
  balance_type                  varchar(255),
  voucher_no                    varchar(255),
  digest                        varchar(255),
  balance_direction             varchar(255),
  amount                        bigint not null,
  uid                           bigint not null,
  status                        integer not null,
  shop_id                       bigint not null,
  org_id                        bigint not null,
  is_public                     tinyint(1) default 0 not null,
  user_name                     varchar(255),
  participants                  bigint not null,
  participants_name             varchar(255),
  filter                        varchar(255),
  balance_time                  bigint not null,
  date                          bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_voucher_log primary key (id)
);

create table v1_withdraw_log (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  balance_log_id                bigint not null,
  amount                        bigint not null,
  real_amount                   bigint not null,
  fee_amount                    bigint not null,
  real_name                     varchar(255),
  bank_number                   varchar(255),
  bank_name                     varchar(255),
  operator_id                   bigint not null,
  operator_name                 varchar(255),
  status                        integer not null,
  note                          varchar(255),
  uuid                          varchar(255),
  payment_no                    varchar(255),
  payment_time                  varchar(255),
  phone_number                  varchar(255),
  open_id                       varchar(255),
  org_id                        bigint not null,
  audit_time                    bigint not null,
  create_time                   bigint not null,
  type                          integer not null,
  constraint pk_v1_withdraw_log primary key (id)
);

create table workload_record (
  id                            bigint auto_increment not null,
  teacher_id                    bigint,
  standard_class_hours          integer,
  actual_class_hours            integer,
  substitution_hours            integer,
  special_class_hours           integer,
  workload_score                double,
  constraint pk_workload_record primary key (id)
);


-- !Downs

-- drop all
drop table if exists cp_system_action;

drop table if exists v1_activity;

drop table if exists v1_activity_category;

drop table if exists v1_admin_config;

drop table if exists cp_member;

drop table if exists v1_agreement;

drop table if exists v1_announcement;

drop table if exists v1_area;

drop table if exists v1_article;

drop table if exists v1_article_category;

drop table if exists v1_article_comment;

drop table if exists v1_article_comment_like;

drop table if exists v1_article_comment_reply;

drop table if exists v1_article_fav;

drop table if exists v1_article_read_log;

drop table if exists assessment_plan;

drop table if exists attendance_record;

drop table if exists v1_balance_log;

drop table if exists v1_birthday_log;

drop table if exists v1_brand;

drop table if exists v1_browse_log;

drop table if exists v1_cabinet;

drop table if exists v1_cabinet_box;

drop table if exists v1_cabinet_callback;

drop table if exists v1_cabinet_log;

drop table if exists v1_card_coupon_config;

drop table if exists v1_category;

drop table if exists v1_category_attr;

drop table if exists v1_category_classify;

drop table if exists v1_product_classify;

drop table if exists classroom_teaching_evaluation;

drop table if exists v1_client_log;

drop table if exists v1_contact_detail;

drop table if exists v1_coupon_config;

drop table if exists v1_default_product_param;

drop table if exists v1_deliver_fee_change_apply;

drop table if exists v1_deliver_fee_change_confirm_log;

drop table if exists v1_delivery_fee;

drop table if exists v1_deposit;

drop table if exists v1_charge_config;

drop table if exists v1_dict;

drop table if exists v1_district;

drop table if exists v1_driver;

drop table if exists v1_driver_audit;

drop table if exists v1_driver_balance;

drop table if exists v1_driver_feedback_log;

drop table if exists v1_everyday_sign_in;

drop table if exists v1_upgrade_grey_user;

drop table if exists cp_group;

drop table if exists cp_group_action;

drop table if exists cp_group_menu;

drop table if exists cp_group_user;

drop table if exists v1_item;

drop table if exists cp_log;

drop table if exists v1_login_log;

drop table if exists v1_logistics;

drop table if exists v1_mail_fee_config;

drop table if exists management_duty;

drop table if exists v1_member;

drop table if exists v1_member_balance;

drop table if exists v1_member_card_coupon;

drop table if exists v1_member_coupon;

drop table if exists v1_member_level;

drop table if exists v1_member_score_config;

drop table if exists cp_menu;

drop table if exists teacher_moral_ethics;

drop table if exists v1_msg;

drop table if exists v1_news_search;

drop table if exists v1_operation_log;

drop table if exists v1_order;

drop table if exists v1_order_commission;

drop table if exists v1_order_coupon;

drop table if exists v1_order_dealer;

drop table if exists v1_order_delivery;

drop table if exists v1_order_delivery_detail;

drop table if exists v1_order_delivery_form;

drop table if exists v1_order_detail;

drop table if exists v1_order_favor_detail;

drop table if exists v1_order_gift;

drop table if exists v1_order_log;

drop table if exists v1_order_notify;

drop table if exists v1_order_pay_detail;

drop table if exists v1_order_print_detail;

drop table if exists v1_order_read_status;

drop table if exists v1_order_receive_detail;

drop table if exists v1_order_return_delivery_form;

drop table if exists v1_order_return_refund_form;

drop table if exists v1_order_returns;

drop table if exists v1_order_returns_apply;

drop table if exists v1_order_returns_detail;

drop table if exists v1_order_returns_img;

drop table if exists v1_org;

drop table if exists v1_org_charge;

drop table if exists v1_print_template;

drop table if exists v1_out_stock_reg;

drop table if exists v1_system_config;

drop table if exists v1_system_config_template;

drop table if exists parent_contact_record;

drop table if exists v1_pay_method;

drop table if exists v1_poster;

drop table if exists v1_product;

drop table if exists v1_product_cover_frame;

drop table if exists v1_product_default_recommend;

drop table if exists v1_product_fav;

drop table if exists v1_product_recommend;

drop table if exists v1_product_relate;

drop table if exists v1_product_search;

drop table if exists v1_tab;

drop table if exists v1_product_tab_classify;

drop table if exists v1_product_tag;

drop table if exists v1_product_uni_img;

drop table if exists v1_product_views;

drop table if exists v1_region;

drop table if exists v1_return_contact_detail;

drop table if exists v1_score_category;

drop table if exists v1_score_product;

drop table if exists v1_search_keyword;

drop table if exists v1_search_log;

drop table if exists v1_shop;

drop table if exists v1_shop_admin;

drop table if exists v1_shop_balance;

drop table if exists v1_shop_delivered_log;

drop table if exists v1_shop_fav;

drop table if exists v1_shop_finish_meal;

drop table if exists v1_shop_place;

drop table if exists v1_shop_printer;

drop table if exists v1_shop_category;

drop table if exists v1_shop_search;

drop table if exists v1_shop_search_log;

drop table if exists v1_shop_self_took_log;

drop table if exists v1_shop_sku_attr;

drop table if exists v1_shop_sku_attr_option;

drop table if exists v1_shop_sku_template;

drop table if exists v1_shop_tag;

drop table if exists v1_shop_take_order_log;

drop table if exists v1_shopping_cart;

drop table if exists v1_show_case;

drop table if exists v1_sku_category_type;

drop table if exists v1_sms_log;

drop table if exists v1_sms_template;

drop table if exists v1_staff_month_salary;

drop table if exists v1_staff_month_work_day;

drop table if exists v1_staff_salary;

drop table if exists v1_stat_area_day;

drop table if exists v1_stat_area_month;

drop table if exists v1_stat_platform_day;

drop table if exists v1_stat_platform_month;

drop table if exists v1_stat_shop_day;

drop table if exists v1_stat_shop_month;

drop table if exists v1_storage_place;

drop table if exists student_guidance_record;

drop table if exists v1_suggestion;

drop table if exists v1_system_attr;

drop table if exists v1_system_attr_option;

drop table if exists v1_system_carousel;

drop table if exists v1_system_category_type;

drop table if exists v1_system_link;

drop table if exists v1_system_param;

drop table if exists teacher_assessment;

drop table if exists v1_upgrade_config;

drop table if exists v1_user_dict;

drop table if exists v1_voucher_category;

drop table if exists v1_voucher_log;

drop table if exists v1_withdraw_log;

drop table if exists workload_record;

