package com.example.home.service.category

import com.example.home.datasource.category.CategoryRepositoryImpl
import com.example.home.domain.entity.category.result.CategoryDeleteResult
import com.example.home.domain.entity.category.result.CategoryReferResult
import com.example.home.domain.entity.category.result.CategorySaveResult
import com.example.home.domain.entity.category.result.CategoryUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.value_object.category.CategoryName
import com.example.home.domain.value_object.category.CategoryNo
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.util.ValidationCheck
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository = CategoryRepositoryImpl()
) {
    fun refer(groupsId: GroupsId): CategoryReferResult {
        val CategoryList = categoryRepository.refer(groupsId)
        if (CategoryList == null) {
            return CategoryReferResult(
                ResponseCode.成功_条件付き.code,
                null
            )
        }
        return CategoryReferResult(
            ResponseCode.成功.code,
            CategoryList
        )
    }

    fun save(groupsId: GroupsId, categoryNo: CategoryNo, categoryName: CategoryName): CategorySaveResult {
        if (!ValidationCheck.symbol(categoryName.toString()).result) {
            return CategorySaveResult(
                ResponseCode.バリデーションエラー.code,
                null
            )
        }
        val category = categoryRepository.save(groupsId, categoryNo, categoryName)
        return CategorySaveResult(
            ResponseCode.成功.code,
            category
        )
    }

    fun update(groupsId: GroupsId, categoryNo: CategoryNo, categoryName: CategoryName): CategoryUpdateResult {
        if (!ValidationCheck.symbol(categoryName.toString()).result) {
            return CategoryUpdateResult(
                ResponseCode.バリデーションエラー.code,
                0
            )
        }
        val updateRows = categoryRepository.update(groupsId, categoryNo, categoryName)
        if (updateRows == 0) {
            return CategoryUpdateResult(
                ResponseCode.データ不在エラー.code,
                updateRows
            )
        }
        return CategoryUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun delete(groupsId: GroupsId, categoryNo: CategoryNo?): CategoryDeleteResult {
        val deleteRows = if (categoryNo != null) {
            categoryRepository.delete(groupsId, categoryNo)
        } else {
            categoryRepository.delete(groupsId)
        }
        if (deleteRows == 0) {
            return CategoryDeleteResult(
                ResponseCode.データ不在エラー.code,
                deleteRows
            )
        }
        return CategoryDeleteResult(
            ResponseCode.成功.code,
            deleteRows
        )
    }
}