package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table

object TbTsComment : Table("ts_comment") {
    val id = integer("id").autoIncrement()
    val groupsId = varchar("groups_id", 64)
    val yyyy = integer("yyyy")
    val mm = integer("mm")
    val content = varchar("content", 65535)
    override val primaryKey = PrimaryKey(id)
}
