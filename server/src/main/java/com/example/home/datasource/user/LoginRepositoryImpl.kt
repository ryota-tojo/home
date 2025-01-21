package com.example.home.datasource.user

import com.example.home.domain.user.UserInfo
import com.example.home.domain.value_object.user.*
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsUserInfo
import com.example.home.infrastructure.persistence.repository.user.LoginRepository
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class LoginRepositoryImpl : LoginRepository {
    override fun refer(userName: UserName, password: UserPassword): UserInfo {
        return transaction {
            val userInfo = TbTsUserInfo.select {
                (TbTsUserInfo.userName eq userName.value) and
                        (TbTsUserInfo.password eq password.value)
            }.singleOrNull()

            userInfo?.let {
                UserInfo(
                    it[TbTsUserInfo.userId],
                    UserName(it[TbTsUserInfo.userName]),
                    UserPassword(it[TbTsUserInfo.password]),
                    UserPermission(it[TbTsUserInfo.permission]),
                    UserApprovalFlg(it[TbTsUserInfo.approvalFlg]),
                    UserDeleteFlg(it[TbTsUserInfo.deleteFlg]),
                    it[TbTsUserInfo.createDate],
                    it[TbTsUserInfo.updateDate],
                    it[TbTsUserInfo.approvalDate],
                    it[TbTsUserInfo.deleteDate]
                )

            } ?: throw IllegalStateException("User not found for userName: ${userName.value}")
        }
    }
}