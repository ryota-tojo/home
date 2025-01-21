package com.example.home.service.user

import com.example.home.datasource.group.GroupInfoRepositoryImpl
import com.example.home.datasource.user.LoginRepositoryImpl
import com.example.home.domain.user.LoginInfo
import com.example.home.domain.user.result.UserLoginResult
import com.example.home.domain.value_object.user.UserId
import com.example.home.infrastructure.persistence.repository.group.GroupInfoRepository
import com.example.home.infrastructure.persistence.repository.user.LoginRepository
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val groupInfoRepository: GroupInfoRepository = GroupInfoRepositoryImpl(),
    private val loginRepository: LoginRepository = LoginRepositoryImpl()
) {
    fun login(loginInfo: LoginInfo): UserLoginResult {
        // ユーザー情報を取得する
        val userInfo = loginRepository.refer(loginInfo.username, loginInfo.password)

        // ユーザー名、パスワードを照合する
        if (userInfo == null) {
            return UserLoginResult("COLLATION_ERROR", false)
        }

        // ユーザーの削除状態を確認する
        if (userInfo.deleteFlg.value == 1) {
            return UserLoginResult("USER_DELETE_ERROR", false)
        }

        // ユーザーの承認状態を確認する
        if (userInfo.approvalFlg.value != 1) {
            return UserLoginResult("USER_APPROVAL_ERROR", false)
        }

        // グループの所属を確認する
        val groupInfo = groupInfoRepository.refer(loginInfo.groupsId, UserId(userInfo.userId))
        if (groupInfo.isEmpty()) {
            return UserLoginResult("GROUP_AFFILIATION_ERROR", false)
        }

        return UserLoginResult("SUCCESS", true)

    }
}