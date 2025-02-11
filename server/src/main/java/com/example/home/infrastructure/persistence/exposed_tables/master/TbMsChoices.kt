package com.example.home.infrastructure.persistence.exposed_tables.master

import org.jetbrains.exposed.sql.Table

object TbMsChoices : Table("ms_choices") {
    val id = integer("id").autoIncrement()
    val itemType = varchar("item_type", length = 256)
    val itemNo = integer("item_no")
    val itemNamePC = varchar("item_name_pc", length = 256)
    val itemNameSP = varchar("item_name_sp", length = 256)
    override val primaryKey = PrimaryKey(id)
}
