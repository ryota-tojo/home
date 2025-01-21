<?php

namespace Domain\Models;
class GroupSettingRepository
{

    public function __construct($db_config, $groups_id)
    {
        $this->db_config = $db_config;
        $this->groups_id = $groups_id;
    }

    public function get_db_config()
    {
        return $this->db_config;
    }

    public function get_groups_id()
    {
        return $this->groups_id;
    }

    public function refer()
    {
        return $this->db_config->query("SELECT * FROM ts_group_setting WHERE groups_id = '{$this->groups_id}';");
    }

    public function entry()
    {
        $current_year = date("Y");
        $this->db_config->query("INSERT INTO ts_group_setting (groups_id, setting_key, setting_value)VALUES('{$this->groups_id}', 'view_year', '{$current_year}') RETURNING id;");
    }
    public function delete()
    {
        $this->db_config->query("DELETE FROM ts_group_setting WHERE groups_id = '{$this->groups_id}';");
    }

}
