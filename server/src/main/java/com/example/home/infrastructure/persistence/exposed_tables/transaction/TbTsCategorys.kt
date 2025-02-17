package com.example.home.infrastructure.persistence.exposed_tables.transaction

import org.jetbrains.exposed.sql.Table

// カテゴリー
object TbTsCategorys : Table("ts_categorys") {
    val categoryId = integer("id").autoIncrement()
    val groupsId = varchar("groups_id", 64)
    val categoryNo = integer("category_no")
    val categoryName = varchar("category_name", 64)
    val deletedFlg = integer("deleted_flag")
    override val primaryKey = PrimaryKey(categoryId)
}
