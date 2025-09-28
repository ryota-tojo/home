package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table

object TbTsTmpShoppingSearch : Table("ts_tmp_shopping_search") {
    val id = integer("id").autoIncrement()
    val groupsId = varchar("groups_id", 64)
    val templateNo = integer("template_no")
    val templateId = varchar("template_id", 64)
    val tmpsName = varchar("tmps_name", 64)
    val tmpsMemberId = integer("tmps_member_id").nullable()
    val tmpsCategoryId = integer("tmps_category_id").nullable()
    val tmpsType = integer("tmps_type").nullable()
    val tmpsPayment = integer("tmps_payment").nullable()
    val tmpsSettlement = integer("tmps_settlement").nullable()
    val tmpsMinAmount = integer("tmps_min_amount").nullable()
    val tmpsMaxAmount = integer("tmps_max_amount").nullable()
    val tmpsRemarks = varchar("tmps_remarks", 1024).nullable()
    val tmpsUseFlg = integer("tmps_use_flg")
    val deletedFlg = integer("deleted_flag")
    override val primaryKey = PrimaryKey(id)
}