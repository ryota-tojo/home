package com.example.home.service.group

import com.example.home.domain.entity.group.result.*
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
        leader: UserLeaderFlg? = null,
        offset: Long? = null,
        limit: Int? = null,
    ): GroupInfoReferResult {
        val groupInfoList = groupInfoRepository.refer(groupsId, userId, leader, offset, limit)
        if (groupInfoList.isNullOrEmpty()) {
            return GroupInfoReferResult(
                ResponseCode.データ不在エラー.code,
                groupInfoList
            )
        }
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
    fun leaderChange(
        groupsId: GroupsId,
        leaderUserId: UserId,
        newLeaderUserId: UserId,
    ): GroupInfoLeaderChangeResult {
        val leaderUser = refer(groupsId,leaderUserId).groupInfoList
        val newLeaderUser = refer(groupsId,newLeaderUserId).groupInfoList

        // リーダー存在チェック
        if(leaderUser.isNullOrEmpty()) {
            return GroupInfoLeaderChangeResult(
                ResponseCode.存在しないユーザー.code,
                leaderUserId,
                newLeaderUserId
            )
        }
        // 新リーダー存在チェック
        if(newLeaderUser.isNullOrEmpty()) {
            return GroupInfoLeaderChangeResult(
                ResponseCode.存在しないユーザー.code,
                leaderUserId,
                newLeaderUserId
            )
        }
        // リーダー：リーダーフラグチェック
        if(leaderUser.first().userLeaderFlg.value!=1){
            return GroupInfoLeaderChangeResult(
                ResponseCode.リーダー以外のユーザー.code,
                leaderUserId,
                newLeaderUserId
            )
        }
        // リーダー：承認フラグチェック
        if(leaderUser.first().groupApprovalFlg.value==0){
            return GroupInfoLeaderChangeResult(
                ResponseCode.未承認のユーザー.code,
                leaderUserId,
                newLeaderUserId
            )
        }
        // 新リーダー：リーダーフラグチェック
        if(newLeaderUser.first().userLeaderFlg.value!=0){
            return GroupInfoLeaderChangeResult(
                ResponseCode.メンバー以外のユーザー.code,
                leaderUserId,
                newLeaderUserId
            )
        }
        // 新リーダー：承認フラグチェック
        if(newLeaderUser.first().groupApprovalFlg.value==0){
            return GroupInfoLeaderChangeResult(
                ResponseCode.未承認のユーザー.code,
                leaderUserId,
                newLeaderUserId
            )
        }
        val updateResultLeader = update(groupsId,leaderUserId, UserLeaderFlg(0))
        if(updateResultLeader.updateRows == 0){
            return GroupInfoLeaderChangeResult(
                ResponseCode.グループ情報の更新に失敗.code,
                leaderUserId,
                newLeaderUserId
            )
        }
        val updateResultNewLeader = update(groupsId,newLeaderUserId,UserLeaderFlg(1))
        if(updateResultNewLeader.updateRows == 0){
            update(groupsId,leaderUserId, UserLeaderFlg(1))
            return GroupInfoLeaderChangeResult(
                ResponseCode.グループ情報の更新に失敗.code,
                leaderUserId,
                newLeaderUserId
            )
        }
        return GroupInfoLeaderChangeResult(
            ResponseCode.成功.code,
            leaderUserId,
            newLeaderUserId
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
