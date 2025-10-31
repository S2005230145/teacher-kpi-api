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

create table tk_assessment_detail (
  id                            bigint auto_increment not null,
  assessment_id                 bigint,
  category_id                   integer,
  evaluation_element            varchar(100),
  evaluation_content            TEXT,
  evaluation_standard           TEXT,
  score                         double,
  max_score                     double,
  evidence_description          TEXT,
  evaluator                     varchar(50),
  evaluate_date                 date,
  constraint pk_tk_assessment_detail primary key (id)
);

create table tk_assessment_element_config (
  id                            bigint auto_increment not null,
  category_id                   integer,
  element_name                  varchar(100),
  element_code                  varchar(50),
  content_description           TEXT,
  evaluation_criteria           TEXT,
  max_score                     double,
  weight                        double,
  calculation_method            varchar(50),
  is_active                     tinyint(1),
  display_order                 integer,
  constraint uq_tk_assessment_element_config_element_code unique (element_code),
  constraint pk_tk_assessment_element_config primary key (id)
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

create table tk_v3_content (
  id                            bigint auto_increment not null,
  element_id                    bigint,
  content                       varchar(255),
  constraint pk_tk_v3_content primary key (id)
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

create table critical_work_assessment (
  id                            bigint auto_increment not null,
  teacher_id                    bigint,
  assessment_id                 bigint,
  academic_year                 varchar(20),
  administrative_positions      varchar(500),
  work_categories               varchar(200),
  specific_duties               varchar(500),
  work_performance              TEXT,
  work_difficulty_level         varchar(20),
  work_importance_level         varchar(20),
  work_completion_evaluation    varchar(50),
  administrative_work_score     double,
  temporary_task_types          varchar(200),
  task_descriptions             TEXT,
  task_start_date               date,
  task_end_date                 date,
  task_urgency_level            varchar(20),
  task_completion_status        varchar(50),
  task_achievements             TEXT,
  temporary_tasks_score         double,
  total_critical_work_score     double,
  constraint pk_critical_work_assessment primary key (id)
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

create table tk_v3_element (
  id                            bigint auto_increment not null,
  indicator_id                  bigint,
  element                       varchar(255),
  criteria                      varchar(255),
  is_auto                       tinyint(1),
  constraint pk_tk_v3_element primary key (id)
);

create table tk_v1_teacher_content (
  id                            bigint auto_increment not null,
  evaluation_content            varchar(255),
  score                         double,
  constraint pk_tk_v1_teacher_content primary key (id)
);

create table tk_v1_evaluation_element (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint pk_tk_v1_evaluation_element primary key (id)
);

create table tk_v1_evaluation_indicator (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  sub_name                      varchar(255),
  kpi_id                        bigint,
  constraint pk_tk_v1_evaluation_indicator primary key (id)
);

create table v1_everyday_sign_in (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_everyday_sign_in primary key (id)
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

create table tk_v3_indicator (
  id                            bigint auto_increment not null,
  kpi_id                        bigint,
  indicator_name                varchar(255),
  sub_name                      varchar(255),
  constraint pk_tk_v3_indicator primary key (id)
);

create table tk_v3_kpi (
  id                            bigint auto_increment not null,
  title                         varchar(255),
  constraint pk_tk_v3_kpi primary key (id)
);

create table tk_v1_kpi (
  id                            bigint auto_increment not null,
  start_time                    datetime(6),
  end_time                      datetime(6),
  user_id                       bigint,
  parent_id                     bigint,
  total_score                   double,
  constraint pk_tk_v1_kpi primary key (id)
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

create table moral_ethics_evaluation (
  id                            bigint auto_increment not null,
  teacher_id                    bigint not null,
  assessment_id                 bigint,
  behavior_standard_compliance  tinyint(1),
  has_violation_behavior        tinyint(1),
  has_negative_list_behavior    tinyint(1),
  evaluation_level_id           integer,
  evaluation_basis              TEXT,
  evaluator                     varchar(50),
  evaluate_date                 date,
  constraint pk_moral_ethics_evaluation primary key (id)
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

create table personal_development_assessment (
  id                            bigint auto_increment not null,
  teacher_id                    bigint,
  assessment_id                 bigint,
  academic_year                 varchar(20),
  demonstration_classes_count   integer,
  observation_classes_count     integer,
  special_lectures_count        integer,
  class_observation_activities  integer,
  regional_exchange_activities  integer,
  activity_levels               varchar(100),
  public_teaching_score         double,
  education_papers_count        integer,
  paper_titles                  TEXT,
  paper_types                   varchar(100),
  award_status                  varchar(50),
  compilation_level             varchar(50),
  publish_date                  date,
  papers_score                  double,
  research_projects             TEXT,
  research_levels               varchar(100),
  research_roles                varchar(100),
  subject_proposition_work      tinyint(1),
  continuing_education_completion tinyint(1),
  continuing_education_hours    integer,
  research_participation_score  double,
  teaching_special_awards       varchar(500),
  comprehensive_honors          varchar(500),
  teaching_competition_awards   varchar(500),
  excellent_teacher_certification varchar(255),
  award_dates                   varchar(255),
  personal_awards_score         double,
  total_personal_development_score double,
  constraint pk_personal_development_assessment primary key (id)
);

create table tk_v1_role (
  id                            bigint auto_increment not null,
  nick_name                     varchar(255),
  constraint pk_tk_v1_role primary key (id)
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

create table v1_shop_tag (
  id                            bigint auto_increment not null,
  shop_id                       bigint not null,
  tag                           varchar(255),
  constraint pk_v1_shop_tag primary key (id)
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

create table tk_v3_teacher_content_score (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  content_id                    bigint,
  element_id                    bigint,
  score                         double,
  constraint pk_tk_v3_teacher_content_score primary key (id)
);

create table tk_v3_teacher_element_score (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  element_id                    bigint,
  kpi_id                        bigint,
  score                         double,
  constraint pk_tk_v3_teacher_element_score primary key (id)
);

create table tk_v3_teacher_kpi_score (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  kpi_id                        bigint,
  score                         double,
  constraint pk_tk_v3_teacher_kpi_score primary key (id)
);

create table tk_teacher_performance_assessment (
  id                            bigint auto_increment not null,
  teacher_id                    bigint not null,
  academic_year                 varchar(20),
  assessment_date               date,
  total_score                   double,
  final_grade                   varchar(20),
  assessor                      varchar(50),
  assessment_status             varchar(20),
  constraint pk_tk_teacher_performance_assessment primary key (id)
);

create table tk_v1_teacher_performance_assessment (
  id                            bigint auto_increment not null,
  element_ids                   varchar(255),
  evaluation_id                 bigint,
  content_ids                   varchar(255),
  evaluation_standard           varchar(255),
  score                         double,
  constraint pk_tk_v1_teacher_performance_assessment primary key (id)
);

create table teaching_achievement_assessment (
  id                            bigint auto_increment not null,
  teacher_id                    bigint,
  assessment_id                 bigint,
  academic_year                 varchar(20),
  student_academic_development  varchar(255),
  exam_types                    varchar(200),
  average_score                 double,
  pass_rate                     double,
  excellence_rate               double,
  score_improvement             double,
  city_ranking                  integer,
  school_ranking                integer,
  academic_score                double,
  mentorship_agreement_fulfillment tinyint(1),
  guidance_effectiveness        varchar(500),
  research_group_work           varchar(300),
  master_studio_participation   tinyint(1),
  training_instructor_role      tinyint(1),
  rural_teaching_activities     tinyint(1),
  demonstration_score           double,
  class_meeting_awards          varchar(500),
  team_activity_awards          varchar(500),
  single_competition_awards     varchar(500),
  comprehensive_honors          varchar(500),
  award_levels                  varchar(100),
  class_awards_score            double,
  subject_competition_guidance  varchar(500),
  innovation_competition_guidance varchar(500),
  sports_league_guidance        varchar(500),
  art_performance_guidance      varchar(500),
  other_competition_guidance    varchar(500),
  student_awards_levels         varchar(100),
  student_guidance_score        double,
  school_based_curriculum_development tinyint(1),
  research_learning_guidance    tinyint(1),
  comprehensive_practice_organization tinyint(1),
  social_practice_organization  tinyint(1),
  club_activities_guidance      tinyint(1),
  extracurricular_tutor_role    tinyint(1),
  school_activities_score       double,
  teaching_research_participation tinyint(1),
  collective_preparation_participation tinyint(1),
  resource_sharing              tinyint(1),
  post_training_participation   tinyint(1),
  training_learning_participation tinyint(1),
  class_observation_completion  tinyint(1),
  team_cooperation_score        double,
  student_satisfaction_rate     double,
  parent_satisfaction_rate      double,
  evaluation_participant_count  integer,
  satisfaction_score            double,
  total_teaching_achievement_score double,
  constraint pk_teaching_achievement_assessment primary key (id)
);

create table teaching_routine_assessment (
  id                            bigint auto_increment not null,
  teacher_id                    bigint,
  assessment_id                 bigint,
  academic_year                 varchar(20),
  sick_leave_days               integer,
  personal_leave_days           integer,
  late_times                    integer,
  absence_times                 integer,
  political_study_attendance    tinyint(1),
  teaching_research_attendance  tinyint(1),
  school_meeting_attendance     tinyint(1),
  other_collective_activities_attendance tinyint(1),
  attendance_score              double,
  standard_class_hours          integer,
  actual_class_hours            integer,
  substitution_hours            integer,
  special_class_hours           integer,
  workload_completion_rate      double,
  workload_score                double,
  class_teacher_role            tinyint(1),
  grade_leader_role             tinyint(1),
  teaching_research_leader_role tinyint(1),
  young_pioneer_leader_role     tinyint(1),
  youth_league_secretary_role   tinyint(1),
  middle_manager_role           tinyint(1),
  management_position_description varchar(500),
  management_work_evaluation    varchar(500),
  management_score              double,
  teaching_plan_quality         varchar(255),
  teaching_organization         varchar(255),
  classroom_management          varchar(255),
  teaching_concept              varchar(255),
  classroom_effectiveness       varchar(255),
  moral_education_integration   varchar(255),
  modern_education_tech_integration varchar(255),
  classroom_teaching_score      double,
  homework_correction_quality   varchar(255),
  personalized_guidance         varchar(255),
  psychological_guidance        varchar(255),
  student_comprehensive_assessment varchar(255),
  growth_records_maintenance    varchar(255),
  learning_difficulty_support   varchar(255),
  career_planning_guidance      varchar(255),
  five_education_guidance       varchar(255),
  learning_behavior_habits_cultivation varchar(255),
  student_development_score     double,
  parent_meeting_participation  varchar(255),
  home_visit_records            varchar(255),
  parenting_guidance            varchar(255),
  parent_school_training        varchar(255),
  parent_contact_score          double,
  total_teaching_routine_score  double,
  constraint pk_teaching_routine_assessment primary key (id)
);

create table tk_v1_user (
  id                            bigint auto_increment not null,
  user_name                     varchar(255),
  password                      varchar(255),
  type_name                     varchar(255),
  status                        integer not null,
  role_id                       bigint,
  constraint pk_tk_v1_user primary key (id)
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


-- !Downs

-- drop all
drop table if exists cp_system_action;

drop table if exists v1_admin_config;

drop table if exists cp_member;

drop table if exists v1_agreement;

drop table if exists tk_assessment_detail;

drop table if exists tk_assessment_element_config;

drop table if exists v1_balance_log;

drop table if exists v1_card_coupon_config;

drop table if exists v1_contact_detail;

drop table if exists tk_v3_content;

drop table if exists v1_coupon_config;

drop table if exists critical_work_assessment;

drop table if exists v1_dict;

drop table if exists tk_v3_element;

drop table if exists tk_v1_teacher_content;

drop table if exists tk_v1_evaluation_element;

drop table if exists tk_v1_evaluation_indicator;

drop table if exists v1_everyday_sign_in;

drop table if exists cp_group;

drop table if exists cp_group_action;

drop table if exists cp_group_menu;

drop table if exists cp_group_user;

drop table if exists tk_v3_indicator;

drop table if exists tk_v3_kpi;

drop table if exists tk_v1_kpi;

drop table if exists v1_member;

drop table if exists v1_member_balance;

drop table if exists v1_member_card_coupon;

drop table if exists v1_member_coupon;

drop table if exists v1_member_level;

drop table if exists v1_member_score_config;

drop table if exists cp_menu;

drop table if exists moral_ethics_evaluation;

drop table if exists v1_msg;

drop table if exists v1_operation_log;

drop table if exists v1_order;

drop table if exists v1_order_detail;

drop table if exists v1_order_receive_detail;

drop table if exists v1_org;

drop table if exists v1_system_config;

drop table if exists v1_system_config_template;

drop table if exists v1_pay_method;

drop table if exists personal_development_assessment;

drop table if exists tk_v1_role;

drop table if exists v1_shop;

drop table if exists v1_shop_admin;

drop table if exists v1_shop_printer;

drop table if exists v1_shop_category;

drop table if exists v1_shop_tag;

drop table if exists v1_show_case;

drop table if exists v1_sms_log;

drop table if exists v1_sms_template;

drop table if exists v1_system_carousel;

drop table if exists v1_system_link;

drop table if exists tk_v3_teacher_content_score;

drop table if exists tk_v3_teacher_element_score;

drop table if exists tk_v3_teacher_kpi_score;

drop table if exists tk_teacher_performance_assessment;

drop table if exists tk_v1_teacher_performance_assessment;

drop table if exists teaching_achievement_assessment;

drop table if exists teaching_routine_assessment;

drop table if exists tk_v1_user;

drop table if exists v1_user_dict;

drop table if exists v1_voucher_log;

