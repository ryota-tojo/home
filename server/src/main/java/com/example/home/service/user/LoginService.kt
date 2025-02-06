package com.example.home.service.user

import com.example.home.datasource.group.GroupInfoRepositoryImpl
import com.example.home.datasource.user.LoginRepositoryImpl
import com.example.home.domain.entity.user.LoginInfo
import com.example.home.domain.entity.user.result.UserLoginResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.group.GroupInfoRepository
import com.example.home.domain.repository.user.LoginRepository
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
            return UserLoginResult(ResponseCode.ログインエラー_データ照合.code, false)
        }

        // ユーザーの削除状態を確認する
        if (userInfo.deleteFlg.value == 1) {
            return UserLoginResult(ResponseCode.ログインエラー_削除済ユーザー.code, false)
        }

        // ユーザーの承認状態を確認する
        if (userInfo.approvalFlg.value != 1) {
            return UserLoginResult(ResponseCode.ログインエラー_未承認ユーザー.code, false)
        }

        // グループの所属を確認する
        val groupInfo = groupInfoRepository.refer(loginInfo.groupsId, userInfo.userId)
        if (groupInfo.isEmpty()) {
            return UserLoginResult(ResponseCode.ログインエラー_所属グループ不正.code, false)
        }

        return UserLoginResult(ResponseCode.成功.code, true)

    }
}