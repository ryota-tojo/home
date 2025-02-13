package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table

object TbTsUserSetting : Table("ts_user_setting") {
    val userSettingId = integer("id").autoIncrement()
    val userId = integer("user_id")
    val settingKey = varchar("setting_key", 512)
    val settingValue = varchar("setting_value", 256)
    override val primaryKey = PrimaryKey(userSettingId)
}
