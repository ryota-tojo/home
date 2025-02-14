package com.example.home.datasource.user

import com.example.home.domain.entity.user.UserInfo
import com.example.home.domain.repository.user.UserInfoRepository
import com.example.home.domain.value_object.user.*
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsBudgets
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsUserInfo
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class UserInfoRepositoryImpl : UserInfoRepository {
    override fun refer(userId: UserId?, userName: UserName?): List<UserInfo> {
        return transaction {

            if (userId == null && userName == null) {
                TbTsUserInfo.selectAll()
                    .map {
                        UserInfo(
                            UserId(it[TbTsUserInfo.userId]),
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
            } else if (userId != null) {
                TbTsUserInfo.select {
                    Op.build {
                        var condition: Op<Boolean> = Op.TRUE
                        userId.let { condition = TbTsUserInfo.userId eq it.value }
                        condition
                    }
                }.map {
                    UserInfo(
                        UserId(it[TbTsUserInfo.userId]),
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
            } else {
                TbTsUserInfo.select {
                    Op.build {
                        var condition: Op<Boolean> = Op.TRUE
                        userName?.let { condition = condition and (TbTsUserInfo.userName eq it.value) }
                        condition
                    }
                }.map {
                    UserInfo(
                        UserId(it[TbTsUserInfo.userId]),
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
    }

    override fun save(
        userName: UserName,
        password: UserPassword,
        permission: UserPermission,
        approvalFlg: UserApprovalFlg,
        deleteFlg: UserDeleteFlg
    ): UserInfo {
        return transaction {
            val id = TbTsUserInfo.insert {
                it[TbTsUserInfo.userName] = userName.value
                it[TbTsUserInfo.password] = password.value
                it[TbTsUserInfo.permission] = permission.value
                it[TbTsUserInfo.approvalFlg] = approvalFlg.value
                it[TbTsUserInfo.deleteFlg] = deleteFlg.value
                it[createDate] = LocalDateTime.now()
                it[updateDate] = LocalDateTime.now()
            }.resultedValues?.firstOrNull()?.get(TbTsUserInfo.userId)

            if (id == null || id == 0) {
                throw IllegalStateException("データの挿入に失敗しました")
            }

            val userInfoList = refer(UserId(id))
            userInfoList.single()

        }
    }

    override fun isDuplication(userName: UserName): Boolean {
        return transaction {
            val userInfo = TbTsUserInfo.select {
                Op.build {
                    var condition: Op<Boolean> = Op.TRUE
                    userName.let { condition = TbTsUserInfo.userName eq it.value }
                    condition
                }
            }.map {
                UserInfo(
                    UserId(it[TbTsUserInfo.userId]),
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
            return@transaction userInfo.isEmpty()
        }
    }

    override fun update(
        userId: UserId,
        userName: UserName?,
        password: UserPassword?,
        permission: UserPermission?,
        approvalFlg: UserApprovalFlg?,
        deleteFlg: UserDeleteFlg?
    ): Int {
        return transaction {
            var condition: Op<Boolean> = TbTsUserInfo.userId eq userId.value
            val updateRows = TbTsUserInfo.update({
                condition
            }) {
                if (userName != null) {
                    it[TbTsUserInfo.userName] = userName.value
                }
                if (password != null) {
                    it[TbTsUserInfo.password] = password.value
                }
                if (permission != null) {
                    it[TbTsUserInfo.permission] = permission.value
                }
                if (approvalFlg != null) {
                    it[TbTsUserInfo.approvalFlg] = approvalFlg.value
                    it[TbTsUserInfo.approvalDate] = LocalDateTime.now()
                }
                if (deleteFlg != null) {
                    it[TbTsUserInfo.deleteFlg] = deleteFlg.value
                    it[TbTsUserInfo.deleteDate] = LocalDateTime.now()
                }
                it[updateDate] = LocalDateTime.now()
            }
            return@transaction updateRows
        }
    }

    override fun delete(userId: UserId): Int {
        return transaction {
            val deleteRows = TbTsUserInfo.deleteWhere { TbTsUserInfo.userId eq userId.value }
            deleteRows
        }
    }
}