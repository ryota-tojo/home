package com.example.home.service.group

import com.example.home.domain.entity.group.result.*
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.group.GroupInfoRepository
import com.example.home.domain.value_object.group.*
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserLeaderFlg
import org.springframework.stereotype.Service

@Service
class GroupInfoControlService(
    private val groupInfoRepository: GroupInfoRepository,
) {
    fun refer(groupsId: GroupsId? = null, userId: UserId? = null): GroupInfoReferResult {
        val groupInfoList = groupInfoRepository.refer(groupsId, userId)
        return GroupInfoReferResult(
            ResponseCode.成功.code,
            groupInfoList
        )
    }

    fun save(
        groupsId: GroupsId,
        userId: UserId,
        userLeaderFlg: UserLeaderFlg
    ): GroupInfoSaveResult {
        val groupInfo = groupInfoRepository.save(groupsId, userId, userLeaderFlg)
        return GroupInfoSaveResult(
            ResponseCode.成功.code,
            groupInfo
        )
    }

    fun update(
        groupsId: GroupsId,
        userId: UserId,
        userLeaderFlg: UserLeaderFlg? = null,
        groupApprovalFlg: GroupApprovalFlg? = null
    ): GroupInfoUpdateResult {
        val updateRows = groupInfoRepository.update(groupsId, userId, userLeaderFlg, groupApprovalFlg)
        if (updateRows == 0) {
            return GroupInfoUpdateResult(
                ResponseCode.データ不在エラー.code,
                updateRows
            )
        }
        return GroupInfoUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun delete(groupsId: GroupsId? = null, userId: UserId? = null): GroupInfoDeleteResult {
        val deleteRows = groupInfoRepository.delete(groupsId, userId)
        if (deleteRows == 0) {
            return GroupInfoDeleteResult(
                ResponseCode.データ不在エラー.code,
                deleteRows
            )
        }
        return GroupInfoDeleteResult(
            ResponseCode.成功.code,
            deleteRows
        )
    }

}
