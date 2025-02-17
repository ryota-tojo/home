package com.example.home.domain.repository.shopping

import com.example.home.domain.entity.shopping.Shopping
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.FixedFlg
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.shopping.*
import com.example.home.domain.value_object.user.UserId
import java.time.LocalDate

interface ShoppingRepository {
    fun refer(
        id: ShoppingId? = null,
        groupsId: GroupsId? = null,
        userId: UserId? = null,
        shoppingDateYYYY: YYYY? = null,
        shoppingDateMM: MM? = null,
        memberId: MemberId? = null,
        categoryId: CategoryId? = null,
        type: ShoppingType? = null,
        payment: ShoppingPayment? = null,
        settlement: ShoppingSettlement? = null,
        minAmount: Amount? = null,
        maxAmount: Amount? = null,
        remarks: ShoppingRemarks? = null
    ): List<Shopping>

    fun getOldCategories(
        groupsId: GroupsId,
        shoppingDateYYYY: YYYY? = null,
        shoppingDateMM: MM? = null,
    ): List<CategoryId>

    fun save(
        groupsId: GroupsId,
        userId: UserId,
        shoppingDate: LocalDate,
        memberId: MemberId,
        categoryId: CategoryId,
        type: ShoppingType,
        payment: ShoppingPayment,
        settlement: ShoppingSettlement,
        amount: Amount,
        remarks: ShoppingRemarks
    ): Shopping

    fun isDuplication(
        groupsId: GroupsId,
        shoppingDate: LocalDate,
        memberId: MemberId,
        categoryId: CategoryId,
        type: ShoppingType,
        payment: ShoppingPayment,
        settlement: ShoppingSettlement,
        amount: Amount,
        remarks: ShoppingRemarks
    ): Boolean

    fun update(
        shoppingId: ShoppingId,
        groupsId: GroupsId,
        userId: UserId? = null,
        shoppingDate: LocalDate? = null,
        memberId: MemberId? = null,
        categoryId: CategoryId? = null,
        type: ShoppingType? = null,
        payment: ShoppingPayment? = null,
        settlement: ShoppingSettlement? = null,
        amount: Amount? = null,
        remarks: ShoppingRemarks? = null,
        fixedFlg: FixedFlg? = null
    ): Int

    fun fixed(
        groupsId: GroupsId,
        shoppingDateYYYY: YYYY,
        shoppingDateMM: MM
    ): Int

    fun unFixed(
        groupsId: GroupsId,
        shoppingDateYYYY: YYYY,
        shoppingDateMM: MM
    ): Int

    fun delete(
        shoppingId: ShoppingId? = null,
        groupsId: GroupsId? = null,
        shoppingDateYYYY: YYYY? = null,
        shoppingDateMM: MM? = null,
    ): Int
}
