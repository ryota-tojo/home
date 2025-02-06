package com.example.home.domain.entity.shopping

import com.example.home.domain.value_object.category.CategoryName
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.FixedFlg
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberName
import com.example.home.domain.value_object.shopping.*
import java.time.LocalDate

data class Shopping(
    val id: ShoppingId,
    val groupsId :GroupsId,
    val shoppingDate: LocalDate,
    val memberName: MemberName,
    val categoryName: CategoryName,
    val shoppingType: ShoppingType,
    val shoppingPayment: ShoppingPayment,
    val shoppingSettlement: ShoppingSettlement,
    val shoppingAmount: Amount,
    val shoppingRemarks: ShoppingRemarks,
    val fixedFlg: FixedFlg,
)