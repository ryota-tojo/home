package com.example.home.service.group

import com.example.home.domain.entity.group.GroupInfoAndUserInfo
import com.example.home.domain.entity.group.result.*
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.group.GroupInfoAndUserInfoRepository
import com.example.home.domain.repository.group.GroupInfoRepository
import com.example.home.domain.repository.user.UserInfoRepository
import com.example.home.domain.value_object.group.GroupApprovalFlg
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserLeaderFlg
import org.springframework.stereotype.Service

@Service
class GroupInfoAndUserInfoControlService(
    private val groupInfoAndUserInfoRepository: GroupInfoAndUserInfoRepository,
    val userInfoRepository: UserInfoRepository,
) {
    fun refer(
        groupsId: GroupsId? = null,
        userId: UserId? = null,
        leader: UserLeaderFlg? = null,
        offset: Long? = null,
        limit: Int? = null,
    ): GroupInfoAndUserInfoReferResult {
        val groupInfoAndUserInfoList = groupInfoAndUserInfoRepository.refer(groupsId, userId, leader, offset, limit)
        if (groupInfoAndUserInfoList.isNullOrEmpty()) {
            return GroupInfoAndUserInfoReferResult(
                ResponseCode.データ不在エラー.code,
                groupInfoAndUserInfoList
            )
        }
        return GroupInfoAndUserInfoReferResult(
            ResponseCode.成功.code,
            groupInfoAndUserInfoList
        )
    }
}
