package com.example.home.datasource.master

import com.example.home.domain.entity.master.MasterSetting
import com.example.home.domain.repository.master.MasterSettingRepository
import com.example.home.domain.value_object.master.MasterSettingKey
import com.example.home.domain.value_object.master.MasterSettingValue
import com.example.home.infrastructure.persistence.exposed_tables.master.TbMsSetting
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class MasterSettingRepositoryImpl : MasterSettingRepository {

    override fun refer(masterSettingKey: MasterSettingKey?): List<MasterSetting> {
        return transaction {
            TbMsSetting.select {
                Op.build {
                    var condition: Op<Boolean> = Op.TRUE
                    masterSettingKey?.let { condition = TbMsSetting.settingKey eq it.value }
                    condition
                }
            }.map {
                MasterSetting(
                    MasterSettingKey(it[TbMsSetting.settingKey]),
                    MasterSettingValue(it[TbMsSetting.settingValue])
                )
            }
        }
    }

    override fun save(masterSettingKey: MasterSettingKey, masterSettingValue: MasterSettingValue): MasterSetting {
        return transaction {
            TbMsSetting.insert {
                it[settingKey] = masterSettingKey.value
                it[settingValue] = masterSettingValue.value
            }
            return@transaction MasterSetting(masterSettingKey, masterSettingValue)
        }
    }

    override fun update(masterSettingKey: MasterSettingKey, masterSettingValue: MasterSettingValue): Int {
        return transaction {
            val updateRows = TbMsSetting.update({ TbMsSetting.settingKey eq masterSettingKey.value }) {
                it[settingValue] = masterSettingValue.value
            }
            return@transaction updateRows
        }
    }

    override fun delete(masterSettingKey: MasterSettingKey): Int {
        return transaction {
            val deleteRows = TbMsSetting.deleteWhere { settingKey eq masterSettingKey.value }
            return@transaction deleteRows
        }
    }

}