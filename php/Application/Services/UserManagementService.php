<?php

namespace Application\Services;

use Domain\Models\UserRepository;

class UserManagementService
{

    public function __construct($db_config, $groups_id, $user_name, $password, $re_password)
    {
        $this->db_config = $db_config;
        $this->groups_id = $groups_id;
        $this->user_name = $user_name;
        $this->password = $password;
        $this->re_password = $re_password;
    }

    public function get_db_config()
    {
        return $this->db_config;
    }

    public function get_groups_id()
    {
        return $this->groups_id;
    }

    public function get_user_name()
    {
        return $this->user_name;
    }

    public function get_password()
    {
        return $this->password;
    }

    public function get_re_password()
    {
        return $this->re_password;
    }

    public function certification()
    {
        // 所属不可グループID
        foreach (INVALID_GROUPS_ID as $char) {
            if (strpos($this->groups_id, $char) !== false) {
                return "unavailable group id";
            }
        }

        // グループID存在チェック
        $results = $this->db_config->query("SELECT * FROM ts_grouplist WHERE groups_id='{$this->groups_id}';");
        if (!$results) {
            return "groupid not found";
        }

        // 使用不可名チェック
        foreach (INVALID_USER_NAME as $char) {
            if (strpos($this->user_name, $char) !== false) {
                return "unavailable user name";
            }
        }

        // ユーザー名重複チェック
        $results = $this->db_config->query("SELECT * FROM ts_userinfo WHERE user_name='{$this->user_name}';");
        if ($results) {
            return "user name is duplication";
        }

        // パスワード照合
        if ($this->password != $this->re_password) {
            return "password mismatch";
        }
        return "success";
    }

    public function creation()
    {

        // ユーザー情報
        $user_repository = new UserRepository($this->db_config, $this->user_name, $this->password);
        $user_repository->entry();
        // ユーザー設定
        // 所属グループ情報
        // カテゴリー
        // メンバー
        // コメント

    }

}
