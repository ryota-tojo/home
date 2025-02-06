package com.example.home.domain.entity.template

import com.example.home.domain.value_object.category.CategoryName
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberName
import com.example.home.domain.value_object.shopping.*
import com.example.home.domain.value_object.template.TemplateId
import com.example.home.domain.value_object.template.TemplateName
import com.example.home.domain.value_object.template.TemplateUseFlg

data class ShoppingEntryTemplate(
    val id: TemplateId,
    val groupsId: GroupsId,
    val templateName: TemplateName,
    val memberName: MemberName,
    val categoryName: CategoryName,
    val shoppingType: ShoppingType,
    val shoppingPayment: ShoppingPayment,
    val shoppingSettlement: ShoppingSettlement,
    val shoppingAmount: Amount,
    val shoppingRemarks: ShoppingRemarks,
    val templateUseFlg: TemplateUseFlg
)
