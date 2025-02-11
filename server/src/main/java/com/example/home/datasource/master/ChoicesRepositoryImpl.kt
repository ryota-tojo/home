package com.example.home.datasource.master

import com.example.home.domain.entity.master.MasterChoices
import com.example.home.domain.repository.master.ChoicesRepository
import com.example.home.domain.value_object.master.ChoicesItemNamePC
import com.example.home.domain.value_object.master.ChoicesItemNameSP
import com.example.home.domain.value_object.master.ChoicesItemNo
import com.example.home.domain.value_object.master.ChoicesItemType
import com.example.home.infrastructure.persistence.exposed_tables.master.TbMsChoices
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ChoicesRepositoryImpl : ChoicesRepository {
    override fun refer(): List<MasterChoices> {
        return transaction {
            TbMsChoices.selectAll().map {
                MasterChoices(
                    ChoicesItemType(it[TbMsChoices.itemType]),
                    ChoicesItemNo(it[TbMsChoices.itemNo]),
                    ChoicesItemNamePC(it[TbMsChoices.itemNamePC]),
                    ChoicesItemNameSP(it[TbMsChoices.itemNameSP])
                )
            }
        }
    }

    override fun getItemName(itemType: ChoicesItemType, itemNo: ChoicesItemNo): MasterChoices? {
        return transaction {
            TbMsChoices.select {
                Op.build {
                    var condition: Op<Boolean> = Op.TRUE
                    itemType.let { condition = TbMsChoices.itemType eq it.value }
                    itemNo.let { condition = TbMsChoices.itemNo eq it.value }
                    condition
                }
            }.map {
                MasterChoices(
                    ChoicesItemType(it[TbMsChoices.itemType]),
                    ChoicesItemNo(it[TbMsChoices.itemNo]),
                    ChoicesItemNamePC(it[TbMsChoices.itemNamePC]),
                    ChoicesItemNameSP(it[TbMsChoices.itemNameSP])
                )
            }.firstOrNull()
        }
    }
}