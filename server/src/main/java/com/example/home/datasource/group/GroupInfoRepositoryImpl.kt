package com.example.home.datasource.group

import com.example.home.domain.entity.group.GroupInfo
import com.example.home.domain.repository.group.GroupInfoRepository
import com.example.home.domain.value_object.group.GroupApprovalFlg
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserLeaderFlg
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsGroupInfo
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class GroupInfoRepositoryImpl : GroupInfoRepository {
    override fun refer(
        groupsId: GroupsId?,
        userId: UserId?,
        leader: UserLeaderFlg?,
        offset: Long?,
        limit: Int?
    ): List<GroupInfo> {
        return transaction {
            val conditions = mutableListOf<Op<Boolean>>()

            groupsId?.let { conditions.add(TbTsGroupInfo.groupsId eq it.value) }
            userId?.let { conditions.add(TbTsGroupInfo.userId eq it.value) }
            leader?.let { conditions.add(TbTsGroupInfo.leaderFlg eq it.value) }

            val query = if (conditions.isNotEmpty()) {
                TbTsGroupInfo.select { conditions.reduce { acc, op -> acc and op } }
            } else {
                TbTsGroupInfo.selectAll()
            }

            query
                .orderBy(TbTsGroupInfo.groupInfoId to SortOrder.ASC)
                .apply { limit?.let { limit(it, offset = offset ?: 0) } }
                .map {
                GroupInfo(
                    GroupsId(it[TbTsGroupInfo.groupsId]),
                    UserId(it[TbTsGroupInfo.userId]),
                    UserLeaderFlg(it[TbTsGroupInfo.leaderFlg]),
                    GroupApprovalFlg(it[TbTsGroupInfo.approvalFlg]),
                    it[TbTsGroupInfo.createDate],
                    it[TbTsGroupInfo.updateDate]
                )
            }
        }
    }

    override fun getGroupsId(userId: UserId): GroupsId? {
        return transaction {
            TbTsGroupInfo
                .select { TbTsGroupInfo.userId eq userId.value }
                .firstOrNull() // データがない場合は null を返す
                ?.let { row ->
                    GroupsId(row[TbTsGroupInfo.groupsId]) // groupsId を直接取得
                }
        }
    }

    override fun save(
        groupsId: GroupsId,
        userId: UserId,
        userLeaderFlg: UserLeaderFlg
    ): GroupInfo {
        return transaction {
            val approvalFlg = if (userLeaderFlg == UserLeaderFlg(1)) {
                1
            } else {
                0
            }
            TbTsGroupInfo.insert {
                it[TbTsGroupInfo.groupsId] = groupsId.value
                it[TbTsGroupInfo.userId] = userId.value
                it[leaderFlg] = userLeaderFlg.value
                it[TbTsGroupInfo.approvalFlg] = approvalFlg
                it[createDate] = LocalDateTime.now()
                it[updateDate] = LocalDateTime.now()
            }

            val groupInfo = TbTsGroupInfo.select {
                (TbTsGroupInfo.groupsId eq groupsId.value) and
                        (TbTsGroupInfo.userId eq userId.value)
            }.singleOrNull()

            return@transaction groupInfo?.let {
                GroupInfo(
                    GroupsId(it[TbTsGroupInfo.groupsId]),
                    UserId(it[TbTsGroupInfo.userId]),
                    UserLeaderFlg(it[TbTsGroupInfo.leaderFlg]),
                    GroupApprovalFlg(it[TbTsGroupInfo.approvalFlg]),
                    it[TbTsGroupInfo.createDate],
                    it[TbTsGroupInfo.updateDate]
                )
            } ?: throw IllegalStateException("Failed to save the GroupInfo")
        }
    }

    override fun update(
        groupsId: GroupsId,
        userId: UserId,
        userLeaderFlg: UserLeaderFlg?,
        groupApprovalFlg: GroupApprovalFlg?
    ): Int {
        return transaction {
            val affectedRows = TbTsGroupInfo.update({
                (TbTsGroupInfo.groupsId eq groupsId.value) and
                        (TbTsGroupInfo.userId eq userId.value)
            }) {
                var shouldUpdateDate = false

                if (userLeaderFlg != null) {
                    it[leaderFlg] = userLeaderFlg.value
                    shouldUpdateDate = true
                }
                groupApprovalFlg?.value?.let { value ->
                    it[approvalFlg] = value
                    shouldUpdateDate = true
                }
                if (shouldUpdateDate) {
                    it[updateDate] = LocalDateTime.now()
                }
            }
            return@transaction affectedRows
        }
    }

    override fun delete(groupsId: GroupsId?, userId: UserId?): Int {
        return transaction {
            when {
                groupsId == null && userId == null -> {
                    throw IllegalStateException(
                        "Deletion failed: Please provide either a valid groupsId or userId. Both are null."
                    )
                }

                userId != null -> {
                    TbTsGroupInfo.deleteWhere { TbTsGroupInfo.userId eq userId.value }
                }

                else -> {
                    TbTsGroupInfo.deleteWhere { TbTsGroupInfo.groupsId eq groupsId!!.value }
                }
            }
        }
    }
}