package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object TbTsUserInfo : Table("ts_userinfo") {
    val userId = integer("user_id").autoIncrement()
    val userName = varchar("user_name", 64).uniqueIndex()
    val password = varchar("password", 64)
    val permission = integer("permission")
    val approvalFlg = integer("approval_flg")
    val deleteFlg = integer("delete_flg")
    val createDate = datetime("create_date")
    val updateDate = datetime("update_date")
    val approvalDate = datetime("approval_date")
    val deleteDate = datetime("delete_date")
    override val primaryKey = PrimaryKey(userId)
}
