<?php

namespace Domain\Models;
class UserRepository
{
    private $user_name;
    private $password;

    public function __construct($db_config, $user_name, $password)
    {
        $this->db_config = $db_config;
        $this->user_name = $user_name;
        $this->password = $password;
    }

    public function get_db_config()
    {
        return $this->db_config;
    }

    public function get_user_name()
    {
        return $this->user_name;
    }

    public function get_password()
    {
        return $this->password;
    }

    public function entry()
    {
        // ユーザー情報
        $result = $this->db_config->query("INSERT INTO ts_userinfo (user_name, password, permission, approval_flg, delete_flg, create_date, update_date, approval_date, delete_date) VALUES ('{$this->user_name}', '{$this->password}', 1, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL) RETURNING user_id;");
        foreach ($result as $row) {
            return $row['user_id'];
        }
        return 0;
    }
}
