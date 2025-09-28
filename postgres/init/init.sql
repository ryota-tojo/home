-- 設定マスタ
CREATE TABLE ms_setting (
    id SERIAL NOT NULL,
    setting_key VARCHAR(512),
    setting_value VARCHAR(256), 
    setting_remarks VARCHAR(1024),
    PRIMARY KEY (id)
);

-- 選択肢マスタ
CREATE TABLE ms_choices (
    id SERIAL NOT NULL,
    item_type VARCHAR(256), 
    item_no INTEGER, 
    item_name_pc VARCHAR(256), 
    item_name_sp VARCHAR(256), 
    PRIMARY KEY (id)
);

-- 画面マスタ
CREATE TABLE ms_screen (
    screen_id VARCHAR(256), 
    screen_name VARCHAR(256), 
    remarks VARCHAR(1024)
);

-- お知らせ
CREATE TABLE ts_notice (
    id SERIAL NOT NULL,
    title VARCHAR(1024), 
    content VARCHAR(65535), 
    create_date TIMESTAMP,
    update_date TIMESTAMP,
    PRIMARY KEY (id)
);

-- ユーザー情報
CREATE TABLE ts_userinfo (
    user_id SERIAL NOT NULL,
    user_name VARCHAR(64) UNIQUE, 
    password VARCHAR(64), 
    permission INTEGER, 
    approval_flg INTEGER,
    delete_flg INTEGER, 
    create_date TIMESTAMP,
    update_date TIMESTAMP,
    approval_date TIMESTAMP,
    delete_date TIMESTAMP,
    PRIMARY KEY (user_id)
);

-- ユーザー設定
CREATE TABLE ts_user_setting (
    id SERIAL NOT NULL,
    user_id INTEGER,
    setting_key VARCHAR(512),
    setting_value VARCHAR(256), 
    PRIMARY KEY (id)
);

-- 所属グループ一覧
CREATE TABLE ts_grouplist (
    group_id SERIAL NOT NULL,
    groups_id VARCHAR(64) UNIQUE, 
    group_name VARCHAR(64),
    group_password VARCHAR(64)
);

-- 所属グループ情報
CREATE TABLE ts_groupinfo (
    id SERIAL NOT NULL,
    groups_id VARCHAR(64), 
    user_id INTEGER, 
    leader_flg INTEGER,
    approval_flg INTEGER,
    create_date TIMESTAMP,
    update_date TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES ts_userinfo (user_id)
);

-- 所属グループ設定
CREATE TABLE ts_group_setting (
    id SERIAL NOT NULL,
    groups_id VARCHAR(64),
    setting_key VARCHAR(512),
    setting_value VARCHAR(256), 
    PRIMARY KEY (id)
);

-- カテゴリー
CREATE TABLE ts_categorys (
    id SERIAL NOT NULL,
    groups_id VARCHAR(64), 
    category_no INTEGER,
    category_name VARCHAR(64),
    deleted_flag INTEGER,
    PRIMARY KEY (id)
);

-- メンバー
CREATE TABLE ts_members (
    id SERIAL NOT NULL,
    groups_id VARCHAR(64), 
    member_no INTEGER,
    member_name VARCHAR(64),
    deleted_flag INTEGER,
    PRIMARY KEY (id)
);

-- 購入データ入力テンプレート
CREATE TABLE ts_tmp_shopping_input (
    id SERIAL NOT NULL,
    groups_id VARCHAR(64),
    template_no INTEGER, 
    template_id VARCHAR(64),
    tmpi_name VARCHAR(64), 
    tmpi_member_id INTEGER,
    tmpi_category_id INTEGER, 
    tmpi_type INTEGER,
    tmpi_payment INTEGER,
    tmpi_settlement INTEGER,
    tmpi_amount INTEGER,
    tmpi_remarks VARCHAR(1024),
    tmpi_use_flg INTEGER,
    deleted_flag INTEGER,
    PRIMARY KEY (id)
);

-- 購入データ検索テンプレート
CREATE TABLE ts_tmp_shopping_search (
    id SERIAL NOT NULL,
    groups_id VARCHAR(64), 
    template_no INTEGER, 
    template_id VARCHAR(64),
    tmps_name VARCHAR(64), 
    tmps_member_id INTEGER,
    tmps_category_id INTEGER, 
    tmps_type INTEGER,
    tmps_payment INTEGER,
    tmps_settlement INTEGER,
    tmps_min_amount INTEGER,
    tmps_max_amount INTEGER,
    tmps_remarks VARCHAR(1024),
    tmps_use_flg INTEGER,
    deleted_flag INTEGER,
    PRIMARY KEY (id)
);

-- 購入データ登録テンプレート
CREATE TABLE ts_tmp_shopping_entry (
    id SERIAL NOT NULL,
    groups_id VARCHAR(64), 
    template_no INTEGER, 
    template_id VARCHAR(64),
    tmpe_name VARCHAR(64), 
    tmpe_member_id INTEGER,
    tmpe_category_id INTEGER, 
    tmpe_type INTEGER,
    tmpe_payment INTEGER,
    tmpe_settlement INTEGER,
    tmpe_amount INTEGER,
    tmpe_remarks VARCHAR(1024),
    tmpe_use_flg INTEGER,
    deleted_flag INTEGER,
    PRIMARY KEY (id)
);

-- 予算
CREATE TABLE ts_budgets (
    id SERIAL NOT NULL,
    groups_id VARCHAR(64), 
    bg_yyyy INTEGER,
    bg_mm INTEGER,
    bg_category_id INTEGER, 
    bg_amount INTEGER,
    fixed_flg INTEGER,
    PRIMARY KEY (id)
);

-- 購入データ
CREATE TABLE ts_shopping (
    id SERIAL NOT NULL,
    groups_id VARCHAR(64), 
    user_id INTEGER,
    shopping_date DATE,
    member_id INTEGER,
    category_id INTEGER, 
    type INTEGER,
    payment INTEGER,
    settlement INTEGER,
    amount INTEGER,
    remarks VARCHAR(1024),
    fixed_flg INTEGER,
    PRIMARY KEY (id)
);

-- 確定データ
CREATE TABLE ts_fixed (
    id SERIAL NOT NULL,
    groups_id VARCHAR(64), 
    yyyy INTEGER,
    mm INTEGER,
    PRIMARY KEY (id)
);

-- コメント
CREATE TABLE ts_comment (
    id SERIAL NOT NULL,
    groups_id VARCHAR(64), 
    yyyy INTEGER,
    mm INTEGER,
    content VARCHAR(65535),
    fixed_flg INTEGER,
    PRIMARY KEY (id)
);

-- お付き合い帳
CREATE TABLE ts_communication (
    id SERIAL NOT NULL,
    groups_id VARCHAR(64),
    gift_date DATE,
    gift_from VARCHAR(64),
    gift_to VARCHAR(64),
    gift_item VARCHAR(64),
    gift_amount INTEGER,
    gift_remarks VARCHAR(65535),
    gift_rtn_date DATE,
    gift_rtn_item VARCHAR(64),
    gift_rtn_amount INTEGER,
    gift_rtn_remarks VARCHAR(65535),
    return_flg INTEGER,
    PRIMARY KEY (id)
);

