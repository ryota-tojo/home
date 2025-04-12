package com.example.home.service.group

import com.example.home.domain.entity.group.result.GroupInfoDeleteResult
import com.example.home.domain.entity.group.result.GroupInfoReferResult
import com.example.home.domain.entity.group.result.GroupInfoSaveResult
import com.example.home.domain.entity.group.result.GroupInfoUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.group.GroupInfoRepository
import com.example.home.domain.repository.user.UserInfoRepository
import com.example.home.domain.value_object.group.GroupApprovalFlg
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserLeaderFlg
import org.springframework.stereotype.Service

@Service
class GroupInfoControlService(
    private val groupInfoRepository: GroupInfoRepository,
    val userInfoRepository: UserInfoRepository,
) {
    fun refer(
        groupsId: GroupsId? = null,
        userId: UserId? = null,
        offset: Long? = null,
        limit: Int? = null,
    ): GroupInfoReferResult {
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
        // 存在しないユーザー
        val userInfo = userInfoRepository.refer(userId)
        if (userInfo == null) {
            return GroupInfoSaveResult(
                ResponseCode.存在しないユーザー.code,
                null
            )
        }

        // リーダーが存在する
        if (userLeaderFlg == UserLeaderFlg(1)) {
            val groupInfoRefer = groupInfoRepository.refer(groupsId)
            groupInfoRefer.forEach { groupInfo ->
                if (groupInfo.userLeaderFlg.value == 1) {
                    return GroupInfoSaveResult(
                        ResponseCode.既にリーダーが存在するグループ.code,
                        null
                    )
                }
            }
        }

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
        val groupInfoList = groupInfoRepository.refer(groupsId, userId)
        if (groupInfoList == null) {
            return GroupInfoDeleteResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        groupInfoList.forEach { groupInfo ->
            if (groupInfo.userLeaderFlg.value == 1) {
                return GroupInfoDeleteResult(
                    ResponseCode.既にリーダーが存在するグループ.code,
                    0
                )
            }
        }
        val deleteRows = groupInfoRepository.delete(groupsId, userId)
        if (deleteRows == 0) {
            return GroupInfoDeleteResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        return GroupInfoDeleteResult(
            ResponseCode.成功.code,
            deleteRows
        )
    }

}
