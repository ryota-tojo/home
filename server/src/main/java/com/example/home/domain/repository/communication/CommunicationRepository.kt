package com.example.home.domain.repository.communication

import com.example.home.domain.entity.communication.Communication
import com.example.home.domain.value_object.communication.*
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.group.GroupsId
import java.time.LocalDate

interface CommunicationRepository {
    fun refer(giftId: GiftId? = null): List<Communication>
    fun save(
        groupsId: GroupsId,
        giftDate: LocalDate,
        giftFrom: GiftFrom,
        giftTo: GiftTo,
        giftItem: GiftItem,
        giftAmount: Amount,
        giftRemarks: GiftRemarks,
        giftRtnDate: LocalDate,
        giftRtnItem: GiftItem,
        giftRtnAmount: Amount,
        giftRtnRemarks: GiftRemarks,
        returnFlg: GiftReturnFlg
    ): Communication

    fun update(
        giftId: GiftId,
        giftDate: LocalDate? = null,
        giftFrom: GiftFrom? = null,
        giftTo: GiftTo? = null,
        giftItem: GiftItem? = null,
        giftAmount: Amount? = null,
        giftRemarks: GiftRemarks? = null,
        giftRtnDate: LocalDate? = null,
        giftRtnItem: GiftItem? = null,
        giftRtnAmount: Amount? = null,
        giftRtnRemarks: GiftRemarks? = null,
        returnFlg: GiftReturnFlg? = null
    ): Int

    fun delete(groupsId: GroupsId? = null, giftId: GiftId? = null): Int
}