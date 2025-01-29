package com.example.home.datasource.user

import com.example.home.domain.entity.user.UserSetting
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserSettingKey
import com.example.home.domain.value_object.user.UserSettingValue
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsUserSetting
import com.example.home.domain.repository.user.UserSettingRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class UserSettingRepositoryImpl : UserSettingRepository {
    override fun refer(userId: UserId, settingKey: UserSettingKey): List<UserSetting> {
        return transaction {
            TbTsUserSetting.select {
                (TbTsUserSetting.userId eq userId.value) and
                        (TbTsUserSetting.settingKey eq settingKey.value)
            }.map {
                    UserSetting(
                        it[TbTsUserSetting.id],
                        UserId(it[TbTsUserSetting.userId]),
                        UserSettingKey(it[TbTsUserSetting.settingKey]),
                        UserSettingValue(it[TbTsUserSetting.settingValue])
                    )
                }
        }
    }

    override fun referAll(userId: UserId): List<UserSetting> {
        return transaction {
            TbTsUserSetting.select(TbTsUserSetting.userId eq userId.value)
                .map {
                    UserSetting(
                        it[TbTsUserSetting.id],
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

            val userSetting = TbTsUserSetting
                .select {
                    (TbTsUserSetting.userId eq userId.value) and
                            (TbTsUserSetting.settingKey eq settingKey.value) and
                            (TbTsUserSetting.settingValue eq settingValue.value)
                }
                .singleOrNull()

            return@transaction userSetting?.let {
                UserSetting(
                    it[TbTsUserSetting.id],
                    UserId(it[TbTsUserSetting.userId]),
                    UserSettingKey(it[TbTsUserSetting.settingKey]),
                    UserSettingValue(it[TbTsUserSetting.settingValue])
                )
            } ?: throw IllegalStateException("Failed to save the UserSetting")
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
            if (affectedRows == 0) {
                throw IllegalStateException("No rows updated for UserId: ${userId.value}, settingKey: ${settingKey.value}")
            }
            return@transaction affectedRows
        }
    }

    override fun delete(userId: UserId): Int {
        return transaction {
            val deletedRows = TbTsUserSetting.deleteWhere { TbTsUserSetting.userId eq userId.value }
            return@transaction deletedRows
        }
    }
}