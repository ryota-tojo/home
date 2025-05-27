package com.example.home.datasource.user

import com.example.home.domain.entity.user.UserInfo
import com.example.home.domain.repository.user.UserInfoRepository
import com.example.home.domain.value_object.user.*
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsGroupInfo
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsUserInfo
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull

@Repository
class UserInfoRepositoryImpl : UserInfoRepository {
    override fun refer(
        userId: UserId?,
        userName: UserName?,
        userPermission: UserPermission?,
        userApprovalFlg: UserApprovalFlg?,
        userDeleteFlg: UserDeleteFlg?,
        requestGroupAffiliation:Int?,
        offset: Long?,
        limit: Int?
    ): List<UserInfo> {
        return transaction {
            // JOIN 先を決定
            val joinedTable = when (requestGroupAffiliation) {
                1 -> TbTsUserInfo innerJoin TbTsGroupInfo
                0 -> TbTsUserInfo leftJoin TbTsGroupInfo
                else -> TbTsUserInfo
            }

            // グループ所属条件（requestGroupAffiliation に応じて）
            val baseCondition = when (requestGroupAffiliation) {
                0 -> TbTsGroupInfo.userId.isNull()
                else -> null
            }

            // ユーザー検索条件
            val additionalCondition = buildList<Op<Boolean>> {
                userId?.let { add(TbTsUserInfo.userId eq it.value) }
                userName?.let { add(TbTsUserInfo.userName like "%${it.value}%") }
                userPermission?.let { add(TbTsUserInfo.permission eq it.value) }
                userApprovalFlg?.let { add(TbTsUserInfo.approvalFlg eq it.value) }
                userDeleteFlg?.let { add(TbTsUserInfo.deleteFlg eq it.value) }
            }.reduceOrNull { acc, op -> acc and op }

            // すべての条件を AND でまとめる
            val whereCondition = when {
                baseCondition != null && additionalCondition != null -> baseCondition and additionalCondition
                baseCondition != null -> baseCondition
                else -> additionalCondition
            }

            // クエリ実行
            val query = if (whereCondition != null) {
                joinedTable.select { whereCondition }
            } else {
                joinedTable.selectAll()
            }

            query
                .apply { limit?.let { limit(it, offset ?: 0) } }
                .orderBy(TbTsUserInfo.updateDate, SortOrder.DESC)
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
            return@transaction userInfo.isNotEmpty()
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
            val userRecord = TbTsUserInfo
                .select { TbTsUserInfo.userId eq userId.value }
                .singleOrNull()

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
                    if(userRecord!=null){
                        if (userRecord[TbTsUserInfo.approvalFlg]!=1 && approvalFlg.value == 1) {
                            it[approvalDate] = LocalDateTime.now()
                        }
                    }
                }
                if (deleteFlg != null) {
                    it[TbTsUserInfo.deleteFlg] = deleteFlg.value
                    if(userRecord!=null) {
                        if (userRecord[TbTsUserInfo.deleteFlg] != 1 && deleteFlg.value == 1) {
                            it[deleteDate] = LocalDateTime.now()
                        }
                    }
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