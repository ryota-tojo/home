package com.example.home.datasource.group

import com.example.home.domain.group.GroupInfo
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserLeaderFlg
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsGroupInfo
import com.example.home.infrastructure.persistence.repository.group.GroupInfoRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class GroupInfoRepositoryImpl : GroupInfoRepository {
    override fun refer(groupsId: GroupsId, userId: UserId?): List<GroupInfo> {
        val condition = TbTsGroupInfo.groupsId eq groupsId.value

        val fullCondition = when {
            userId != null ->
                condition and (TbTsGroupInfo.userId eq userId.value)

            else ->
                condition
        }

        return transaction {
            TbTsGroupInfo.select(fullCondition)
                .map {
                    GroupInfo(
                        GroupsId(it[TbTsGroupInfo.groupsId]),
                        UserId(it[TbTsGroupInfo.userId]),
                        UserLeaderFlg(it[TbTsGroupInfo.leaderFlg]),
                        it[TbTsGroupInfo.createDate],
                        it[TbTsGroupInfo.updateDate],
                    )
                }
        }
    }

    override fun referAll(): List<GroupInfo> {
        return transaction {
            TbTsGroupInfo.selectAll()
                .map {
                    GroupInfo(
                        GroupsId(it[TbTsGroupInfo.groupsId]),
                        UserId(it[TbTsGroupInfo.userId]),
                        UserLeaderFlg(it[TbTsGroupInfo.leaderFlg]),
                        it[TbTsGroupInfo.createDate],
                        it[TbTsGroupInfo.updateDate],
                    )
                }
        }
    }

    override fun save(
        groupsId: GroupsId,
        userId: UserId,
        userLeaderFlg: UserLeaderFlg
    ): GroupInfo {
        return transaction {
            TbTsGroupInfo.insert {
                it[TbTsGroupInfo.groupsId] = groupsId.value
                it[TbTsGroupInfo.userId] = userId.value
                it[leaderFlg] = userLeaderFlg.value
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
                    it[TbTsGroupInfo.createDate],
                    it[TbTsGroupInfo.updateDate]
                )
            } ?: throw IllegalStateException("Failed to save the GroupInfo")
        }
    }

    override fun update(
        groupsId: GroupsId,
        userId: UserId,
        userLeaderFlg: UserLeaderFlg
    ): Int {
        return transaction {
            val affectedRows = TbTsGroupInfo.update({
                (TbTsGroupInfo.groupsId eq groupsId.value) and
                        (TbTsGroupInfo.userId eq userId.value)
            }) {
                it[leaderFlg] = userLeaderFlg.value
                it[updateDate] = LocalDateTime.now()
            }
            if (affectedRows == 0) {
                throw IllegalStateException("No rows updated for groupsId: ${groupsId.value}, userId: ${userId.value}")
            }
            return@transaction affectedRows
        }
    }

    override fun delete(userId: UserId): Int {
        return transaction {
            val deleteRows = TbTsGroupInfo.deleteWhere { TbTsGroupInfo.userId eq userId.value }

            if (deleteRows == 0) {
                throw IllegalStateException(
                    "No rows deleted for userId: ${userId.value}}"
                )
            }
            deleteRows
        }
    }
}