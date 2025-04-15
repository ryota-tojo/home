package com.example.home.datasource.master

import com.example.home.domain.entity.master.MasterChoices
import com.example.home.domain.repository.master.ChoicesRepository
import com.example.home.domain.value_object.master.ChoicesItemNamePC
import com.example.home.domain.value_object.master.ChoicesItemNameSP
import com.example.home.domain.value_object.master.ChoicesItemNo
import com.example.home.domain.value_object.master.ChoicesItemType
import com.example.home.infrastructure.persistence.exposed_tables.master.TbMsChoices
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class ChoicesRepositoryImpl : ChoicesRepository {
    override fun refer(): List<MasterChoices> {
        return transaction {
            TbMsChoices.selectAll().map {
                MasterChoices(
                    it[TbMsChoices.id],
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
                    it[TbMsChoices.id],
                    ChoicesItemType(it[TbMsChoices.itemType]),
                    ChoicesItemNo(it[TbMsChoices.itemNo]),
                    ChoicesItemNamePC(it[TbMsChoices.itemNamePC]),
                    ChoicesItemNameSP(it[TbMsChoices.itemNameSP])
                )
            }.firstOrNull()
        }
    }

    override fun save(
        itemType: ChoicesItemType,
        itemNo: ChoicesItemNo,
        itemNamePC: ChoicesItemNamePC,
        itemNameSP: ChoicesItemNameSP
    ): MasterChoices {
        return transaction {
            TbMsChoices.insert {
                it[TbMsChoices.itemType] = itemType.value
                it[TbMsChoices.itemNo] = itemNo.value
                it[TbMsChoices.itemNamePC] = itemNamePC.value
                it[TbMsChoices.itemNameSP] = itemNameSP.value
            }
            val choices = TbMsChoices.select {
                (TbMsChoices.itemType eq itemType.value) and
                        (TbMsChoices.itemNo eq itemNo.value) and
                        (TbMsChoices.itemNamePC eq itemNamePC.value) and
                        (TbMsChoices.itemNameSP eq itemNameSP.value)
            }.singleOrNull()
            return@transaction choices?.let {
                MasterChoices(
                    it[TbMsChoices.id],
                    ChoicesItemType(it[TbMsChoices.itemType]),
                    ChoicesItemNo(it[TbMsChoices.itemNo]),
                    ChoicesItemNamePC(it[TbMsChoices.itemNamePC]),
                    ChoicesItemNameSP(it[TbMsChoices.itemNameSP])
                )
            } ?: throw IllegalStateException("Failed to save the Choices")
        }
    }

    override fun update(
        id: Int,
        itemType: ChoicesItemType,
        itemNo: ChoicesItemNo,
        itemNamePC: ChoicesItemNamePC,
        itemNameSP: ChoicesItemNameSP
    ): Int {
        return transaction {
            val updateRows = TbMsChoices.update({ TbMsChoices.id eq id }) {
                it[TbMsChoices.itemType] = itemType.value
                it[TbMsChoices.itemNo] = itemNo.value
                it[TbMsChoices.itemNamePC] = itemNamePC.value
                it[TbMsChoices.itemNameSP] = itemNameSP.value
            }
            return@transaction updateRows
        }
    }

    override fun delete(id: Int): Int {
        return transaction {
            val deleteRows = TbMsChoices.deleteWhere { TbMsChoices.id eq id }
            return@transaction deleteRows
        }
    }
}