package com.example.home.domain.repository.category

import com.example.home.domain.entity.category.Category
import com.example.home.domain.value_object.category.CategoryName
import com.example.home.domain.value_object.category.CategoryNo
import com.example.home.domain.value_object.group.GroupsId

interface CategoryRepository {
    fun refer(groupsId: GroupsId? = null): List<Category>
    fun save(
        groupsId: GroupsId,
        categoryNo: CategoryNo,
        categoryName: CategoryName
    ): Category

    fun update(
        groupsId: GroupsId,
        categoryNo: CategoryNo,
        categoryName: CategoryName
    ): Int

    fun delete(groupsId: GroupsId, categoryNo: CategoryNo? = null, categoryName: CategoryName? = null): Int

}