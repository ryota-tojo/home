package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table

object TbTsGroupList : Table("ts_grouplist") {
    val groupId = integer("group_id").autoIncrement()
    val groupsId = varchar("groups_id", 64).uniqueIndex()
    val groupsName = varchar("groups_name", 64)
    override val primaryKey = PrimaryKey(groupId)
}
