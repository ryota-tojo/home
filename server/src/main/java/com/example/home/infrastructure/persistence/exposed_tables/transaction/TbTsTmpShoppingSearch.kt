package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table

object TbTsTmpShoppingSearch : Table("ts_tmp_shopping_search") {
    val id = integer("id").autoIncrement()
    val groupsId = varchar("groups_id", 64)
    val tmpsName = varchar("tmps_name", 64)
    val tmpsMemberName = varchar("tmps_member_name", 64)
    val tmpsCategoryName = varchar("tmps_category_name", 64)
    val tmpsType = integer("tmps_type")
    val tmpsPayment = integer("tmps_payment")
    val tmpsSettlement = integer("tmps_settlement")
    val tmpsAmount = integer("tmps_amount")
    val tmpsRemarks = varchar("tmps_remarks", 1024)
    val tmpsUseFlg = integer("tmps_use_flg")
    override val primaryKey = PrimaryKey(id)
}
