package com.example.home.domain.communication

import com.example.home.domain.value_object.communication.*
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.group.GroupsId
import java.time.LocalDate

data class Communication(
    val id: GiftId,
    val groups_id: GroupsId,
    val giftDate : LocalDate,
    val giftFrom : GiftFrom,
    val giftTo: GiftTo,
    val giftItem: GiftItem,
    val giftAmount: Amount,
    val giftRemarks: GiftRemarks,
    val giftRtnDate :LocalDate,
    val giftRtnItem:GiftItem,
    val giftRtnAmount :Amount,
    val giftRtnRemarks : GiftRemarks,
    val returnFlg :GiftReturnFlg,
)