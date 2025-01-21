package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object TbTsNotice : Table("ts_notice") {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 1024)
    val content = varchar("content", 65535)
    val createDate = datetime("create_date")
    val updateDate = datetime("update_date")
    override val primaryKey = PrimaryKey(id)
}
