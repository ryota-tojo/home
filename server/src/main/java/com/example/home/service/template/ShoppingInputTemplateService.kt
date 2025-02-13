package com.example.home.service.template

import com.example.home.domain.entity.template.result.ShoppingInputTemplateDeleteResult
import com.example.home.domain.entity.template.result.ShoppingInputTemplateReferResult
import com.example.home.domain.entity.template.result.ShoppingInputTemplateSaveResult
import com.example.home.domain.entity.template.result.ShoppingInputTemplateUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.master.ChoicesRepository
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.repository.template.ShoppingInputTemplateRepository
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
class ShoppingInputTemplateService(
    private val shoppingInputTemplateRepository: ShoppingInputTemplateRepository,
    private val memberRepository: MemberRepository,
    private val categoryRepository: CategoryRepository,
    private val choicesRepository: ChoicesRepository,

    ) {

    fun refer(groupsId: GroupsId? = null, templateId: TemplateId? = null): ShoppingInputTemplateReferResult {
        val shoppingInputTemplateList = shoppingInputTemplateRepository.refer(groupsId, templateId)
        return ShoppingInputTemplateReferResult(
            ResponseCode.成功.code,
            shoppingInputTemplateList
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
        shoppingAmount: Amount,
        shoppingRemarks: ShoppingRemarks,
        templateUseFlg: TemplateUseFlg
    ): ShoppingInputTemplateSaveResult {
        if (!ValidationCheck.symbol(templateId.toString()).result ||
            !ValidationCheck.symbol(templateName.toString()).result
        ) {
            return ShoppingInputTemplateSaveResult(
                ResponseCode.バリデーションエラー.code,
                null
            )
        }

        val templateList = shoppingInputTemplateRepository.refer(groupsId, templateId)
        if (templateList.any { it.templateId == templateId }) {
            return ShoppingInputTemplateSaveResult(
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
            return ShoppingInputTemplateSaveResult(
                inputDataCheckResult,
                null
            )
        }
        val template = shoppingInputTemplateRepository.save(
            groupsId,
            templateId,
            templateName,
            memberId,
            categoryId,
            shoppingType,
            shoppingPayment,
            shoppingSettlement,
            shoppingAmount,
            shoppingRemarks,
            templateUseFlg
        )
        return ShoppingInputTemplateSaveResult(
            ResponseCode.成功.code,
            template
        )
    }

    fun update(
        groupsId: GroupsId,
        templateId: TemplateId,
        templateName: TemplateName,
        memberId: MemberId,
        categoryId: CategoryId,
        shoppingType: ShoppingType,
        shoppingPayment: ShoppingPayment,
        shoppingSettlement: ShoppingSettlement,
        shoppingAmount: Amount,
        shoppingRemarks: ShoppingRemarks,
        templateUseFlg: TemplateUseFlg
    ): ShoppingInputTemplateUpdateResult {
        if (!ValidationCheck.symbol(templateId.toString()).result ||
            !ValidationCheck.symbol(templateName.toString()).result
        ) {
            return ShoppingInputTemplateUpdateResult(
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
            return ShoppingInputTemplateUpdateResult(
                inputDataCheckResult,
                0
            )
        }
        val updateRows = shoppingInputTemplateRepository.update(
            groupsId,
            templateId,
            templateName,
            memberId,
            categoryId,
            shoppingType,
            shoppingPayment,
            shoppingSettlement,
            shoppingAmount,
            shoppingRemarks,
            templateUseFlg
        )
        if (updateRows == 0) {
            return ShoppingInputTemplateUpdateResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        return ShoppingInputTemplateUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun delete(groupsId: GroupsId, templateId: TemplateId? = null): ShoppingInputTemplateDeleteResult {
        val deleteRows = shoppingInputTemplateRepository.delete(groupsId, templateId)
        if (deleteRows == 0) {
            return ShoppingInputTemplateDeleteResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        return ShoppingInputTemplateDeleteResult(
            ResponseCode.成功.code,
            deleteRows
        )
    }

    fun inputDataCheck(
        groupsId: GroupsId,
        memberId: MemberId,
        categoryId: CategoryId,
        type: ShoppingType,
        payment: ShoppingPayment,
        settlement: ShoppingSettlement,
    ): String {

        val memberList = memberRepository.refer(groupsId)
        if (!memberList.any { it.id == memberId }) {
            return ResponseCode.存在しないメンバー.code
        }

        val categoryList = categoryRepository.refer(groupsId)
        if (!categoryList.any { it.id == categoryId }) {
            return ResponseCode.存在しないカテゴリー.code
        }

        choicesRepository.getItemName(ChoicesItemType("type"), ChoicesItemNo(type.value))
            ?: return ResponseCode.存在しない購入種別.code

        choicesRepository.getItemName(ChoicesItemType("payment"), ChoicesItemNo(payment.value))
            ?: return ResponseCode.存在しない支払い方法.code

        choicesRepository.getItemName(ChoicesItemType("settlement"), ChoicesItemNo(settlement.value))
            ?: return ResponseCode.存在しない精算状況.code

        return ResponseCode.成功.code

    }
}