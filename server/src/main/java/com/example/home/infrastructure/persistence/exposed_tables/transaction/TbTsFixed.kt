package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table

object TbTsFixed : Table("ts_fixed") {
    val fixedId = integer("id").autoIncrement()
    val groupsId = varchar("groups_id", 64)
    val yyyy = integer("yyyy")
    val mm = integer("mm")
    override val primaryKey = PrimaryKey(fixedId)
}
