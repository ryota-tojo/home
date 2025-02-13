package com.example.home.domain.repository.category

import com.example.home.domain.entity.category.Category
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.category.CategoryName
import com.example.home.domain.value_object.group.GroupsId

interface CategoryRepository {
    fun refer(groupsId: GroupsId? = null): List<Category>
    fun save(
        groupsId: GroupsId,
        categoryNo: CategoryId,
        categoryName: CategoryName
    ): Category

    fun update(
        groupsId: GroupsId,
        categoryNo: CategoryId,
        categoryName: CategoryName
    ): Int

    fun delete(groupsId: GroupsId, categoryNo: CategoryId? = null, categoryName: CategoryName? = null): Int

}