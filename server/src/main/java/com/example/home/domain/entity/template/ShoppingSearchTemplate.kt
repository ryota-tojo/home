package com.example.home.domain.entity.template

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
import com.example.home.domain.value_object.template.TmpId

data class ShoppingSearchTemplate(
    val id: TmpId,
    val groupsId: GroupsId,
    val templateId: TemplateId,
    val templateName: TemplateName,
    val memberId: MemberId,
    val categoryId: CategoryId,
    val shoppingType: ShoppingType,
    val shoppingPayment: ShoppingPayment,
    val shoppingSettlement: ShoppingSettlement,
    val shoppingMinAmount: Amount,
    val shoppingMaxAmount: Amount,
    val shoppingRemarks: ShoppingRemarks,
    val templateUseFlg: TemplateUseFlg
)





