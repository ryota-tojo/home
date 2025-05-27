package com.example.home.datasource.group

import com.example.home.domain.entity.group.GroupSetting
import com.example.home.domain.repository.group.GroupSettingRepository
import com.example.home.domain.value_object.group.GroupSettingKey
import com.example.home.domain.value_object.group.GroupSettingValue
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsGroupSetting
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class GroupSettingRepositoryImpl : GroupSettingRepository {
    override fun refer(groupsId: GroupsId?,groupSettingKey: GroupSettingKey?): List<GroupSetting> {
        return transaction {
            val condition = buildList<Op<Boolean>> {
                groupsId?.let { add(TbTsGroupSetting.groupsId eq it.value) }
                groupSettingKey?.let { add(TbTsGroupSetting.settingKey eq it.value) }
            }.reduceOrNull { acc, op -> acc and op } ?: Op.TRUE
            TbTsGroupSetting.select { condition }
                .orderBy(TbTsGroupSetting.groupSettingId to SortOrder.ASC)
                .map {
                GroupSetting(
                    it[TbTsGroupSetting.groupSettingId],
                    GroupsId(it[TbTsGroupSetting.groupsId]),
                    GroupSettingKey(it[TbTsGroupSetting.settingKey]),
                    GroupSettingValue(it[TbTsGroupSetting.settingValue])
                )
            }
        }
    }

    override fun save(
        groupsId: GroupsId,
        settingKey: GroupSettingKey,
        settingValue: GroupSettingValue
    ): GroupSetting {
        return transaction {
            TbTsGroupSetting.insert {
                it[TbTsGroupSetting.groupsId] = groupsId.value
                it[TbTsGroupSetting.settingKey] = settingKey.value
                it[TbTsGroupSetting.settingValue] = settingValue.value
            }

            val group = TbTsGroupSetting
                .select { TbTsGroupSetting.groupsId eq groupsId.value }
                .orderBy(TbTsGroupSetting.groupSettingId, SortOrder.DESC)
                .limit(1)
                .singleOrNull()

            return@transaction group?.let {
                GroupSetting(
                    it[TbTsGroupSetting.groupSettingId],
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

    override fun delete(groupsId: GroupsId,settingKey: GroupSettingKey?): Int {
        return transaction {
            var condition: Op<Boolean> = TbTsGroupSetting.groupsId eq groupsId.value
            settingKey?.let {
                condition = condition and (TbTsGroupSetting.settingKey eq it.value)
            }

            val deletedRows = TbTsGroupSetting.deleteWhere { condition }
            return@transaction deletedRows
        }
    }
}