package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object TbTsGroupInfo : Table("ts_groupinfo") {
    val groupId = integer("group_id").autoIncrement()
    val groupsId = varchar("groups_id", 64).uniqueIndex()
    val userId = integer("user_id").references(TbTsUserInfo.userId)
    val leaderFlg = integer("leader_flg")
    val createDate = datetime("create_date")
    val updateDate = datetime("update_date")
    override val primaryKey = PrimaryKey(groupId)
}
