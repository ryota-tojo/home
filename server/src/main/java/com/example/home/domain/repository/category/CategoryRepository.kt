package com.example.home.domain.repository.category

import com.example.home.domain.entity.category.Category
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.category.CategoryName
import com.example.home.domain.value_object.category.CategoryNo
import com.example.home.domain.value_object.group.GroupsId

interface CategoryRepository {
    fun refer(
        categoryId: CategoryId? = null,
        groupsId: GroupsId? = null,
        categoryNo: CategoryNo? = null
    ): List<Category>

    fun save(
        groupsId: GroupsId,
        categoryNo: CategoryNo,
        categoryName: CategoryName
    ): Category

    fun update(
        categoryId: CategoryId,
        categoryNo: CategoryNo? = null,
        categoryName: CategoryName? = null
    ): Int

    fun setDeleted(categoryId: CategoryId): Int

    fun delete(groupsId: GroupsId? = null, categoryId: CategoryId? = null): Int

}