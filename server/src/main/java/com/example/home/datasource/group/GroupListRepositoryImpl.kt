package com.example.home.datasource.group

import com.example.home.domain.group.GroupList
import com.example.home.domain.value_object.group.GroupName
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsGroupList
import com.example.home.infrastructure.persistence.repository.group.GroupListRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class GroupListRepositoryImpl() : GroupListRepository {
    override fun refer(groupsId: GroupsId): List<GroupList> {
        return transaction {
            TbTsGroupList.select(TbTsGroupList.groupsId eq groupsId.value)
                .map {
                    GroupList(
                        it[TbTsGroupList.groupId],
                        GroupsId(it[TbTsGroupList.groupsId]),
                        GroupName(it[TbTsGroupList.groupsName])
                    )
                }
        }
    }

    override fun referAll(): List<GroupList> {
        return transaction {
            TbTsGroupList.selectAll()
                .map {
                    GroupList(
                        it[TbTsGroupList.groupId],
                        GroupsId(it[TbTsGroupList.groupsId]),
                        GroupName(it[TbTsGroupList.groupsName])
                    )
                }
        }
    }

    override fun save(groupsId: GroupsId, groupName: GroupName): GroupList {
        return transaction {
            TbTsGroupList.insert {
                it[TbTsGroupList.groupsId] = groupsId.value
                it[TbTsGroupList.groupsName] = groupName.value
            }

            val group = TbTsGroupList.select {
                (TbTsGroupList.groupsId eq groupsId.value) and
                        (TbTsGroupList.groupsName eq groupName.value)
            }.singleOrNull()

            return@transaction group?.let {
                GroupList(
                    it[TbTsGroupList.groupId],
                    GroupsId(it[TbTsGroupList.groupsId]),
                    GroupName(it[TbTsGroupList.groupsName])
                )
            } ?: throw IllegalStateException("Failed to save the GroupList")
        }
    }

    override fun update(groupsId: GroupsId, groupName: GroupName): Int {
        return transaction {
            val affectedRows = TbTsGroupList.update({ TbTsGroupList.groupsId eq groupsId.value }) {
                it[TbTsGroupList.groupsName] = groupName.value
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