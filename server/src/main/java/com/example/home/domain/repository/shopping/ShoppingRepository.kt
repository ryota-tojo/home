package com.example.home.domain.repository.shopping

import com.example.home.domain.entity.shopping.Shopping
import com.example.home.domain.value_object.category.CategoryNo
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.FixedFlg
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberNo
import com.example.home.domain.value_object.shopping.*
import java.time.LocalDate

interface ShoppingRepository {
    fun refer(
        groupsId: GroupsId,
        shoppingDateYYYY: YYYY? = null,
        shoppingDateMM: MM? = null,
        memberNo: MemberNo? = null,
        categoryNo: CategoryNo? = null,
        type: ShoppingType? = null,
        payment: ShoppingPayment? = null,
        settlement: ShoppingSettlement? = null,
        minAmount: Amount? = null,
        maxAmount: Amount? = null,
        remarks: ShoppingRemarks? = null
    ): List<Shopping>

    fun save(
        groupsId: GroupsId,
        shoppingDate: LocalDate,
        memberNo: MemberNo,
        categoryNo: CategoryNo,
        type: ShoppingType,
        payment: ShoppingPayment,
        settlement: ShoppingSettlement,
        amount: Amount,
        remarks: ShoppingRemarks
    ): Shopping

    fun isDuplication(
        groupsId: GroupsId,
        shoppingDate: LocalDate,
        memberNo: MemberNo,
        categoryNo: CategoryNo,
        type: ShoppingType,
        payment: ShoppingPayment,
        settlement: ShoppingSettlement,
        amount: Amount,
        remarks: ShoppingRemarks
    ): Boolean

    fun update(
        shoppingId: ShoppingId,
        groupsId: GroupsId,
        shoppingDate: LocalDate,
        memberNo: MemberNo,
        categoryNo: CategoryNo,
        type: ShoppingType,
        payment: ShoppingPayment,
        settlement: ShoppingSettlement,
        amount: Amount,
        remarks: ShoppingRemarks,
        fixedFlg: FixedFlg
    ): Int

    fun shoppingDatafixed(
        groupsId: GroupsId,
        shoppingDateYYYY: YYYY,
        shoppingDateMM: MM,
        fixedFlg: FixedFlg
    ): Int

    fun delete(
        shoppingId: ShoppingId? = null,
        groupsId: GroupsId? = null,
        shoppingDateYYYY: YYYY? = null,
        shoppingDateMM: MM? = null,
    ): Int
}
