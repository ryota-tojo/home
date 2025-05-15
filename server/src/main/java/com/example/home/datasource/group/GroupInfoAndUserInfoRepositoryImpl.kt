package com.example.home.datasource.group

import com.example.home.domain.entity.group.GroupInfoAndUserInfo
import com.example.home.domain.repository.group.GroupInfoAndUserInfoRepository
import com.example.home.domain.value_object.group.GroupApprovalFlg
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.*
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsGroupInfo
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsUserInfo
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class GroupInfoAndUserInfoRepositoryImpl : GroupInfoAndUserInfoRepository {
    override fun refer(
        groupsId: GroupsId?,
        userId: UserId?,
        leader: UserLeaderFlg?,
        offset: Long?,
        limit: Int?
    ): List<GroupInfoAndUserInfo> {
        return transaction {
            val conditions = mutableListOf<Op<Boolean>>()

            groupsId?.let { conditions.add(TbTsGroupInfo.groupsId eq it.value) }
            userId?.let { conditions.add(TbTsGroupInfo.userId eq it.value) }
            leader?.let { conditions.add(TbTsGroupInfo.leaderFlg eq it.value) }

            val baseTable = TbTsGroupInfo.innerJoin(TbTsUserInfo)

            val query = if (conditions.isNotEmpty()) {
                baseTable.select { conditions.reduce { acc, op -> acc and op } }
            } else {
                baseTable.selectAll()
            }

            query
                .orderBy(TbTsGroupInfo.groupInfoId to SortOrder.ASC)
                .apply { limit?.let { limit(it, offset ?: 0) } }
                .map {

                    GroupInfoAndUserInfo(
                        GroupsId(it[TbTsGroupInfo.groupsId]),
                        UserId(it[TbTsGroupInfo.userId]),
                        UserName(it[TbTsUserInfo.userName]),
                        UserPermission(it[TbTsUserInfo.permission]),
                        UserApprovalFlg(it[TbTsUserInfo.approvalFlg]),
                        UserDeleteFlg(it[TbTsUserInfo.deleteFlg]),
                        UserLeaderFlg(it[TbTsGroupInfo.leaderFlg]),
                        GroupApprovalFlg(it[TbTsGroupInfo.approvalFlg]),
                    )
                }

        }
    }
}