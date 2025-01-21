package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table

object TbTsTmpShoppingInput : Table("ts_tmp_shopping_input") {
    val id = integer("id").autoIncrement()
    val groupsId = varchar("groups_id", 64)
    val tmpiName = varchar("tmpi_name", 64)
    val tmpiMemberName = varchar("tmpi_member_name", 64)
    val tmpiCategoryName = varchar("tmpi_category_name", 64)
    val tmpiType = integer("tmpi_type")
    val tmpiPayment = integer("tmpi_payment")
    val tmpiSettlement = integer("tmpi_settlement")
    val tmpiAmount = integer("tmpi_amount")
    val tmpiRemarks = varchar("tmpi_remarks", 1024)
    val tmpiUseFlg = integer("tmpi_use_flg")
    override val primaryKey = PrimaryKey(id)
}
