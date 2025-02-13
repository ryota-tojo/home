-- 設定マスタ
create table ms_setting(
    id SERIAL NOT NULL,
    setting_key varchar(512),
    setting_value varchar(256), 
    PRIMARY KEY (id)
);

-- 選択肢マスタ
create table ms_choices(
    id SERIAL NOT NULL,
    item_type varchar(256), 
    item_no integer, 
    item_name_pc varchar(256), 
    item_name_sp varchar(256), 
    PRIMARY KEY (id)
);

-- お知らせ
create table ts_notice(
    id SERIAL NOT NULL,
    title varchar(1024), 
    content varchar(65535), 
    create_date timestamp,
    update_date timestamp,
    PRIMARY KEY (id)
);

-- ユーザー情報
create table ts_userinfo(
    user_id SERIAL NOT NULL,
    user_name varchar(64) UNIQUE, 
    password varchar(64), 
    permission integer, 
    approval_flg integer,
    delete_flg integer, 
    create_date timestamp,
    update_date timestamp,
    approval_date timestamp,
    delete_date timestamp,
    PRIMARY KEY (user_id)
);

-- ユーザー設定
create table ts_user_setting(
    id SERIAL NOT NULL,
    user_id integer,
    setting_key varchar(512),
    setting_value varchar(256), 
    PRIMARY KEY (id)
);

-- 所属グループ一覧
create table ts_grouplist(
    group_id SERIAL NOT NULL,
    groups_id varchar(64) UNIQUE, 
    group_name varchar(64),
    group_password varchar(64)
);

-- 所属グループ情報
create table ts_groupinfo(
    id SERIAL NOT NULL,
    groups_id varchar(64), 
    user_id integer, 
    leader_flg integer,
    create_date timestamp,
    update_date timestamp,
    PRIMARY KEY (group_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES ts_userinfo (user_id)
);

-- 所属グループ設定
create table ts_group_setting(
    id SERIAL NOT NULL,
    groups_id varchar(64),
    setting_key varchar(512),
    setting_value varchar(256), 
    PRIMARY KEY (id)
);

-- カテゴリー
create table ts_categorys(
    id SERIAL NOT NULL,
    groups_id varchar(64), 
    category_no integer,
    category_name varchar(64),
    PRIMARY KEY (id)
);

-- メンバー
create table ts_members(
    id SERIAL NOT NULL,
    groups_id varchar(64), 
    member_no integer,
    member_name varchar(64),
    PRIMARY KEY (id)
);

-- 購入データ入力テンプレート
create table ts_tmp_shopping_input(
    id SERIAL NOT NULL,
    groups_id varchar(64), 
    template_id varchar(64),
    tmpi_name varchar(64), 
    tmpi_member_id integer,
    tmpi_category_id integer, 
    tmpi_type integer,
    tmpi_payment integer,
    tmpi_settlement integer,
    tmpi_amount integer,
    tmpi_remarks varchar(1024),
    tmpi_use_flg integer,
    PRIMARY KEY (id)
);

-- 購入データ検索テンプレート
create table ts_tmp_shopping_search(
    id SERIAL NOT NULL,
    groups_id varchar(64), 
    template_id varchar(64),
    tmps_name varchar(64), 
    tmps_member_id integer,
    tmps_category_id integer, 
    tmps_type integer,
    tmps_payment integer,
    tmps_settlement integer,
    tmps_amount integer,
    tmps_remarks varchar(1024),
    tmps_use_flg integer,
    PRIMARY KEY (id)
);

-- 購入データ登録テンプレート
create table ts_tmp_shopping_entry(
    id SERIAL NOT NULL,
    groups_id varchar(64), 
    template_id varchar(64),
    tmpe_name varchar(64), 
    tmpe_member_id integer,
    tmpe_category_id integer, 
    tmpe_type integer,
    tmpe_payment integer,
    tmpe_settlement integer,
    tmpe_amount integer,
    tmpe_remarks varchar(1024),
    tmpe_use_flg integer,
    PRIMARY KEY (id)
);

-- 予算
create table ts_budgets(
    id SERIAL NOT NULL,
    groups_id varchar(64), 
    bg_yyyy integer,
    bg_mm integer,
    bg_category_id integer, 
    bg_amount integer,
    fixed_flg integer,
    PRIMARY KEY (id)
);

-- 購入データ
create table ts_shopping(
    id SERIAL NOT NULL,
    groups_id varchar(64), 
    user_id integer,
    shopping_date date,
    member_id integer,
    category_id integer, 
    type integer,
    payment integer,
    settlement integer,
    amount integer,
    remarks varchar(1024),
    fixed_flg integer,
    PRIMARY KEY (id)
);

-- 確定データ
create table ts_fixed(
    id SERIAL NOT NULL,
    groups_id varchar(64), 
    yyyy integer,
    mm integer,
    PRIMARY KEY (id)
);

-- コメント
create table ts_comment(
    id SERIAL NOT NULL,
    groups_id varchar(64), 
    yyyy integer,
    mm integer,
    content varchar(65535),
    fixed_flg integer,
    PRIMARY KEY (id)
);

-- お付き合い帳
create table ts_communication(
    id SERIAL NOT NULL,
    groups_id varchar(64),
    gift_date date,
    gift_from varchar(64),
    gift_to varchar(64),
    gift_item varchar(64),
    gift_amount integer,
    gift_remarks varchar(65535),
    gift_rtn_date date,
    gift_rtn_item varchar(64),
    gift_rtn_amount varchar(64),
    gift_rtn_remarks varchar(65535),
    return_flg integer,
    PRIMARY KEY (id)
);



INSERT INTO ms_setting (setting_key, setting_value)
VALUES
    ('account_lockout_count', '5'),
    ('maintenance', '0'),
    ('slack_report_send_flg', '0'),
    ('slack_report_webhookurl', ''),
    ('slack_send_test', '0'),
    ('layout', '0'),
    ('admin_userdata_view', '10'),
    ('admin_notice_view', '5'),
    ('admin_notice_initial_title', 'お知らせ'),
    ('admin_notice_initial_content', 'XXXXX'),
    ('user_input_history_view', '10'),
    ('user_management_view', '20'),
    ('user_analysis_graph_size_pc_width', '750'),
    ('user_analysis_graph_size_pc_height', '200'),
    ('user_analysis_graph_size_sp_width', '320'),
    ('user_analysis_graph_size_sp_height', '200'),
    ('user_analysis_graph_size_tb_width', '680'),
    ('user_analysis_graph_size_tb_height', '200'),
    ('user_communication_histry_view', '10'),
    ('user_communication_list_view', '20'),
    ('user_communication_view_conditions', '0');

INSERT INTO ms_choices (item_type, item_no, item_name_pc,item_name_sp)
VALUES
    ('type', 0, '収入', '収入'),
    ('type', 1, '支出', '支出'),
    ('payment', 0, '現金', '現金'),
    ('payment', 1, 'カード', 'カード'),
    ('payment', 2, '引落し', '引落し'),
    ('payment', 3, '振込み', '振込み'),
    ('settlement', 0, '未精算','未'),
    ('settlement', 1, '精算済','済'),

INSERT INTO ts_userinfo (
    user_name, 
    password, 
    permission, 
    approval_flg, 
    delete_flg, 
    create_date, 
    update_date, 
    approval_date, 
    delete_date
)
VALUES
    ('admin', 'adminadmin', 2, 1, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO ts_grouplist (
    groups_id, 
    group_name,
    group_password
)
VALUES
    ('maintenance', '管理者','adminadmin');

INSERT INTO ts_groupinfo (
    groups_id, 
    user_id, 
    leader_flg, 
    create_date, 
    update_date
)
VALUES
    ('maintenance',  
     (SELECT user_id FROM ts_userinfo WHERE user_name = 'admin' and password = 'adminadmin'), 
     1, 
     CURRENT_TIMESTAMP, 
     CURRENT_TIMESTAMP);

INSERT INTO ts_notice (
    title, 
    content, 
    create_date, 
    update_date
)
VALUES
    ('サービス開始', 
     '本サービスを開始しました。',
     CURRENT_TIMESTAMP, 
     CURRENT_TIMESTAMP);