package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table

object TbTsBudgets : Table("ts_budgets") {
    val id = integer("id").autoIncrement()
    val groupsId = varchar("groups_id", 64)
    val bgYyyy = integer("bg_yyyy")
    val bgMm = integer("bg_mm")
    val bgCategoryName = varchar("bg_category_name", 64)
    val bgValue = integer("bg_value")
    val fixedFlg = integer("fixed_flg")
    override val primaryKey = PrimaryKey(id)
}
