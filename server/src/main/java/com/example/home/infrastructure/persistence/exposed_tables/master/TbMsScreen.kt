package com.example.home.infrastructure.persistence.exposed_tables.master

import org.jetbrains.exposed.sql.Table

object TbMsScreen : Table("ms_screen") {
    val screenId = varchar("screen_id", length = 256)
    val screenName = varchar("screen_name", length = 256)
    val remarks = varchar("remarks", length = 1024)
}
