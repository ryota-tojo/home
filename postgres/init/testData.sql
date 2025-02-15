-- group001: 正常データ
-- group002: メンバーが存在しないデータ
-- group003: カテゴリーが存在しないデータ


-- ts_grouplist にデータを挿入
INSERT INTO ts_grouplist (groups_id, group_name, group_password) 
VALUES 
    ('group001', 'グループ１', '1234567890'),
    ('group002', 'グループ２', '1234567890'),
    ('group003', 'グループ３', '1234567890');

-- ts_group_setting にデータを挿入
INSERT INTO ts_group_setting (groups_id, setting_key, setting_value) 
VALUES 
    ('group001', 'グループ設定１', '1001'),
    ('group001', 'グループ設定２', '1002'),
    ('group002', 'グループ設定１', '2001'),
    ('group002', 'グループ設定２', '2002'),
    ('group003', 'グループ設定１', '3001'),
    ('group003', 'グループ設定２', '3002');

-- ts_categorys にデータを挿入
INSERT INTO ts_categorys (groups_id, category_no, category_name) 
VALUES 
    ('group001', 1, 'カテゴリー１'),
    ('group001', 2, 'カテゴリー２'),
    ('group002', 1, 'カテゴリー１'),
    ('group002', 2, 'カテゴリー２');

-- ts_members にデータを挿入
INSERT INTO ts_members (groups_id, member_no, member_name) 
VALUES 
    ('group001', 1, 'メンバー１'),
    ('group001', 2, 'メンバー２'),
    ('group003', 1, 'メンバー１'),
    ('group003', 2, 'メンバー２');

-- ts_comment にデータを挿入
INSERT INTO ts_comment (groups_id, yyyy, mm, content, fixed_flg) 
VALUES 
    ('group001', 2999, 12, 'コメントを入力してください。', 0),
    ('group002', 2999, 12, 'コメントを入力してください。', 0),
    ('group003', 2999, 12, 'コメントを入力してください。', 0);

-- ts_userinfo にデータを挿入
INSERT INTO ts_userinfo (user_name, password, permission, approval_flg, delete_flg, create_date, update_date, approval_date, delete_date) 
VALUES 
    ('user_001', '1234567890', 1, 1, 0, NOW(), NOW(), NOW(), NULL),
    ('user_002', '1234567890', 2, 1, 0, NOW(), NOW(), NOW(), NULL),
    ('user_003', '1234567890', 1, 0, 0, NOW(), NOW(), NULL, NULL),
    ('user_004', '1234567890', 3, 1, 0, NOW(), NOW(), NOW(), NULL),
    ('user_005', '1234567890', 4, 1, 0, NOW(), NOW(), NOW(), NULL);

-- ts_user_setting にデータを挿入
INSERT INTO ts_user_setting (user_id, setting_key, setting_value) 
VALUES 
    (1, 'ユーザー設定１', '1001'),
    (1, 'ユーザー設定２', '1002'),
    (2, 'ユーザー設定１', '2001'),
    (2, 'ユーザー設定２', '2002'),
    (3, 'ユーザー設定１', '3001'),
    (3, 'ユーザー設定２', '3002'),
    (4, 'ユーザー設定１', '4001'),
    (4, 'ユーザー設定２', '4002'),
    (5, 'ユーザー設定１', '5001'),
    (5, 'ユーザー設定２', '5002');

-- ts_groupinfo にデータを挿入
INSERT INTO ts_groupinfo (groups_id, user_id, leader_flg, create_date, update_date) 
VALUES 
    ('group001', 1, 1, NOW(), NOW()),
    ('group001', 2, 0, NOW(), NOW()),
    ('group002', 3, 1, NOW(), NOW()),
    ('group002', 4, 0, NOW(), NOW()),
    ('group003', 5, 1, NOW(), NOW());
