package com.example.home.domain.entity.category

import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.category.CategoryName
import com.example.home.domain.value_object.group.GroupsId

data class Category(
    val id: CategoryId,
    val groupsId: GroupsId,
    val categoryNo: CategoryId,
    val categoryName: CategoryName
)
