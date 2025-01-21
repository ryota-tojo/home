<?php

namespace Domain\Models;
class UserSettingRepository
{

    public function __construct($db_config, $user_id)
    {
        $this->db_config = $db_config;
        $this->user_id = $user_id;
    }

    public function get_db_config()
    {
        return $this->db_config;
    }

    public function get_user_id()
    {
        return $this->user_id;
    }

}
