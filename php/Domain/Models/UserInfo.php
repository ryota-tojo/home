<?php

namespace Domain\Models;
class UserInfo
{
    private $db_config;
    private $user_id;
    private $user_name;
    private $password;
    private $permission;
    private $approval_flg;
    private $delete_flg;
    private $create_date;
    private $update_date;
    private $approval_date;
    private $delete_date;

    public function __construct($db_config, $user_id)
    {
        $this->db_config = $db_config;
        $this->user_id = $user_id;

        $results = $this->db_config->query("SELECT * FROM ts_userinfo WHERE user_id='{$this->user_id}';");
        if (!$results) {
            $this->user_name = null;
            $this->password = null;
            $this->permission = null;
            $this->approval_flg = null;
            $this->delete_flg = null;
            $this->create_date = null;
            $this->update_date = null;
            $this->approval_date = null;
            $this->delete_date = null;
        } else {
            foreach ($results as $row) {
                $this->user_id = $row['user_id'];
                $this->password = $row['password'];
                $this->permission = $row['permission'];
                $this->approval_flg = $row['approval_flg'];
                $this->delete_flg = $row['delete_flg'];
                $this->create_date = $row['create_date'];
                $this->update_date = $row['update_date'];
                $this->approval_date = $row['approval_date'];
                $this->delete_date = $row['delete_date'];
            }
        }
    }

    // ゲッター
    public function getUserId()
    {
        return $this->user_id;
    }

    public function getUserName()
    {
        return $this->user_name;
    }

    public function getPassword()
    {
        return $this->password;
    }

    public function getPermission()
    {
        return $this->permission;
    }

    public function getApprovalFlg()
    {
        return $this->approval_flg;
    }

    public function getDeleteFlg()
    {
        return $this->delete_flg;
    }

    public function getCreateDate()
    {
        return $this->create_date;
    }

    public function getUpdateDate()
    {
        return $this->update_date;
    }

    public function getApprovalDate()
    {
        return $this->approval_date;
    }

    public function getDeleteDate()
    {
        return $this->delete_date;
    }
}
