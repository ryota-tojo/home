package com.example.home.service.shopping

import com.example.home.datasource.category.CategoryRepositoryImpl
import com.example.home.datasource.master.ChoicesRepositoryImpl
import com.example.home.datasource.member.MemberRepositoryImpl
import com.example.home.datasource.shopping.ShoppingRepositoryImpl
import com.example.home.domain.entity.shopping.result.ShoppingDeleteResult
import com.example.home.domain.entity.shopping.result.ShoppingReferResult
import com.example.home.domain.entity.shopping.result.ShoppingSaveResult
import com.example.home.domain.entity.shopping.result.ShoppingUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.master.ChoicesRepository
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.repository.shopping.ShoppingRepository
import com.example.home.domain.value_object.category.CategoryNo
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.FixedFlg
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.master.ChoicesItemNo
import com.example.home.domain.value_object.master.ChoicesItemType
import com.example.home.domain.value_object.member.MemberNo
import com.example.home.domain.value_object.shopping.*
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ShoppingService(
    private val shoppingRepository: ShoppingRepository = ShoppingRepositoryImpl(),
    private val memberRepository: MemberRepository = MemberRepositoryImpl(),
    private val categoryRepository: CategoryRepository = CategoryRepositoryImpl(),
    private val choicesRepository: ChoicesRepository = ChoicesRepositoryImpl()
) {
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
    ): ShoppingReferResult {
        val shoppingList = shoppingRepository.refer(
            groupsId,
            shoppingDateYYYY,
            shoppingDateMM,
            memberNo,
            categoryNo,
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
        shoppingDate: LocalDate,
        memberNo: MemberNo,
        categoryNo: CategoryNo,
        type: ShoppingType,
        payment: ShoppingPayment,
        settlement: ShoppingSettlement,
        amount: Amount,
        remarks: ShoppingRemarks
    ): ShoppingSaveResult {

        val inputDataCheckResult = inputDataCheck(groupsId, memberNo, categoryNo, type, payment, settlement)
        if (inputDataCheckResult != ResponseCode.成功.code) {
            return ShoppingSaveResult(
                inputDataCheckResult,
                null
            )
        }

        val shopping = shoppingRepository.save(
            groupsId,
            shoppingDate,
            memberNo,
            categoryNo,
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
        memberNo: MemberNo,
        categoryNo: CategoryNo,
        type: ShoppingType,
        payment: ShoppingPayment,
        settlement: ShoppingSettlement,
        amount: Amount,
        remarks: ShoppingRemarks
    ): Boolean {
        return shoppingRepository.isDuplication(
            groupsId,
            shoppingDate,
            memberNo,
            categoryNo,
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
        shoppingDate: LocalDate,
        memberNo: MemberNo,
        categoryNo: CategoryNo,
        type: ShoppingType,
        payment: ShoppingPayment,
        settlement: ShoppingSettlement,
        amount: Amount,
        remarks: ShoppingRemarks,
        fixedFlg: FixedFlg
    ): ShoppingUpdateResult {
        val inputDataCheckResult = inputDataCheck(groupsId, memberNo, categoryNo, type, payment, settlement)
        if (inputDataCheckResult != ResponseCode.成功.code) {
            return ShoppingUpdateResult(
                inputDataCheckResult,
                0
            )
        }

        val updateRows = shoppingRepository.update(
            shoppingId,
            groupsId,
            shoppingDate,
            memberNo,
            categoryNo,
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
        memberNo: MemberNo,
        categoryNo: CategoryNo,
        type: ShoppingType,
        payment: ShoppingPayment,
        settlement: ShoppingSettlement,
    ): String {
        val memberList = memberRepository.refer(groupsId)
        if (!memberList.any { it.memberNo == memberNo }) {
            return ResponseCode.存在しないメンバー.code
        }

        val categoryList = categoryRepository.refer(groupsId)
        if (!categoryList.any { it.categoryNo == categoryNo }) {
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