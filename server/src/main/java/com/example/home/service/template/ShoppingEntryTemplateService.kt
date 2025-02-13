package com.example.home.service.template

import com.example.home.domain.entity.template.result.ShoppingEntryTemplateDeleteResult
import com.example.home.domain.entity.template.result.ShoppingEntryTemplateReferResult
import com.example.home.domain.entity.template.result.ShoppingEntryTemplateSaveResult
import com.example.home.domain.entity.template.result.ShoppingEntryTemplateUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.master.ChoicesRepository
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.repository.template.ShoppingEntryTemplateRepository
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
class ShoppingEntryTemplateService(
    private val shoppingEntryTemplateRepository: ShoppingEntryTemplateRepository,
    private val memberRepository: MemberRepository,
    private val categoryRepository: CategoryRepository,
    private val choicesRepository: ChoicesRepository,

    ) {

    fun refer(groupsId: GroupsId? = null, templateId: TemplateId? = null): ShoppingEntryTemplateReferResult {
        val shoppingEntryTemplateList = shoppingEntryTemplateRepository.refer(groupsId, templateId)
        return ShoppingEntryTemplateReferResult(
            ResponseCode.成功.code,
            shoppingEntryTemplateList
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
    ): ShoppingEntryTemplateSaveResult {
        if (!ValidationCheck.symbol(templateId.toString()).result ||
            !ValidationCheck.symbol(templateName.toString()).result
        ) {
            return ShoppingEntryTemplateSaveResult(
                ResponseCode.バリデーションエラー.code,
                null
            )
        }

        val templateList = shoppingEntryTemplateRepository.refer(groupsId, templateId)
        if (templateList.any { it.templateId == templateId }) {
            return ShoppingEntryTemplateSaveResult(
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
            return ShoppingEntryTemplateSaveResult(
                inputDataCheckResult,
                null
            )
        }
        val template = shoppingEntryTemplateRepository.save(
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
        return ShoppingEntryTemplateSaveResult(
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
    ): ShoppingEntryTemplateUpdateResult {
        if (!ValidationCheck.symbol(templateId.toString()).result ||
            !ValidationCheck.symbol(templateName.toString()).result
        ) {
            return ShoppingEntryTemplateUpdateResult(
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
            return ShoppingEntryTemplateUpdateResult(
                inputDataCheckResult,
                0
            )
        }
        val updateRows = shoppingEntryTemplateRepository.update(
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
            return ShoppingEntryTemplateUpdateResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        return ShoppingEntryTemplateUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun delete(groupsId: GroupsId, templateId: TemplateId? = null): ShoppingEntryTemplateDeleteResult {
        val deleteRows = shoppingEntryTemplateRepository.delete(groupsId, templateId)
        if (deleteRows == 0) {
            return ShoppingEntryTemplateDeleteResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        return ShoppingEntryTemplateDeleteResult(
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