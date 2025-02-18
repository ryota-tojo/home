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
INSERT INTO ts_categorys (groups_id, category_no, category_name, deleted_flag) 
VALUES 
    ('group001', 1, 'カテゴリー１', 0),
    ('group001', 2, 'カテゴリー２', 0),
    ('group001', 3, '削除済みカテゴリー１', 1),
    ('group001', 4, '削除済みカテゴリー２', 1),
    ('group001', 5, '削除済みカテゴリー３', 1),
    ('group001', 6, '削除済みカテゴリー４', 1),
    ('group001', 7, '削除済みカテゴリー５', 1),
    ('group001', 8, '削除済みカテゴリー６', 1),
    ('group001', 9, '削除済みカテゴリー７', 1),
    ('group002', 1, 'カテゴリー１', 0),
    ('group002', 2, 'カテゴリー２', 0);

-- ts_members にデータを挿入
INSERT INTO ts_members (groups_id, member_no, member_name, deleted_flag) 
VALUES 
    ('group001', 1, 'メンバー１', 0),
    ('group001', 2, 'メンバー２', 0),
    ('group003', 1, 'メンバー１', 0),
    ('group003', 2, 'メンバー２', 0);

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

INSERT INTO ts_shopping (
    groups_id, user_id, shopping_date, member_id, category_id, type, 
    payment, settlement, amount, remarks, fixed_flg
) VALUES 
('group001', 1, '2024-02-17', 1, 1, 1, 1, 1, 5000, '買い物メモ1', 1),
('group001', 1, '2024-02-16', 1, 3, 1, 1, 1, 3000, '買い物メモ2', 1),
('group001', 1, '2024-02-15', 1, 4, 1, 1, 1, 7000, '買い物メモ3', 1),
('group001', 1, '2024-03-17', 1, 2, 1, 1, 1, 5000, '買い物メモ4', 1),
('group001', 1, '2024-03-16', 1, 5, 1, 1, 1, 3000, '買い物メモ5', 1),
('group001', 1, '2024-03-15', 1, 6, 1, 1, 1, 7000, '買い物メモ6', 1),
('group001', 1, '2025-02-17', 1, 7, 1, 1, 1, 5000, '買い物メモ7', 1),
('group001', 1, '2025-02-16', 1, 8, 1, 1, 1, 3000, '買い物メモ8', 1),
('group001', 1, '2025-02-15', 1, 9, 1, 1, 1, 7000, '買い物メモ9', 1);

INSERT INTO ts_fixed (groups_id, yyyy, mm) VALUES 
('group001', 2024, 1),
('group001', 2024, 2),
('group001', 2024, 3);
