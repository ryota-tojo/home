package com.example.home.domain.repository.template

import com.example.home.domain.entity.template.ShoppingSearchTemplate
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

interface ShoppingSearchTemplateRepository {
    fun refer(groupsId: GroupsId? = null, templateId: TemplateId? = null): List<ShoppingSearchTemplate>
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
    ): ShoppingSearchTemplate

    fun update(
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
    ): Int

    fun delete(groupsId: GroupsId, templateId: TemplateId? = null): Int

}


