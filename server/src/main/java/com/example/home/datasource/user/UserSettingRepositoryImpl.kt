package com.example.home.datasource.user

import com.example.home.domain.entity.user.UserSetting
import com.example.home.domain.repository.user.UserSettingRepository
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserSettingKey
import com.example.home.domain.value_object.user.UserSettingValue
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsGroupSetting
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsUserSetting
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class UserSettingRepositoryImpl : UserSettingRepository {
    override fun refer(userId: UserId, settingKey: UserSettingKey?): List<UserSetting> {
        return transaction {
            val condition = buildList<Op<Boolean>> {
                add(TbTsUserSetting.userId eq userId.value) // userId は非nullと仮定
                settingKey?.let { add(TbTsUserSetting.settingKey eq it.value) }
            }.reduceOrNull { acc, op -> acc and op } ?: Op.TRUE
            TbTsUserSetting.select { condition }
                .orderBy(TbTsUserSetting.userSettingId to SortOrder.ASC)
                .map {
                UserSetting(
                    it[TbTsUserSetting.userSettingId],
                    UserId(it[TbTsUserSetting.userId]),
                    UserSettingKey(it[TbTsUserSetting.settingKey]),
                    UserSettingValue(it[TbTsUserSetting.settingValue])
                )
            }
        }
    }

    override fun save(
        userId: UserId,
        settingKey: UserSettingKey,
        settingValue: UserSettingValue
    ): UserSetting {
        return transaction {
            TbTsUserSetting.insert {
                it[TbTsUserSetting.userId] = userId.value
                it[TbTsUserSetting.settingKey] = settingKey.value
                it[TbTsUserSetting.settingValue] = settingValue.value
            }

            val userSettingList = refer(userId, settingKey)
            if (userSettingList == null) {
                throw IllegalStateException("データの挿入に失敗しました")
            }
            userSettingList.single()
        }
    }

    override fun update(
        userId: UserId,
        settingKey: UserSettingKey,
        settingValue: UserSettingValue
    ): Int {
        return transaction {
            val affectedRows = TbTsUserSetting.update({
                (TbTsUserSetting.userId eq userId.value) and
                        (TbTsUserSetting.settingKey eq settingKey.value)
            }) {
                it[TbTsUserSetting.settingValue] = settingValue.value
            }
            return@transaction affectedRows
        }
    }

    override fun delete(userId: UserId,settingKey: UserSettingKey?): Int {
        return transaction {
            var condition: Op<Boolean> = TbTsUserSetting.userId eq userId.value
            settingKey?.let {
                condition = condition and (TbTsUserSetting.settingKey eq it.value)
            }

            val deletedRows = TbTsUserSetting.deleteWhere { condition }
            return@transaction deletedRows
        }
    }
}