package com.example.home.domain.entity.template

import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.shopping.ShoppingPayment
import com.example.home.domain.value_object.shopping.ShoppingRemarks
import com.example.home.domain.value_object.shopping.ShoppingSettlement
import com.example.home.domain.value_object.shopping.ShoppingType
import com.example.home.domain.value_object.template.*

data class ShoppingSearchTemplate(
    val id: TmpId,
    val groupsId: GroupsId,
    val templateNo: TemplateNo,
    val templateId: TemplateId,
    val templateName: TemplateName,
    val memberId: MemberId? = null,
    val categoryId: CategoryId? = null,
    val shoppingType: ShoppingType? = null,
    val shoppingPayment: ShoppingPayment? = null,
    val shoppingSettlement: ShoppingSettlement? = null,
    val shoppingMinAmount: Amount? = null,
    val shoppingMaxAmount: Amount? = null,
    val shoppingRemarks: ShoppingRemarks? = null,
    val templateUseFlg: TemplateUseFlg,
    val templateDeletedFlg: TemplateDeleteFlg,
)





