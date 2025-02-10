package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table

object TbTsBudgets : Table("ts_budgets") {
    val id = integer("id").autoIncrement()
    val groupsId = varchar("groups_id", 64)
    val bgYyyy = integer("bg_yyyy")
    val bgMm = integer("bg_mm")
    val bgCategoryNo = integer("bg_category_no")
    val bgAmount = integer("bg_amount")
    val fixedFlg = integer("fixed_flg")
    override val primaryKey = PrimaryKey(id)
}
