package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object TbTsShopping : Table("ts_shopping") {
    val id = integer("id").autoIncrement()
    val groupsId = varchar("groups_id", 64)
    val shoppingDate = date("shopping_date")
    val memberName = varchar("member_name", 64)
    val categoryName = varchar("category_name", 64)
    val type = integer("type")
    val payment = integer("payment")
    val settlement = integer("settlement")
    val amount = integer("amount")
    val remarks = varchar("remarks", 1024)
    val fixedFlg = integer("fixed_flg")
    override val primaryKey = PrimaryKey(id)
}
