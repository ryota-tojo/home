package com.example.home.infrastructure.persistence.exposed_tables.master

import org.jetbrains.exposed.sql.Table

object TbMsSetting : Table("ms_setting") {
    val id = integer("id").autoIncrement()
    val settingKey = varchar("setting_key", length = 512)
    val settingValue = varchar("setting_value", length = 256)
    override val primaryKey = PrimaryKey(id)
}
