package com.example.home.domain.entity.shopping

import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.FixedFlg
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.shopping.*
import com.example.home.domain.value_object.user.UserId
import java.time.LocalDate

data class Shopping(
    val id: ShoppingId,
    val groupsId: GroupsId,
    val userId: UserId,
    val shoppingDate: LocalDate,
    val memberId: MemberId,
    val categoryId: CategoryId,
    val shoppingType: ShoppingType,
    val shoppingPayment: ShoppingPayment,
    val shoppingSettlement: ShoppingSettlement,
    val shoppingAmount: Amount,
    val shoppingRemarks: ShoppingRemarks,
    val fixedFlg: FixedFlg,
)