package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table

// 購入データ登録テンプレート
object TbTsTmpShoppingEntry : Table("ts_tmp_shopping_entry") {
    val id = integer("id").autoIncrement()
    val groupsId = varchar("groups_id", 64)
    val tmpeName = varchar("tmpe_name", 64)
    val tmpeMemberName = integer("tmpe_member_no")
    val tmpeCategoryName = integer("tmpe_category_no")
    val tmpeType = integer("tmpe_type")
    val tmpePayment = integer("tmpe_payment")
    val tmpeSettlement = integer("tmpe_settlement")
    val tmpeAmount = integer("tmpe_amount")
    val tmpeRemarks = varchar("tmpe_remarks", 1024)
    val tmpeUseFlg = integer("tmpe_use_flg")
    override val primaryKey = PrimaryKey(id)
}
