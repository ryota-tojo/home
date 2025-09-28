package com.example.home.service.category

import com.example.home.domain.entity.category.result.CategoryDeleteResult
import com.example.home.domain.entity.category.result.CategoryReferResult
import com.example.home.domain.entity.category.result.CategorySaveResult
import com.example.home.domain.entity.category.result.CategoryUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.budgets.BudgetsRepository
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.category.CategoryName
import com.example.home.domain.value_object.category.CategoryNo
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.util.ValidationCheck
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val budgetsRepository: BudgetsRepository
) {
    fun refer(
        categoryId: CategoryId? = null,
        groupsId: GroupsId? = null,
        categoryNo: CategoryNo? = null,
        offset: Long? = null,
        limit: Int? = null,
    ): CategoryReferResult {
        val categoryList = categoryRepository.refer(categoryId, groupsId, categoryNo, offset, limit)
        if (categoryList.isNullOrEmpty()) {
            return CategoryReferResult(
                ResponseCode.データ不在エラー.code,
                categoryList
            )
        }
        return CategoryReferResult(
            ResponseCode.成功.code,
            categoryList
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

    fun update(
        categoryId: CategoryId,
        categoryNo: CategoryNo? = null,
        categoryName: CategoryName? = null
    ): CategoryUpdateResult {
        if (categoryName != null) {
            if (!ValidationCheck.symbol(categoryName.toString()).result) {
                return CategoryUpdateResult(
                    ResponseCode.バリデーションエラー.code,
                    0
                )
            }
        }
        val updateRows = categoryRepository.update(categoryId, categoryNo, categoryName)
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

    fun setDeleted(categoryId: CategoryId): CategoryUpdateResult {
        val updateRows = categoryRepository.setDeleted(categoryId)
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

    fun setUnDeleted(categoryId: CategoryId): CategoryUpdateResult {
        val updateRows = categoryRepository.setUnDeleted(categoryId)
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

    fun delete(groupsId: GroupsId, categoryId: CategoryId? = null): CategoryDeleteResult {

        val categoryList = categoryRepository.refer(categoryId, groupsId)
        if (categoryList.isNullOrEmpty()) {
            return CategoryDeleteResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        categoryList.forEach{ category ->
            budgetsRepository.refer(groupsId,categoryId = category.id).forEach{ budgets ->
                if(budgets.fixedFlg.value == 0){
                    budgetsRepository.delete(groupsId,budgets.YYYY,budgets.MM, budgets.categoryId)
                }
            }
        }

        val deleteRows = categoryRepository.delete(groupsId, categoryId)

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