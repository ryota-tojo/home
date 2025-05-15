package com.example.home.service.user

import com.example.home.domain.entity.user.UserRefer
import com.example.home.domain.entity.user.result.UserDeleteResult
import com.example.home.domain.entity.user.result.UserReferResult
import com.example.home.domain.entity.user.result.UserSaveResult
import com.example.home.domain.entity.user.result.UserUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.group.GroupInfoRepository
import com.example.home.domain.repository.user.UserInfoRepository
import com.example.home.domain.repository.user.UserSettingRepository
import com.example.home.domain.value_object.TsDefaultData
import com.example.home.domain.value_object.user.*
import com.example.home.util.ValidationCheck
import org.springframework.stereotype.Service

@Service
class UserControlService(
    val userInfoRepository: UserInfoRepository,
    val userSettingRepository: UserSettingRepository,
    val groupInfoRepository: GroupInfoRepository,
) {
    fun refer(
        userId: UserId? = null,
        userName: UserName? = null,
        userPermission: UserPermission? = null,
        userApprovalFlg: UserApprovalFlg? = null,
        userDeleteFlg: UserDeleteFlg? = null,
        offset: Long? = null,
        limit: Int? = null,
    ): UserReferResult {

        // ユーザー情報を取得する
        val userInfoList = userInfoRepository.refer(
            userId = userId,
            userName = userName,
            userPermission = userPermission,
            userApprovalFlg = userApprovalFlg,
            userDeleteFlg = userDeleteFlg,
            offset = offset,
            limit = limit,
        )
        if (userInfoList.isNullOrEmpty()) {
            return UserReferResult(
                ResponseCode.データ不在エラー.code,
                null
            )
        }
        // ユーザー情報をループ
        val userRefer = userInfoList.map { userInfo ->
            val userSetting = userSettingRepository.refer(userInfo.userId)
            val groupsId = groupInfoRepository.getGroupsId(userInfo.userId)
            if (groupsId == null) {
                UserRefer(
                    userInfo,
                    userSetting,
                    null
                )
            }
            val groupInfo = groupInfoRepository.refer(groupsId, userInfo.userId)
            UserRefer(
                userInfo,
                userSetting,
                groupInfo
            )
        }
        return UserReferResult(
            ResponseCode.成功.code,
            userRefer
        )
    }

    fun save(
        userName: UserName,
        password: UserPassword,
        permission: UserPermission,
        approvalFlg: UserApprovalFlg,
        deleteFlg: UserDeleteFlg
    ): UserSaveResult {
        if (!ValidationCheck.symbol(userName.toString()).result ||
            !ValidationCheck.symbol(password.toString()).result
        ) {
            return UserSaveResult(
                ResponseCode.バリデーションエラー.code,
                null
            )
        }
        val isDuplication = userInfoRepository.isDuplication(userName)
        if (isDuplication) {
            return UserSaveResult(
                ResponseCode.重複エラー.code,
                null
            )
        }
        val createUser = userInfoRepository.save(
            userName,
            password,
            permission,
            approvalFlg,
            deleteFlg
        )
        val userDefaultSettings = mutableListOf<Pair<String, String>>()
        for ((key, value) in TsDefaultData.USER_SETTING) {
            userDefaultSettings.add(Pair(key, value))
        }
        val userSetting = userDefaultSettings.mapNotNull { (key, value) ->
            try {
                userSettingRepository.save(
                    createUser.userId,
                    UserSettingKey(key),
                    UserSettingValue(value)
                )
            } catch (e: Exception) {
                null
            }
        }

        return UserSaveResult(
            ResponseCode.成功.code,
            UserRefer(
                createUser,
                userSetting,
                null
            )
        )
    }

    fun userInfoUpdate(
        userId: UserId,
        userName: UserName? = null,
        password: UserPassword? = null,
        permission: UserPermission? = null,
        approvalFlg: UserApprovalFlg? = null,
        deleteFlg: UserDeleteFlg? = null
    ): UserUpdateResult {
        if (!ValidationCheck.symbol(userName.toString()).result ||
            !ValidationCheck.symbol(password.toString()).result
        ) {
            return UserUpdateResult(
                ResponseCode.バリデーションエラー.code,
                0
            )
        }
        val updateRows = userInfoRepository.update(
            userId,
            userName,
            password,
            permission,
            approvalFlg,
            deleteFlg
        )
        if (updateRows == 0) {
            return UserUpdateResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        return UserUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun userSettingUpdate(
        userId: UserId,
        userSettingKey: UserSettingKey,
        userSettingValue: UserSettingValue
    ): UserUpdateResult {
        if (!ValidationCheck.symbol(userSettingKey.toString()).result ||
            !ValidationCheck.symbol(userSettingValue.toString()).result
        ) {
            return UserUpdateResult(
                ResponseCode.バリデーションエラー.code,
                0
            )
        }
        val updateRows = userSettingRepository.update(
            userId,
            userSettingKey,
            userSettingValue
        )
        if (updateRows == 0) {
            return UserUpdateResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        return UserUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun delete(userId: UserId): UserDeleteResult {
        val groupsId = groupInfoRepository.getGroupsId(userId)

        if (groupsId != null) {
            val groupInfoReferResult = groupInfoRepository.refer(groupsId, userId)
            if (groupInfoReferResult == null) {
                return UserDeleteResult(
                    ResponseCode.データ不正エラー.code,
                    0
                )
            }
            if (groupInfoReferResult.first().userLeaderFlg.value == 1) {
                return UserDeleteResult(
                    ResponseCode.ユーザーエラー_グループリーダー削除.code,
                    0
                )
            }
            groupInfoRepository.delete(userId = userId)
        }

        userSettingRepository.delete(userId)
        val deleteRows = userInfoRepository.delete(userId)
        if (deleteRows == 0) {
            return UserDeleteResult(
                ResponseCode.データ不在エラー.code,
                deleteRows
            )
        }
        return UserDeleteResult(
            ResponseCode.成功.code,
            deleteRows
        )
    }
}