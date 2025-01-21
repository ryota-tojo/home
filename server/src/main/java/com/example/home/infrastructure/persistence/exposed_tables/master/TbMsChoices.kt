package com.example.home.infrastructure.persistence.exposed_tables.master

import org.jetbrains.exposed.sql.Table

object TbMsChoices : Table("ms_choices") {
    val id = integer("id").autoIncrement()
    val itemType = varchar("item_type", length = 256)
    val itemNo = integer("item_no")
    val itemName = varchar("item_name", length = 256)
    override val primaryKey = PrimaryKey(id)
}
