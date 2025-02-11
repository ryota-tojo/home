package com.example.home.datasource.shopping

import com.example.home.domain.entity.shopping.Shopping
import com.example.home.domain.repository.shopping.ShoppingRepository
import com.example.home.domain.value_object.category.CategoryNo
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.FixedFlg
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberNo
import com.example.home.domain.value_object.shopping.*
import com.example.home.domain.value_object.user.UserId
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsShopping
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.month
import org.jetbrains.exposed.sql.javatime.year
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

class ShoppingRepositoryImpl : ShoppingRepository {
    override fun refer(
        groupsId: GroupsId,
        userId: UserId?,
        shoppingDateYYYY: YYYY?,
        shoppingDateMM: MM?,
        memberNo: MemberNo?,
        categoryNo: CategoryNo?,
        type: ShoppingType?,
        payment: ShoppingPayment?,
        settlement: ShoppingSettlement?,
        minAmount: Amount?,
        maxAmount: Amount?,
        remarks: ShoppingRemarks?
    ): List<Shopping> {
        return transaction {
            TbTsShopping.select {
                Op.build {
                    var condition: Op<Boolean> = Op.TRUE
                    condition = condition and (TbTsShopping.groupsId eq groupsId.value)
                    userId?.let { condition = condition and (TbTsShopping.userId eq it.value) }
                    shoppingDateYYYY?.let { condition = condition and (TbTsShopping.shoppingDate.year() eq it.value) }
                    shoppingDateMM?.let { condition = condition and (TbTsShopping.shoppingDate.month() eq it.value) }
                    memberNo?.let { condition = condition and (TbTsShopping.memberNo eq it.value) }
                    categoryNo?.let { condition = condition and (TbTsShopping.categoryNo eq it.value) }
                    type?.let { condition = condition and (TbTsShopping.type eq it.value) }
                    payment?.let { condition = condition and (TbTsShopping.payment eq it.value) }
                    settlement?.let { condition = condition and (TbTsShopping.settlement eq it.value) }
                    minAmount?.let { condition = condition and (TbTsShopping.amount greaterEq it.value) }
                    maxAmount?.let { condition = condition and (TbTsShopping.amount lessEq it.value) }
                    remarks?.let { condition = condition and (TbTsShopping.remarks like "%${it.value}%") }
                    condition
                }
            }.map {
                Shopping(
                    ShoppingId(it[TbTsShopping.id]),
                    GroupsId(it[TbTsShopping.groupsId]),
                    UserId(it[TbTsShopping.userId]),
                    it[TbTsShopping.shoppingDate],
                    MemberNo(it[TbTsShopping.memberNo]),
                    CategoryNo(it[TbTsShopping.categoryNo]),
                    ShoppingType(it[TbTsShopping.type]),
                    ShoppingPayment(it[TbTsShopping.payment]),
                    ShoppingSettlement(it[TbTsShopping.settlement]),
                    Amount(it[TbTsShopping.amount]),
                    ShoppingRemarks(it[TbTsShopping.remarks]),
                    FixedFlg(it[TbTsShopping.fixedFlg])
                )
            }
        }
    }

    override fun save(
        groupsId: GroupsId,
        userId: UserId,
        shoppingDate: LocalDate,
        memberNo: MemberNo,
        categoryNo: CategoryNo,
        type: ShoppingType,
        payment: ShoppingPayment,
        settlement: ShoppingSettlement,
        amount: Amount,
        remarks: ShoppingRemarks
    ): Shopping {
        return transaction {
            TbTsShopping.insert {
                it[TbTsShopping.groupsId] = groupsId.value
                it[TbTsShopping.userId] = userId.value
                it[TbTsShopping.shoppingDate] = shoppingDate
                it[TbTsShopping.memberNo] = memberNo.value
                it[TbTsShopping.categoryNo] = categoryNo.value
                it[TbTsShopping.type] = type.value
                it[TbTsShopping.payment] = payment.value
                it[TbTsShopping.settlement] = settlement.value
                it[TbTsShopping.amount] = amount.value
                it[TbTsShopping.remarks] = remarks.value
                it[fixedFlg] = 0
            }

            val shopping = TbTsShopping.select {
                (TbTsShopping.groupsId eq groupsId.value) and
                        (TbTsShopping.userId eq userId.value) and
                        (TbTsShopping.shoppingDate eq shoppingDate) and
                        (TbTsShopping.memberNo eq memberNo.value) and
                        (TbTsShopping.categoryNo eq categoryNo.value) and
                        (TbTsShopping.type eq type.value) and
                        (TbTsShopping.payment eq payment.value) and
                        (TbTsShopping.settlement eq settlement.value) and
                        (TbTsShopping.amount eq amount.value) and
                        (TbTsShopping.remarks eq remarks.value) and
                        (TbTsShopping.fixedFlg eq 0)
            }.singleOrNull()

            return@transaction shopping?.let {
                Shopping(
                    ShoppingId(it[TbTsShopping.id]),
                    GroupsId(it[TbTsShopping.groupsId]),
                    UserId(it[TbTsShopping.userId]),
                    it[TbTsShopping.shoppingDate],
                    MemberNo(it[TbTsShopping.memberNo]),
                    CategoryNo(it[TbTsShopping.categoryNo]),
                    ShoppingType(it[TbTsShopping.type]),
                    ShoppingPayment(it[TbTsShopping.payment]),
                    ShoppingSettlement(it[TbTsShopping.settlement]),
                    Amount(it[TbTsShopping.amount]),
                    ShoppingRemarks(it[TbTsShopping.remarks]),
                    FixedFlg(it[TbTsShopping.fixedFlg])
                )
            } ?: throw IllegalStateException("Failed to save the Shopping")
        }
    }

    override fun isDuplication(
        groupsId: GroupsId,
        shoppingDate: LocalDate,
        memberNo: MemberNo,
        categoryNo: CategoryNo,
        type: ShoppingType,
        payment: ShoppingPayment,
        settlement: ShoppingSettlement,
        amount: Amount,
        remarks: ShoppingRemarks
    ): Boolean {
        return transaction {
            val shoppingList = TbTsShopping.select {
                Op.build {
                    var condition: Op<Boolean> = Op.TRUE
                    condition = condition and (TbTsShopping.groupsId eq groupsId.value)
                    shoppingDate.let { condition = condition and (TbTsShopping.shoppingDate eq it) }
                    memberNo.let { condition = condition and (TbTsShopping.memberNo eq it.value) }
                    categoryNo.let { condition = condition and (TbTsShopping.categoryNo eq it.value) }
                    type.let { condition = condition and (TbTsShopping.type eq it.value) }
                    payment.let { condition = condition and (TbTsShopping.payment eq it.value) }
                    settlement.let { condition = condition and (TbTsShopping.settlement eq it.value) }
                    amount.let { condition = condition and (TbTsShopping.amount eq it.value) }
                    remarks.let { condition = condition and (TbTsShopping.remarks eq it.value) }
                    condition
                }
            }.map {
                Shopping(
                    ShoppingId(it[TbTsShopping.id]),
                    GroupsId(it[TbTsShopping.groupsId]),
                    UserId(it[TbTsShopping.userId]),
                    it[TbTsShopping.shoppingDate],
                    MemberNo(it[TbTsShopping.memberNo]),
                    CategoryNo(it[TbTsShopping.categoryNo]),
                    ShoppingType(it[TbTsShopping.type]),
                    ShoppingPayment(it[TbTsShopping.payment]),
                    ShoppingSettlement(it[TbTsShopping.settlement]),
                    Amount(it[TbTsShopping.amount]),
                    ShoppingRemarks(it[TbTsShopping.remarks]),
                    FixedFlg(it[TbTsShopping.fixedFlg])
                )
            }
            return@transaction shoppingList.size > 1
        }
    }

    override fun update(
        shoppingId: ShoppingId,
        groupsId: GroupsId,
        userId: UserId,
        shoppingDate: LocalDate,
        memberNo: MemberNo,
        categoryNo: CategoryNo,
        type: ShoppingType,
        payment: ShoppingPayment,
        settlement: ShoppingSettlement,
        amount: Amount,
        remarks: ShoppingRemarks,
        fixedFlg: FixedFlg
    ): Int {
        return transaction {
            val updateRows = TbTsShopping.update({
                TbTsShopping.id eq shoppingId.value
            }) {
                it[TbTsShopping.groupsId] = groupsId.value
                it[TbTsShopping.userId] = userId.value
                it[TbTsShopping.shoppingDate] = shoppingDate
                it[TbTsShopping.memberNo] = memberNo.value
                it[TbTsShopping.categoryNo] = categoryNo.value
                it[TbTsShopping.type] = type.value
                it[TbTsShopping.payment] = payment.value
                it[TbTsShopping.settlement] = settlement.value
                it[TbTsShopping.amount] = amount.value
                it[TbTsShopping.remarks] = remarks.value
                it[TbTsShopping.fixedFlg] = fixedFlg.value
            }
            return@transaction updateRows
        }
    }

    override fun shoppingDatafixed(
        groupsId: GroupsId,
        shoppingDateYYYY: YYYY,
        shoppingDateMM: MM,
        fixedFlg: FixedFlg
    ): Int {
        val updateRows = TbTsShopping.update({
            (TbTsShopping.groupsId eq groupsId.value) and
                    (TbTsShopping.shoppingDate.year() eq shoppingDateYYYY.value) and
                    (TbTsShopping.shoppingDate.month() eq shoppingDateMM.value)
        }) {
            it[TbTsShopping.fixedFlg] = fixedFlg.value
        }
        return updateRows
    }

    override fun delete(
        shoppingId: ShoppingId?,
        groupsId: GroupsId?,
        shoppingDateYYYY: YYYY?,
        shoppingDateMM: MM?,
    ): Int {
        val deleteRows = TbTsShopping.deleteWhere {
            Op.build {
                var condition: Op<Boolean> = Op.TRUE
                shoppingId?.let { condition = condition and (id eq it.value) }
                groupsId?.let { condition = condition and (TbTsShopping.groupsId eq it.value) }
                shoppingDateYYYY?.let { condition = condition and (shoppingDate.year() eq it.value) }
                shoppingDateMM?.let { condition = condition and (shoppingDate.month() eq it.value) }
                condition
            }
        }
        return deleteRows
    }
}