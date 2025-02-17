package com.example.home.domain.repository.template

import com.example.home.domain.entity.template.ShoppingEntryTemplate
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.shopping.ShoppingPayment
import com.example.home.domain.value_object.shopping.ShoppingRemarks
import com.example.home.domain.value_object.shopping.ShoppingSettlement
import com.example.home.domain.value_object.shopping.ShoppingType
import com.example.home.domain.value_object.template.TemplateId
import com.example.home.domain.value_object.template.TemplateName
import com.example.home.domain.value_object.template.TemplateUseFlg

interface ShoppingEntryTemplateRepository {
    fun refer(groupsId: GroupsId? = null, templateId: TemplateId? = null): List<ShoppingEntryTemplate>
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
    ): ShoppingEntryTemplate

    fun update(
        groupsId: GroupsId,
        templateId: TemplateId,
        templateName: TemplateName? = null,
        memberId: MemberId? = null,
        categoryId: CategoryId? = null,
        shoppingType: ShoppingType? = null,
        shoppingPayment: ShoppingPayment? = null,
        shoppingSettlement: ShoppingSettlement? = null,
        shoppingAmount: Amount? = null,
        shoppingRemarks: ShoppingRemarks? = null,
        templateUseFlg: TemplateUseFlg? = null
    ): Int

    fun usage(
        groupsId: GroupsId,
        templateId: TemplateId
    ): Int

    fun unUsage(
        groupsId: GroupsId,
        templateId: TemplateId
    ): Int

    fun setDeleted(
        groupsId: GroupsId,
        templateId: TemplateId
    ): Int

    fun delete(groupsId: GroupsId, templateId: TemplateId? = null): Int

}


