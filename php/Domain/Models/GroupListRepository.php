<?php

namespace Domain\Models;
class GroupListRepository
{
    public function __construct($db_config, $groups_id, $groups_name)
    {
        $this->db_config = $db_config;
        $this->groups_id = $groups_id;
        $this->groups_name = $groups_name;
    }

    public function get_db_config()
    {
        return $this->db_config;
    }

    public function get_groups_id()
    {
        return $this->groups_id;
    }

    public function get_groups_name()
    {
        return $this->groups_name;
    }

    public function refer()
    {
        return $this->db_config->query("SELECT * FROM ts_grouplist WHERE groups_id = '{$this->groups_id}';");
    }

    public function entry()
    {
        return $this->db_config->query("INSERT INTO ts_grouplist (groups_id, groups_name)VALUES('{$this->groups_id}', '{$this->groups_name}') RETURNING group_id;");
    }

    public function delete()
    {
        $this->db_config->query("DELETE FROM ts_grouplist WHERE groups_id = '{$this->groups_id}';");
    }
}
