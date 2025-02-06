package com.example.home.service.user

import com.example.home.datasource.group.GroupInfoRepositoryImpl
import com.example.home.datasource.group.GroupListRepositoryImpl
import com.example.home.datasource.user.UserInfoRepositoryImpl
import com.example.home.datasource.user.UserSettingRepositoryImpl
import com.example.home.domain.entity.user.result.*
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.group.GroupInfoRepository
import com.example.home.domain.repository.group.GroupListRepository
import com.example.home.domain.repository.user.UserInfoRepository
import com.example.home.domain.repository.user.UserSettingRepository
import com.example.home.domain.value_object.TsDefaultData
import com.example.home.domain.value_object.group.GroupPassword
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.*
import com.example.home.util.ValidationCheck

class UserControlService(
    val userInfoRepository: UserInfoRepository = UserInfoRepositoryImpl(),
    val userSettingRepository: UserSettingRepository = UserSettingRepositoryImpl(),
    val groupListRepository: GroupListRepository = GroupListRepositoryImpl(),
    val groupInfoRepository: GroupInfoRepository = GroupInfoRepositoryImpl(),
) {
    fun refer(userId: UserId? = null, userName: UserName? = null): UserReferResult {
        if (userId == null && userName == null) {
            return UserReferResult(
                ResponseCode.キー未設定エラー.code,
                null,
                null,
                null
            )
        }
        if (userName != null) {
            if (!ValidationCheck.symbol(userName.toString()).result) {
                return UserReferResult(
                    ResponseCode.バリデーションエラー.code,
                    null,
                    null,
                    null
                )
            }
        }
        val userInfoList = userInfoRepository.refer(userId = userId, userName = userName)
        if (userInfoList.isNullOrEmpty()) {
            return UserReferResult(
                String.format(ResponseCode.成功_条件付き.code, "USER_NOT_FOUND"),
                null,
                null,
                null
            )
        }
        val userInfo = userInfoList.first()
        val userSetting = userSettingRepository.referAll(userInfo.userId)
        val groupsId = groupInfoRepository.getGroupsId(userInfo.userId)
        if (groupsId == null) {
            return UserReferResult(
                return UserReferResult(
                    String.format(ResponseCode.成功_条件付き.code, "NO_GROUP_AFFILIATION"),
                    userInfo,
                    userSetting,
                    null
                )
            )
        }
        val groupInfo = groupInfoRepository.refer(groupsId, userInfo.userId)
        return UserReferResult(
            ResponseCode.成功.code,
            userInfo,
            userSetting,
            groupInfo,
        )
    }

    fun referAll(): UserReferAllResult {
        val userInfo = userInfoRepository.referAll()
        if (userInfo == null) {
            return UserReferAllResult(
                String.format(ResponseCode.成功_条件付き.code, "USER_NOT_FOUND"),
                null
            )
        }
        return UserReferAllResult(
            ResponseCode.成功.code,
            userInfo
        )
    }

    fun save(
        groupsId: GroupsId,
        groupPassword: GroupPassword,
        userName: UserName,
        password: UserPassword,
        permission: UserPermission,
        approvalFlg: UserApprovalFlg,
        deleteFlg: UserDeleteFlg
    ): UserSaveResult {
        // バリデーションエラー
        if (!ValidationCheck.symbol(userName.toString()).result ||
            !ValidationCheck.symbol(password.toString()).result
        ) {
            return UserSaveResult(
                ResponseCode.バリデーションエラー.code,
                null,
                null,
                null
            )
        }
        // 既存のデータが存在しないか確認する
        val userReferResult = refer(userName = userName)
        if (userReferResult.userInfo != null) {
            return UserSaveResult(
                ResponseCode.重複エラー.code,
                null,
                null,
                null
            )
        }
        // グループ認証
        if (!groupListRepository.certification(groupsId, groupPassword)) {
            return UserSaveResult(
                ResponseCode.グループ認証エラー.code,
                null,
                null,
                null
            )
        }

        // ユーザー情報登録
        val createUser = userInfoRepository.save(
            userName,
            password,
            permission,
            approvalFlg,
            deleteFlg
        )

        // ユーザー設定登録
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

        // 所属グループ情報登録（グループに誰も登録されていない場合はleader）
        val getGroupInfo = groupInfoRepository.refer(groupsId, null)
        val leaderFlg = if (getGroupInfo == null) {
            1
        } else {
            0
        }
        val groupInfo = groupInfoRepository.save(
            groupsId,
            createUser.userId,
            UserLeaderFlg(leaderFlg)
        )

        return UserSaveResult(
            ResponseCode.成功.code,
            createUser,
            userSetting,
            groupInfo
        )
    }


    fun userInfoUpdate(
        userId: UserId,
        userName: UserName,
        password: UserPassword,
        permission: UserPermission,
        approvalFlg: UserApprovalFlg,
        deleteFlg: UserDeleteFlg
    ): UserUpdateResult {
        // バリデーションエラー
        if (!ValidationCheck.symbol(userName.toString()).result ||
            !ValidationCheck.symbol(password.toString()).result
        ) {
            return UserUpdateResult(
                ResponseCode.バリデーションエラー.code,
                0
            )
        }
        // 既存のデータが存在するか確認する
        val userReferResult = refer(userId = userId)
        if (userReferResult.userInfo == null) {
            return UserUpdateResult(
                ResponseCode.データ不在エラー.code,
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
        // バリデーションエラー
        if (!ValidationCheck.symbol(userSettingKey.toString()).result ||
            !ValidationCheck.symbol(userSettingValue.toString()).result
        ) {
            return UserUpdateResult(
                ResponseCode.バリデーションエラー.code,
                0
            )
        }

        // 既存のデータが存在するか確認する
        val userReferResult = userSettingRepository.refer(userId, userSettingKey)
        if (userReferResult.isEmpty()) {
            return UserUpdateResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }

        val updateRows = userSettingRepository.update(
            userId,
            userSettingKey,
            userSettingValue
        )
        return UserUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun delete(userId: UserId): UserDeleteResult {
        val groupsId = groupInfoRepository.getGroupsId(userId)
        if (groupsId == null) {
            return UserDeleteResult(
                ResponseCode.データ不正エラー.code,
                0
            )
        }
        val groupInfoReferResult = groupInfoRepository.refer(groupsId, userId).firstOrNull()
        if (groupInfoReferResult == null) {
            return UserDeleteResult(
                ResponseCode.データ不正エラー.code,
                0
            )
        }
        if (groupInfoReferResult.userLeaderFlg.value == 1) {
            return UserDeleteResult(
                ResponseCode.ユーザーエラー_グループリーダー削除.code,
                0
            )
        }

        groupInfoRepository.delete(userId = userId)
        userSettingRepository.delete(userId)
        val deletedRows = userInfoRepository.delete(userId)
        return UserDeleteResult(
            ResponseCode.成功.code,
            deletedRows
        )
    }

}