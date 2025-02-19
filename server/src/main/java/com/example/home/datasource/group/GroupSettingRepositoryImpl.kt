package com.example.home.datasource.group

import com.example.home.domain.group.GroupList
import com.example.home.domain.group.GroupSetting
import com.example.home.domain.value_object.group.GroupName
import com.example.home.domain.value_object.group.GroupSettingKey
import com.example.home.domain.value_object.group.GroupSettingValue
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsGroupList
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsGroupSetting
import com.example.home.infrastructure.persistence.repository.group.GroupSettingRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class GroupSettingRepositoryImpl(

) : GroupSettingRepository {
    override fun refer(groupsId: GroupsId): List<GroupSetting> {
        return transaction {
            TbTsGroupSetting.select(TbTsGroupSetting.groupsId eq groupsId.value)
                .map {
                    GroupSetting(
                        it[TbTsGroupSetting.id],
                        GroupsId(it[TbTsGroupSetting.groupsId]),
                        GroupSettingKey(it[TbTsGroupSetting.settingKey]),
                        GroupSettingValue(it[TbTsGroupSetting.settingValue])
                    )
                }
        }
    }

    override fun referAll(): List<GroupSetting> {
        return transaction {
            TbTsGroupSetting.selectAll()
                .map {
                    GroupSetting(
                        it[TbTsGroupSetting.id],
                        GroupsId(it[TbTsGroupSetting.groupsId]),
                        GroupSettingKey(it[TbTsGroupSetting.settingKey]),
                        GroupSettingValue(it[TbTsGroupSetting.settingValue])
                    )
                }
        }
    }

    override fun save(groupsId: GroupsId, settingKey: GroupSettingKey, settingValue: GroupSettingValue): GroupSetting {
        return transaction {
            TbTsGroupSetting.insert {
                it[TbTsGroupSetting.groupsId] = groupsId.value
                it[TbTsGroupSetting.settingKey] = settingKey.value
                it[TbTsGroupSetting.settingValue] = settingValue.value
            }

            val group = TbTsGroupSetting
                .select { TbTsGroupSetting.groupsId eq groupsId.value }
                .orderBy(TbTsGroupSetting.id, SortOrder.DESC)
                .limit(1)
                .singleOrNull()

            return@transaction group?.let {
                GroupSetting(
                    it[TbTsGroupSetting.id],
                    GroupsId(it[TbTsGroupSetting.groupsId]),
                    GroupSettingKey(it[TbTsGroupSetting.settingKey]),
                    GroupSettingValue(it[TbTsGroupSetting.settingValue])
                )
            } ?: throw IllegalStateException("Failed to save the GroupSetting")
        }
    }

    override fun update(groupsId: GroupsId, settingKey: GroupSettingKey, settingValue: GroupSettingValue): Int {
        return transaction {
            val affectedRows = TbTsGroupSetting.update({
                (TbTsGroupSetting.groupsId eq groupsId.value) and
                        (TbTsGroupSetting.settingKey eq settingKey.value)
            }) {
                it[TbTsGroupSetting.settingValue] = settingValue.value
            }
            if (affectedRows == 0) {
                throw IllegalStateException("No rows updated for groupsId: ${groupsId.value}, settingKey: ${settingKey.value}")
            }
            return@transaction affectedRows
        }
    }

    override fun delete(groupsId: GroupsId): Int {
        return transaction {
            val deletedRows = TbTsGroupSetting.deleteWhere { TbTsGroupSetting.groupsId eq groupsId.value }
            return@transaction deletedRows
        }
    }
}