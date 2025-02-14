package com.example.home.service.shopping

import com.example.home.domain.entity.shopping.result.ShoppingDeleteResult
import com.example.home.domain.entity.shopping.result.ShoppingReferResult
import com.example.home.domain.entity.shopping.result.ShoppingSaveResult
import com.example.home.domain.entity.shopping.result.ShoppingUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.master.ChoicesRepository
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.repository.shopping.ShoppingRepository
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.FixedFlg
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.master.ChoicesItemNo
import com.example.home.domain.value_object.master.ChoicesItemType
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.shopping.*
import com.example.home.domain.value_object.user.UserId
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ShoppingService(
    private val shoppingRepository: ShoppingRepository,
    private val memberRepository: MemberRepository,
    private val categoryRepository: CategoryRepository,
    private val choicesRepository: ChoicesRepository,
) {
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
    ): ShoppingReferResult {
        val shoppingList = shoppingRepository.refer(
            id,
            groupsId,
            userId,
            shoppingDateYYYY,
            shoppingDateMM,
            memberId,
            categoryId,
            type,
            payment,
            settlement,
            minAmount,
            maxAmount,
            remarks
        )

        return ShoppingReferResult(
            ResponseCode.成功.code,
            shoppingList
        )
    }

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
    ): ShoppingSaveResult {
        val inputDataCheckResult = inputDataCheck(groupsId, memberId, categoryId, type, payment, settlement)
        if (inputDataCheckResult != ResponseCode.成功.code) {
            return ShoppingSaveResult(
                inputDataCheckResult,
                null
            )
        }

        val shopping = shoppingRepository.save(
            groupsId,
            userId,
            shoppingDate,
            memberId,
            categoryId,
            type,
            payment,
            settlement,
            amount,
            remarks
        )
        return ShoppingSaveResult(
            ResponseCode.成功.code,
            shopping
        )
    }

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
    ): Boolean {
        return shoppingRepository.isDuplication(
            groupsId,
            shoppingDate,
            memberId,
            categoryId,
            type,
            payment,
            settlement,
            amount,
            remarks
        )
    }

    fun update(
        shoppingId: ShoppingId,
        groupsId: GroupsId,
        userId: UserId,
        shoppingDate: LocalDate,
        memberId: MemberId,
        categoryId: CategoryId,
        type: ShoppingType,
        payment: ShoppingPayment,
        settlement: ShoppingSettlement,
        amount: Amount,
        remarks: ShoppingRemarks,
        fixedFlg: FixedFlg
    ): ShoppingUpdateResult {
        val inputDataCheckResult = inputDataCheck(groupsId, memberId, categoryId, type, payment, settlement)
        if (inputDataCheckResult != ResponseCode.成功.code) {
            return ShoppingUpdateResult(
                inputDataCheckResult,
                0
            )
        }

        val updateRows = shoppingRepository.update(
            shoppingId,
            groupsId,
            userId,
            shoppingDate,
            memberId,
            categoryId,
            type,
            payment,
            settlement,
            amount,
            remarks,
            fixedFlg
        )

        if (updateRows == 0) {
            return ShoppingUpdateResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        return ShoppingUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun delete(
        shoppingId: ShoppingId?,
        groupsId: GroupsId?,
        shoppingDateYYYY: YYYY?,
        shoppingDateMM: MM?,
    ): ShoppingDeleteResult {

        val deleteRows = shoppingRepository.delete(shoppingId, groupsId, shoppingDateYYYY, shoppingDateMM)
        if (deleteRows == 0) {
            return ShoppingDeleteResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        return ShoppingDeleteResult(
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
        val memberList = memberRepository.refer(null, groupsId, null)
        if (!memberList.any { it.id == memberId }) {
            return ResponseCode.存在しないメンバー.code
        }

        val categoryList = categoryRepository.refer(null, groupsId, null)
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