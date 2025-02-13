package com.example.home.datasource.group

import com.example.home.domain.entity.group.GroupList
import com.example.home.domain.repository.group.GroupListRepository
import com.example.home.domain.value_object.group.GroupName
import com.example.home.domain.value_object.group.GroupPassword
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsGroupList
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class GroupListRepositoryImpl : GroupListRepository {
    override fun refer(groupsId: GroupsId?): List<GroupList> {
        return transaction {
            TbTsGroupList.select {
                Op.build {
                    var condition: Op<Boolean> = Op.TRUE
                    groupsId?.let { condition = TbTsGroupList.groupsId eq it.value }
                    condition
                }
            }.map {
                GroupList(
                    it[TbTsGroupList.groupListId],
                    GroupsId(it[TbTsGroupList.groupsId]),
                    GroupName(it[TbTsGroupList.groupName]),
                    GroupPassword(it[TbTsGroupList.groupPassword])
                )
            }
        }
    }

    override fun certification(groupsId: GroupsId, groupPassword: GroupPassword): Boolean {
        return transaction {
            TbTsGroupList.select {
                (TbTsGroupList.groupsId eq groupsId.value) and
                        (TbTsGroupList.groupPassword eq groupPassword.value)
            }.any()
        }

        return true
    }

    override fun save(
        groupsId: GroupsId,
        groupName: GroupName,
        groupPassword: GroupPassword
    ): GroupList {
        return transaction {
            TbTsGroupList.insert {
                it[TbTsGroupList.groupsId] = groupsId.value
                it[TbTsGroupList.groupName] = groupName.value
                it[TbTsGroupList.groupPassword] = groupPassword.value
            }

            val group = TbTsGroupList.select {
                (TbTsGroupList.groupsId eq groupsId.value) and
                        (TbTsGroupList.groupName eq groupName.value) and
                        (TbTsGroupList.groupPassword eq groupPassword.value)
            }.singleOrNull()

            return@transaction group?.let {
                GroupList(
                    it[TbTsGroupList.groupListId],
                    GroupsId(it[TbTsGroupList.groupsId]),
                    GroupName(it[TbTsGroupList.groupName]),
                    GroupPassword(it[TbTsGroupList.groupPassword])
                )
            } ?: throw IllegalStateException("Failed to save the GroupList")
        }
    }

    override fun update(
        groupsId: GroupsId,
        groupName: GroupName?,
        groupPassword: GroupPassword?
    ): Int {
        return transaction {
            val affectedRows = TbTsGroupList.update({ TbTsGroupList.groupsId eq groupsId.value }) {
                if (groupName != null) {
                    it[TbTsGroupList.groupName] = groupName.value
                }
                if (groupPassword != null) {
                    it[TbTsGroupList.groupPassword] = groupPassword.value
                }
            }
            if (affectedRows == 0) {
                throw IllegalStateException("No rows updated for groupsId: ${groupsId.value}")
            }
            return@transaction affectedRows
        }
    }

    override fun delete(groupsId: GroupsId): Int {
        return transaction {
            val deletedRows = TbTsGroupList.deleteWhere { TbTsGroupList.groupsId eq groupsId.value }
            return@transaction deletedRows
        }
    }
}