package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object TbTsGroupInfo : Table("ts_groupinfo") {
    val groupInfoId = integer("id").autoIncrement()
    val groupsId = varchar("groups_id", 64)
    val userId = integer("user_id").references(TbTsUserInfo.userId)
    val leaderFlg = integer("leader_flg")
    val createDate = datetime("create_date")
    val updateDate = datetime("update_date")
    override val primaryKey = PrimaryKey(groupInfoId)
}
