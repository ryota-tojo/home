package com.example.home.service.communication

import com.example.home.domain.entity.communication.result.CommunicationDeleteResult
import com.example.home.domain.entity.communication.result.CommunicationReferResult
import com.example.home.domain.entity.communication.result.CommunicationSaveResult
import com.example.home.domain.entity.communication.result.CommunicationUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.communication.CommunicationRepository
import com.example.home.domain.value_object.communication.*
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.group.GroupsId
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CommunicationService(
    private val communicationRepository: CommunicationRepository
) {
    fun refer(giftId: GiftId? = null): CommunicationReferResult {
        val communicationList = communicationRepository.refer(giftId)
        return CommunicationReferResult(
            ResponseCode.成功.code,
            communicationList
        )
    }

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
    ): CommunicationSaveResult {
        val communication = communicationRepository.save(
            groupsId,
            giftDate,
            giftFrom,
            giftTo,
            giftItem,
            giftAmount,
            giftRemarks,
            giftRtnDate,
            giftRtnItem,
            giftRtnAmount,
            giftRtnRemarks,
            returnFlg
        )
        return CommunicationSaveResult(
            ResponseCode.成功.code,
            communication
        )
    }

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
    ): CommunicationUpdateResult {
        val updateRows = communicationRepository.update(
            giftId,
            giftDate,
            giftFrom,
            giftTo,
            giftItem,
            giftAmount,
            giftRemarks,
            giftRtnDate,
            giftRtnItem,
            giftRtnAmount,
            giftRtnRemarks,
            returnFlg
        )
        if (updateRows == 0) {
            return CommunicationUpdateResult(
                ResponseCode.データ不在エラー.code,
                updateRows
            )
        }
        return CommunicationUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun delete(groupsId: GroupsId? = null, giftId: GiftId? = null): CommunicationDeleteResult {
        val deleteRows = communicationRepository.delete(groupsId, giftId)
        if (deleteRows == 0) {
            return CommunicationDeleteResult(
                ResponseCode.データ不在エラー.code,
                deleteRows
            )
        }
        return CommunicationDeleteResult(
            ResponseCode.成功.code,
            deleteRows
        )
    }


}