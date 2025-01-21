package com.example.home.datasource.user

import com.example.home.domain.user.UserInfo
import com.example.home.domain.value_object.user.*
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsUserInfo
import com.example.home.infrastructure.persistence.repository.user.UserInfoRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class UserInfoRepositoryImpl : UserInfoRepository {
    override fun refer(userId: UserId): List<UserInfo> {
        return transaction {
            TbTsUserInfo.select(TbTsUserInfo.userId eq userId.value)
                .map {
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
                }
        }
    }

    override fun referAll(): List<UserInfo> {
        return transaction {
            TbTsUserInfo.selectAll()
                .map {
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
                }
        }
    }

    override fun save(
        userName: UserName,
        password: UserPassword,
        permission: UserPermission,
        approvalFlg: UserApprovalFlg,
        deleteFlg: UserDeleteFlg
    ): UserInfo {
        return transaction {
            TbTsUserInfo.insert {
                it[TbTsUserInfo.userName] = userName.value
                it[TbTsUserInfo.password] = password.value
                it[TbTsUserInfo.permission] = permission.value
                it[TbTsUserInfo.approvalFlg] = approvalFlg.value
                it[TbTsUserInfo.deleteFlg] = deleteFlg.value
                it[createDate] = LocalDateTime.now()
                it[updateDate] = LocalDateTime.now()
            }

            val userInfo = TbTsUserInfo.select {
                (TbTsUserInfo.userName eq userName.value) and
                        (TbTsUserInfo.password eq password.value)
            }.singleOrNull()

            return@transaction userInfo?.let {
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
            } ?: throw IllegalStateException("Failed to save the UserInfo")
        }
    }

    override fun update(
        userId: UserId,
        userName: UserName,
        password: UserPassword,
        permission: UserPermission,
        approvalFlg: UserApprovalFlg,
        deleteFlg: UserDeleteFlg
    ): Int {
        return transaction {

            val userRefer = refer(userId)
            val beforeApprovalFlg = userRefer.first().approvalFlg
            val beforeDeleteFlg = userRefer.first().deleteFlg

            val affectedRows = TbTsUserInfo.update({
                TbTsUserInfo.userId eq userId.value
            }) {
                it[TbTsUserInfo.userName] = userName.value
                it[TbTsUserInfo.password] = password.value
                it[TbTsUserInfo.permission] = permission.value
                it[TbTsUserInfo.approvalFlg] = approvalFlg.value
                it[TbTsUserInfo.deleteFlg] = deleteFlg.value
                it[updateDate] = LocalDateTime.now()
                if (beforeApprovalFlg.value == 0 && approvalFlg.value == 1) {
                    it[approvalDate] = LocalDateTime.now()
                }
                if (beforeDeleteFlg.value == 0 && deleteFlg.value == 1) {
                    it[deleteDate] = LocalDateTime.now()
                }
            }
            if (affectedRows == 0) {
                throw IllegalStateException("No rows updated for UserId: ${userId.value}")
            }
            return@transaction affectedRows
        }
    }

    override fun delete(userId: UserId): Int {
        return transaction {
            val deleteRows = TbTsUserInfo.deleteWhere { TbTsUserInfo.userId eq userId.value }

            if (deleteRows == 0) {
                throw IllegalStateException(
                    "No rows deleted for UserId: ${userId.value}"
                )
            }
            deleteRows
        }
    }
}