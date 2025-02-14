package com.example.home.datasource.budgets

import com.example.home.domain.entity.budgets.Budgets
import com.example.home.domain.repository.budgets.BudgetsRepository
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.FixedFlg
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsBudgets
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class BudgetsRepositoryImpl : BudgetsRepository {

    override fun refer(groupsId: GroupsId, yyyy: YYYY?, mm: MM?, categoryNo: CategoryId?): List<Budgets> {
        return transaction {
            TbTsBudgets.select {
                Op.build {
                    var condition: Op<Boolean> = TbTsBudgets.groupsId eq groupsId.value
                    yyyy?.let { condition = condition and (TbTsBudgets.bgYyyy eq it.value) }
                    mm?.let { condition = condition and (TbTsBudgets.bgMm eq it.value) }
                    categoryNo?.let { condition = condition and (TbTsBudgets.bgCategoryId eq it.value) }
                    condition
                }
            }.map {
                Budgets(
                    it[TbTsBudgets.id],
                    GroupsId(it[TbTsBudgets.groupsId]),
                    YYYY(it[TbTsBudgets.bgYyyy]),
                    MM(it[TbTsBudgets.bgMm]),
                    CategoryId(it[TbTsBudgets.bgCategoryId]),
                    Amount(it[TbTsBudgets.bgAmount]),
                    FixedFlg(it[TbTsBudgets.fixedFlg])
                )
            }
        }
    }

    override fun save(groupsId: GroupsId, yyyy: YYYY, mm: MM, categoryNo: CategoryId, amount: Amount): Budgets {
        return transaction {
            TbTsBudgets.insert {
                it[TbTsBudgets.groupsId] = groupsId.value
                it[bgYyyy] = yyyy.value
                it[bgMm] = mm.value
                it[bgCategoryId] = categoryNo.value
                it[bgAmount] = amount.value
                it[fixedFlg] = 0
            }
            val budgets = TbTsBudgets.select {
                (TbTsBudgets.groupsId eq groupsId.value) and
                        (TbTsBudgets.bgYyyy eq yyyy.value) and
                        (TbTsBudgets.bgMm eq mm.value) and
                        (TbTsBudgets.bgCategoryId eq categoryNo.value)
            }.singleOrNull()

            return@transaction budgets?.let {
                Budgets(
                    it[TbTsBudgets.id],
                    GroupsId(it[TbTsBudgets.groupsId]),
                    YYYY(it[TbTsBudgets.bgYyyy]),
                    MM(it[TbTsBudgets.bgMm]),
                    CategoryId(it[TbTsBudgets.bgCategoryId]),
                    Amount(it[TbTsBudgets.bgAmount]),
                    FixedFlg(it[TbTsBudgets.fixedFlg])
                )
            } ?: throw IllegalStateException("Failed to save the Budgets")
        }
    }

    override fun update(
        groupsId: GroupsId,
        yyyy: YYYY?,
        mm: MM?,
        categoryId: CategoryId?,
        amount: Amount?,
        fixedFlg: FixedFlg?
    ): Int {
        return transaction {
            var condition: Op<Boolean> = TbTsBudgets.groupsId eq groupsId.value
            yyyy?.let { condition = condition and (TbTsBudgets.bgYyyy eq it.value) }
            mm?.let { condition = condition and (TbTsBudgets.bgMm eq it.value) }
            categoryId?.let { condition = condition and (TbTsBudgets.bgCategoryId eq it.value) }

            val updateRows = TbTsBudgets.update({
                condition
            }) {
                if (amount != null) {
                    it[TbTsBudgets.bgAmount] = amount.value
                }
                if (fixedFlg != null) {
                    it[TbTsBudgets.fixedFlg] = fixedFlg.value
                }
            }

            return@transaction updateRows
        }
    }


    override fun delete(groupsId: GroupsId, yyyy: YYYY?, mm: MM?, categoryNo: CategoryId?): Int {
        return transaction {
            val deleteRows = TbTsBudgets.deleteWhere {
                Op.build {
                    var condition: Op<Boolean> = TbTsBudgets.groupsId eq groupsId.value
                    yyyy?.let { condition = condition and (bgYyyy eq it.value) }
                    mm?.let { condition = condition and (bgMm eq it.value) }
                    categoryNo?.let { condition = condition and (bgCategoryId eq it.value) }
                    condition
                }
            }
            return@transaction deleteRows
        }
    }
}