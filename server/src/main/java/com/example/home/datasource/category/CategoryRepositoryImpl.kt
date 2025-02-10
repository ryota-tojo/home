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
    override fun refer(groupsId: GroupsId?): List<Category> {
        return transaction {
            if (groupsId == null) {
                TbTsCategorys.selectAll()
                    .map {
                        Category(
                            CategoryId(it[TbTsCategorys.id]),
                            GroupsId(it[TbTsCategorys.groupsId]),
                            CategoryNo(it[TbTsCategorys.categoryNo]),
                            CategoryName(it[TbTsCategorys.categoryName])
                        )
                    }
            } else {
                TbTsCategorys.select(TbTsCategorys.groupsId eq groupsId.value)
                    .map {
                        Category(
                            CategoryId(it[TbTsCategorys.id]),
                            GroupsId(it[TbTsCategorys.groupsId]),
                            CategoryNo(it[TbTsCategorys.categoryNo]),
                            CategoryName(it[TbTsCategorys.categoryName])
                        )
                    }
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
                    CategoryId(it[TbTsCategorys.id]),
                    GroupsId(it[TbTsCategorys.groupsId]),
                    CategoryNo(it[TbTsCategorys.categoryNo]),
                    CategoryName(it[TbTsCategorys.categoryName])
                )
            } ?: throw IllegalStateException("Failed to save the Category")
        }
    }

    override fun update(
        groupsId: GroupsId,
        categoryNo: CategoryNo,
        categoryName: CategoryName
    ): Int {
        return transaction {
            val updateRows = TbTsCategorys.update({
                (TbTsCategorys.groupsId eq groupsId.value) and
                        (TbTsCategorys.categoryNo eq categoryNo.value)
            }) {
                it[TbTsCategorys.groupsId] = groupsId.value
                it[TbTsCategorys.categoryNo] = categoryNo.value
                it[TbTsCategorys.categoryName] = categoryName.value
            }
            return@transaction updateRows
        }
    }

    override fun delete(groupsId: GroupsId, categoryNo: CategoryNo?, categoryName: CategoryName?): Int {
        return transaction {
            val condition = TbTsCategorys.groupsId eq groupsId.value

            val fullCondition = when {
                categoryNo != null && categoryName != null ->
                    condition and (TbTsCategorys.categoryNo eq categoryNo.value) and (TbTsCategorys.categoryName eq categoryName.value)

                categoryNo != null ->
                    condition and (TbTsCategorys.categoryNo eq categoryNo.value)

                categoryName != null ->
                    condition and (TbTsCategorys.categoryName eq categoryName.value)

                else -> condition
            }

            val deleteRows = TbTsCategorys.deleteWhere { fullCondition }

            return@transaction deleteRows

        }
    }

}