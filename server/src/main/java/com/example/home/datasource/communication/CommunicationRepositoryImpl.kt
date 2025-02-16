package com.example.home.datasource.communication

import com.example.home.domain.entity.communication.Communication
import com.example.home.domain.repository.communication.CommunicationRepository
import com.example.home.domain.value_object.communication.*
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsCommunication
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class CommunicationRepositoryImpl : CommunicationRepository {

    override fun refer(giftId: GiftId?): List<Communication> {
        return transaction {
            if (giftId == null) {
                TbTsCommunication.selectAll()
                    .map {
                        Communication(
                            GiftId(it[TbTsCommunication.communicationId]),
                            GroupsId(it[TbTsCommunication.groupsId]),
                            it[TbTsCommunication.comGiftDate],
                            GiftFrom(it[TbTsCommunication.comGiftFrom]),
                            GiftTo(it[TbTsCommunication.comGiftTo]),
                            GiftItem(it[TbTsCommunication.comGiftWhat]),
                            Amount(it[TbTsCommunication.comGiftMoney]),
                            GiftRemarks(it[TbTsCommunication.comGiftReason]),
                            it[TbTsCommunication.comReturnDate],
                            GiftItem(it[TbTsCommunication.comReturnWhat]),
                            Amount(it[TbTsCommunication.comReturnMoney]),
                            GiftRemarks(it[TbTsCommunication.comReturnRemarks]),
                            GiftReturnFlg(it[TbTsCommunication.returnFlg])
                        )
                    }
            } else {
                TbTsCommunication.select {
                    Op.build {
                        val condition: Op<Boolean> = TbTsCommunication.communicationId eq giftId.value
                        condition
                    }
                }.map {
                    Communication(
                        GiftId(it[TbTsCommunication.communicationId]),
                        GroupsId(it[TbTsCommunication.groupsId]),
                        it[TbTsCommunication.comGiftDate],
                        GiftFrom(it[TbTsCommunication.comGiftFrom]),
                        GiftTo(it[TbTsCommunication.comGiftTo]),
                        GiftItem(it[TbTsCommunication.comGiftWhat]),
                        Amount(it[TbTsCommunication.comGiftMoney]),
                        GiftRemarks(it[TbTsCommunication.comGiftReason]),
                        it[TbTsCommunication.comReturnDate],
                        GiftItem(it[TbTsCommunication.comReturnWhat]),
                        Amount(it[TbTsCommunication.comReturnMoney]),
                        GiftRemarks(it[TbTsCommunication.comReturnRemarks]),
                        GiftReturnFlg(it[TbTsCommunication.returnFlg])
                    )
                }
            }
        }
    }

    override fun save(
        groupsId: GroupsId,
        giftDate: LocalDate,
        giftFrom: GiftFrom,
        giftTo: GiftTo,
        giftItem: GiftItem,
        giftAmount: Amount,
        giftRemarks: GiftRemarks,
        giftRtnDate: LocalDate,
        giftRtnItem: GiftItem,
        giftRtnAmount: Amount,
        giftRtnRemarks: GiftRemarks,
        returnFlg: GiftReturnFlg
    ): Communication {
        return transaction {
            TbTsCommunication.insert {
                it[TbTsCommunication.groupsId] = groupsId.value
                it[comGiftDate] = giftDate
                it[comGiftFrom] = giftFrom.value
                it[comGiftTo] = giftTo.value
                it[comGiftWhat] = giftItem.value
                it[comGiftMoney] = giftAmount.value
                it[comGiftReason] = giftRemarks.value
                it[comReturnDate] = giftRtnDate
                it[comReturnWhat] = giftRtnItem.value
                it[comReturnMoney] = giftRtnAmount.value
                it[comReturnRemarks] = giftRtnRemarks.value
                it[TbTsCommunication.returnFlg] = returnFlg.value
            }

            val communication = TbTsCommunication.select {
                (TbTsCommunication.groupsId eq groupsId.value) and
                        (TbTsCommunication.comGiftDate eq giftDate) and
                        (TbTsCommunication.comGiftFrom eq giftFrom.value) and
                        (TbTsCommunication.comGiftTo eq giftTo.value) and
                        (TbTsCommunication.comGiftWhat eq giftItem.value) and
                        (TbTsCommunication.comGiftMoney eq giftAmount.value) and
                        (TbTsCommunication.comGiftReason eq giftRemarks.value)
            }.singleOrNull()

            return@transaction communication?.let {
                Communication(
                    GiftId(it[TbTsCommunication.communicationId]),
                    GroupsId(it[TbTsCommunication.groupsId]),
                    it[TbTsCommunication.comGiftDate],
                    GiftFrom(it[TbTsCommunication.comGiftFrom]),
                    GiftTo(it[TbTsCommunication.comGiftTo]),
                    GiftItem(it[TbTsCommunication.comGiftWhat]),
                    Amount(it[TbTsCommunication.comGiftMoney]),
                    GiftRemarks(it[TbTsCommunication.comGiftReason]),
                    it[TbTsCommunication.comReturnDate],
                    GiftItem(it[TbTsCommunication.comReturnWhat]),
                    Amount(it[TbTsCommunication.comReturnMoney]),
                    GiftRemarks(it[TbTsCommunication.comReturnRemarks]),
                    GiftReturnFlg(it[TbTsCommunication.returnFlg])
                )
            } ?: throw IllegalStateException("Failed to save the Communication")
        }
    }

    override fun update(
        giftId: GiftId,
        giftDate: LocalDate?,
        giftFrom: GiftFrom?,
        giftTo: GiftTo?,
        giftItem: GiftItem?,
        giftAmount: Amount?,
        giftRemarks: GiftRemarks?,
        giftRtnDate: LocalDate?,
        giftRtnItem: GiftItem?,
        giftRtnAmount: Amount?,
        giftRtnRemarks: GiftRemarks?,
        returnFlg: GiftReturnFlg?
    ): Int {
        return transaction {
            var condition: Op<Boolean> = TbTsCommunication.communicationId eq giftId.value

            val updateRows = TbTsCommunication.update({
                condition
            }) {
                if (giftDate != null) {
                    it[comGiftDate] = giftDate
                }
                if (giftFrom != null) {
                    it[comGiftFrom] = giftFrom.value
                }
                if (giftTo != null) {
                    it[comGiftTo] = giftTo.value
                }
                if (giftItem != null) {
                    it[comGiftWhat] = giftItem.value
                }
                if (giftAmount != null) {
                    it[comGiftMoney] = giftAmount.value
                }
                if (giftRemarks != null) {
                    it[comGiftReason] = giftRemarks.value
                }
                if (giftRtnDate != null) {
                    it[comReturnDate] = giftRtnDate
                }
                if (giftRtnItem != null) {
                    it[comReturnWhat] = giftRtnItem.value
                }
                if (giftRtnAmount != null) {
                    it[comReturnMoney] = giftRtnAmount.value
                }
                if (giftRtnRemarks != null) {
                    it[comReturnRemarks] = giftRtnRemarks.value
                }
                if (returnFlg != null) {
                    it[TbTsCommunication.returnFlg] = returnFlg.value
                }
            }
            return@transaction updateRows
        }
    }

    override fun delete(groupsId: GroupsId?, giftId: GiftId?): Int {
        return transaction {
            if (groupsId == null && giftId == null) {
                return@transaction 0
            }
            val deleteRows = TbTsCommunication.deleteWhere {
                Op.build {
                    var condition: Op<Boolean> = Op.TRUE
                    groupsId?.let { condition = condition and (TbTsCommunication.groupsId eq it.value) }
                    giftId?.let { condition = condition and (communicationId eq it.value) }
                    condition
                }
            }
            return@transaction deleteRows
        }
    }

}