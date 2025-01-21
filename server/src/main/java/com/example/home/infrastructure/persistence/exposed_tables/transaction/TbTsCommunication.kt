package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object TbTsCommunication : Table("ts_communication") {
    val id = integer("id").autoIncrement()
    val groupsId = varchar("groups_id", 64)
    val comGiftDate = date("com_gift_date")
    val comGiftFrom = varchar("com_gift_from", 64)
    val comGiftTo = varchar("com_gift_to", 64)
    val comGiftWhat = varchar("com_gift_what", 64)
    val comGiftMoney = integer("com_gift_money")
    val comGiftReason = varchar("com_gift_reason", 65535)
    val comReturnDate = date("com_return_date")
    val comReturnWhat = varchar("com_return_what", 64)
    val comReturnMoney = varchar("com_return_money", 64)
    val comReturnRemarks = varchar("com_return_remarks", 65535)
    val returnFlg = integer("return_flg")
    override val primaryKey = PrimaryKey(id)
}

