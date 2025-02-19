package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table

object TbTsMembers : Table("ts_members") {
    val id = integer("id").autoIncrement()
    val groupsId = varchar("groups_id", 64)
    val memberNo = integer("member_no")
    val memberName = varchar("member_name", 64)
    override val primaryKey = PrimaryKey(id)
}
