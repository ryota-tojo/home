package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table

object TbTsGroupList : Table("ts_grouplist") {
    val groupListId = integer("group_id").autoIncrement()
    val groupsId = varchar("groups_id", 64).uniqueIndex()
    val groupName = varchar("group_name", 64)
    val groupPassword = varchar("group_password", 64)
    override val primaryKey = PrimaryKey(groupListId)
}
