package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table

object TbTsGroupSetting : Table("ts_group_setting") {
    val id = integer("id").autoIncrement()
    val groupsId = varchar("groups_id", 64)
    val settingKey = varchar("setting_key", 512)
    val settingValue = varchar("setting_value", 256)
    override val primaryKey = PrimaryKey(id)
}
