package com.example.home.service.template

import com.example.home.domain.entity.template.result.ShoppingSearchTemplateDeleteResult
import com.example.home.domain.entity.template.result.ShoppingSearchTemplateReferResult
import com.example.home.domain.entity.template.result.ShoppingSearchTemplateSaveResult
import com.example.home.domain.entity.template.result.ShoppingSearchTemplateUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.master.ChoicesRepository
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.repository.template.ShoppingSearchTemplateRepository
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.master.ChoicesItemNo
import com.example.home.domain.value_object.master.ChoicesItemType
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.shopping.ShoppingPayment
import com.example.home.domain.value_object.shopping.ShoppingRemarks
import com.example.home.domain.value_object.shopping.ShoppingSettlement
import com.example.home.domain.value_object.shopping.ShoppingType
import com.example.home.domain.value_object.template.TemplateId
import com.example.home.domain.value_object.template.TemplateName
import com.example.home.domain.value_object.template.TemplateUseFlg
import com.example.home.util.ValidationCheck
import org.springframework.stereotype.Service

@Service
class ShoppingSearchTemplateService(
    private val shoppingSearchTemplateRepository: ShoppingSearchTemplateRepository,
    private val memberRepository: MemberRepository,
    private val categoryRepository: CategoryRepository,
    private val choicesRepository: ChoicesRepository,

    ) {

    fun refer(groupsId: GroupsId? = null, templateId: TemplateId? = null): ShoppingSearchTemplateReferResult {
        val shoppingSearchTemplateList = shoppingSearchTemplateRepository.refer(groupsId, templateId)
        return ShoppingSearchTemplateReferResult(
            ResponseCode.成功.code,
            shoppingSearchTemplateList
        )
    }

    fun save(
        groupsId: GroupsId,
        templateId: TemplateId,
        templateName: TemplateName,
        memberId: MemberId,
        categoryId: CategoryId,
        shoppingType: ShoppingType,
        shoppingPayment: ShoppingPayment,
        shoppingSettlement: ShoppingSettlement,
        shoppingMinAmount: Amount,
        shoppingMaxAmount: Amount,
        shoppingRemarks: ShoppingRemarks,
        templateUseFlg: TemplateUseFlg
    ): ShoppingSearchTemplateSaveResult {
        if (!ValidationCheck.symbol(templateId.toString()).result ||
            !ValidationCheck.symbol(templateName.toString()).result
        ) {
            return ShoppingSearchTemplateSaveResult(
                ResponseCode.バリデーションエラー.code,
                null
            )
        }

        val templateList = shoppingSearchTemplateRepository.refer(groupsId, templateId)
        if (templateList.any { it.templateId == templateId }) {
            return ShoppingSearchTemplateSaveResult(
                ResponseCode.重複するテンプレートID.code,
                null
            )
        }
        val inputDataCheckResult = inputDataCheck(
            groupsId,
            memberId,
            categoryId,
            shoppingType,
            shoppingPayment,
            shoppingSettlement
        )
        if (inputDataCheckResult != ResponseCode.成功.code) {
            return ShoppingSearchTemplateSaveResult(
                inputDataCheckResult,
                null
            )
        }
        val template = shoppingSearchTemplateRepository.save(
            groupsId,
            templateId,
            templateName,
            memberId,
            categoryId,
            shoppingType,
            shoppingPayment,
            shoppingSettlement,
            shoppingMinAmount,
            shoppingMaxAmount,
            shoppingRemarks,
            templateUseFlg
        )
        return ShoppingSearchTemplateSaveResult(
            ResponseCode.成功.code,
            template
        )
    }

    fun update(
        groupsId: GroupsId,
        templateId: TemplateId,
        templateName: TemplateName? = null,
        memberId: MemberId? = null,
        categoryId: CategoryId? = null,
        shoppingType: ShoppingType? = null,
        shoppingPayment: ShoppingPayment? = null,
        shoppingSettlement: ShoppingSettlement? = null,
        shoppingMinAmount: Amount? = null,
        shoppingMaxAmount: Amount? = null,
        shoppingRemarks: ShoppingRemarks? = null,
        templateUseFlg: TemplateUseFlg? = null,
    ): ShoppingSearchTemplateUpdateResult {
        if (!ValidationCheck.symbol(templateId.toString()).result ||
            !ValidationCheck.symbol(templateName.toString()).result
        ) {
            return ShoppingSearchTemplateUpdateResult(
                ResponseCode.バリデーションエラー.code,
                0
            )
        }
        val inputDataCheckResult = inputDataCheck(
            groupsId,
            memberId,
            categoryId,
            shoppingType,
            shoppingPayment,
            shoppingSettlement
        )
        if (inputDataCheckResult != ResponseCode.成功.code) {
            return ShoppingSearchTemplateUpdateResult(
                inputDataCheckResult,
                0
            )
        }
        val updateRows = shoppingSearchTemplateRepository.update(
            groupsId,
            templateId,
            templateName,
            memberId,
            categoryId,
            shoppingType,
            shoppingPayment,
            shoppingSettlement,
            shoppingMinAmount,
            shoppingMaxAmount,
            shoppingRemarks,
            templateUseFlg
        )
        if (updateRows == 0) {
            return ShoppingSearchTemplateUpdateResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        return ShoppingSearchTemplateUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun usage(
        groupsId: GroupsId,
        templateId: TemplateId
    ): ShoppingSearchTemplateUpdateResult {
        val updateRows = shoppingSearchTemplateRepository.usage(groupsId, templateId)
        return ShoppingSearchTemplateUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun unUsage(
        groupsId: GroupsId,
        templateId: TemplateId
    ): ShoppingSearchTemplateUpdateResult {
        val updateRows = shoppingSearchTemplateRepository.usage(groupsId, templateId)
        return ShoppingSearchTemplateUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun setDeleted(
        groupsId: GroupsId,
        templateId: TemplateId
    ): ShoppingSearchTemplateUpdateResult {
        val updateRows = shoppingSearchTemplateRepository.setDeleted(groupsId, templateId)
        if (updateRows == 0) {
            return ShoppingSearchTemplateUpdateResult(
                ResponseCode.データ不在エラー.code,
                updateRows
            )
        }
        return ShoppingSearchTemplateUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun delete(groupsId: GroupsId, templateId: TemplateId? = null): ShoppingSearchTemplateDeleteResult {
        val deleteRows = shoppingSearchTemplateRepository.delete(groupsId, templateId)
        if (deleteRows == 0) {
            return ShoppingSearchTemplateDeleteResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        return ShoppingSearchTemplateDeleteResult(
            ResponseCode.成功.code,
            deleteRows
        )
    }

    fun inputDataCheck(
        groupsId: GroupsId,
        memberId: MemberId? = null,
        categoryId: CategoryId? = null,
        type: ShoppingType? = null,
        payment: ShoppingPayment? = null,
        settlement: ShoppingSettlement? = null
    ): String {
        if (memberId != null) {
            val memberList = memberRepository.refer(null, groupsId, null)
            if (!memberList.any { it.id == memberId }) {
                return ResponseCode.存在しないメンバー.code
            }
        }

        if (categoryId != null) {
            val categoryList = categoryRepository.refer(null, groupsId, null)
            if (!categoryList.any { it.id == categoryId }) {
                return ResponseCode.存在しないカテゴリー.code
            }
        }

        if (type != null) {
            choicesRepository.getItemName(ChoicesItemType("type"), ChoicesItemNo(type.value))
                ?: return ResponseCode.存在しない購入種別.code
        }

        if (payment != null) {
            choicesRepository.getItemName(ChoicesItemType("payment"), ChoicesItemNo(payment.value))
                ?: return ResponseCode.存在しない支払い方法.code
        }

        if (settlement != null) {
            choicesRepository.getItemName(ChoicesItemType("settlement"), ChoicesItemNo(settlement.value))
                ?: return ResponseCode.存在しない精算状況.code
        }
        return ResponseCode.成功.code

    }
}