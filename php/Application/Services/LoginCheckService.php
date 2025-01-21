<?php

namespace Application\Services;
class LoginCheckService
{

    public function __construct($db_config, $groups_id, $user_name, $password)
    {
        $this->db_config = $db_config;
        $this->groups_id = $groups_id;
        $this->user_name = $user_name;
        $this->password = $password;
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

    public function certification()
    {
        $user_id = 0;
        $approval_flg = 0;

        // ユーザー情報の照合
        $results = $this->db_config->query("SELECT * FROM ts_userinfo WHERE user_name='{$this->user_name}' and password='{$this->password}' and delete_flg=0;");
        if (!$results) {
            return -1;
        } else {
            foreach ($results as $row) {
                $user_id = $row['user_id'];
                $approval_flg = $row['approval_flg'];
            }
        }
        // グループ所属
        $results = $this->db_config->query("SELECT * FROM ts_groupinfo WHERE groups_id='{$this->groups_id}' and user_id='{$user_id}';");
        if (!$results) {
            return -1;
        }
        // 承認状況
        if ($approval_flg != 1) {
            return 0;
        }
        return $user_id;
    }
}
