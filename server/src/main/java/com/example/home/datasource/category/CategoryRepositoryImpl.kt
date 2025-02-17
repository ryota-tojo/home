package com.example.home.datasource.category

import com.example.home.domain.entity.category.Category
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.category.CategoryName
import com.example.home.domain.value_object.category.CategoryNo
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsCategorys
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class CategoryRepositoryImpl : CategoryRepository {
    override fun refer(categoryId: CategoryId?, groupsId: GroupsId?, categoryNo: CategoryNo?): List<Category> {
        return transaction {
            var condition: Op<Boolean> = TbTsCategorys.deletedFlg eq 0
            if (categoryId != null) {
                categoryId.let { condition = condition and (TbTsCategorys.categoryId eq categoryId.value) }
            } else {
                groupsId?.let { condition = condition and (TbTsCategorys.groupsId eq it.value) }
                categoryNo?.let { condition = condition and (TbTsCategorys.categoryNo eq it.value) }
            }
            TbTsCategorys
                .select {
                    condition
                }
                .orderBy(TbTsCategorys.categoryNo to SortOrder.ASC)
                .map {
                    Category(
                        CategoryId(it[TbTsCategorys.categoryId]),
                        GroupsId(it[TbTsCategorys.groupsId]),
                        CategoryNo(it[TbTsCategorys.categoryNo]),
                        CategoryName(it[TbTsCategorys.categoryName])
                    )
                }
        }
    }

    override fun save(
        groupsId: GroupsId,
        categoryNo: CategoryNo,
        categoryName: CategoryName
    ): Category {
        return transaction {
            TbTsCategorys.insert {
                it[TbTsCategorys.groupsId] = groupsId.value
                it[TbTsCategorys.categoryNo] = categoryNo.value
                it[TbTsCategorys.categoryName] = categoryName.value
            }

            val category = TbTsCategorys.select {
                (TbTsCategorys.groupsId eq groupsId.value) and
                        (TbTsCategorys.categoryNo eq categoryNo.value) and
                        (TbTsCategorys.categoryName eq categoryName.value)
            }.singleOrNull()

            return@transaction category?.let {
                Category(
                    CategoryId(it[TbTsCategorys.categoryId]),
                    GroupsId(it[TbTsCategorys.groupsId]),
                    CategoryNo(it[TbTsCategorys.categoryNo]),
                    CategoryName(it[TbTsCategorys.categoryName])
                )
            } ?: throw IllegalStateException("Failed to save the Category")
        }
    }

    override fun update(
        categoryId: CategoryId,
        categoryNo: CategoryNo?,
        categoryName: CategoryName?
    ): Int {
        return transaction {
            var condition: Op<Boolean> = TbTsCategorys.categoryId eq categoryId.value
            categoryNo?.let { condition = condition and (TbTsCategorys.categoryNo eq it.value) }
            categoryName?.let { condition = condition and (TbTsCategorys.categoryName eq it.value) }
            val updateRows = TbTsCategorys.update({
                condition
            }) {
                if (categoryNo != null) {
                    it[TbTsCategorys.categoryNo] = categoryNo.value
                }
                if (categoryName != null) {
                    it[TbTsCategorys.categoryName] = categoryName.value
                }
            }
            return@transaction updateRows
        }
    }

    override fun setDeleted(categoryId: CategoryId): Int {
        return transaction {
            var condition: Op<Boolean> = TbTsCategorys.categoryId eq categoryId.value
            val updateRows = TbTsCategorys.update({
                condition
            }) {
                it[deletedFlg] = 1
            }
            return@transaction updateRows
        }
    }

    override fun delete(groupsId: GroupsId?, categoryId: CategoryId?): Int {
        return transaction {
            val condition = if (groupsId != null) {
                TbTsCategorys.groupsId eq groupsId.value
            } else {
                if (categoryId != null) {
                    TbTsCategorys.categoryId eq categoryId.value
                } else {
                    return@transaction 0
                }
            }
            val deleteRows = TbTsCategorys.deleteWhere { condition }
            return@transaction deleteRows
        }
    }

}