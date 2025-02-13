package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object TbTsShopping : Table("ts_shopping") {
    val id = integer("id").autoIncrement()
    val groupsId = varchar("groups_id", 64)
    val userId = integer("user_id")
    val shoppingDate = date("shopping_date")
    val memberId = integer("member_id")
    val categoryId = integer("category_id")
    val type = integer("type")
    val payment = integer("payment")
    val settlement = integer("settlement")
    val amount = integer("amount")
    val remarks = varchar("remarks", 1024)
    val fixedFlg = integer("fixed_flg")
    override val primaryKey = PrimaryKey(id)
}
