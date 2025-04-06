package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object TbTsCommunication : Table("ts_communication") {
    val communicationId = integer("id").autoIncrement()
    val groupsId = varchar("groups_id", 64)
    val comGiftDate = date("gift_date")
    val comGiftFrom = varchar("gift_from", 64)
    val comGiftTo = varchar("gift_to", 64)
    val comGiftWhat = varchar("gift_item", 64)
    val comGiftMoney = integer("gift_amount")
    val comGiftReason = varchar("gift_remarks", 65535)
    val comReturnDate = date("gift_rtn_date")
    val comReturnWhat = varchar("gift_rtn_item", 64)
    val comReturnMoney = integer("gift_rtn_amount")
    val comReturnRemarks = varchar("gift_rtn_remarks", 65535)
    val returnFlg = integer("return_flg")
    override val primaryKey = PrimaryKey(communicationId)
}

