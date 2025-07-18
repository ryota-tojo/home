package com.example.home.datasource.master

import com.example.home.domain.entity.master.MasterScreen
import com.example.home.domain.repository.master.MasterScreenRepository
import com.example.home.domain.value_object.master.ScreenId
import com.example.home.domain.value_object.master.ScreenName
import com.example.home.domain.value_object.master.ScreenRemarks
import com.example.home.infrastructure.persistence.exposed_tables.master.TbMsScreen
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class MasterScreenRepositoryImpl : MasterScreenRepository {

    override fun refer(screenId: ScreenId?): List<MasterScreen> {
        return transaction {
            TbMsScreen.select {
                Op.build {
                    var condition: Op<Boolean> = Op.TRUE
                    screenId?.let { condition = TbMsScreen.screenId eq it.value }
                    condition
                }
            }.map {
                MasterScreen(
                    ScreenId(it[TbMsScreen.screenId]),
                    ScreenName(it[TbMsScreen.screenName]),
                    ScreenRemarks(it[TbMsScreen.remarks])
                )
            }
        }
    }

    override fun save(screenId: ScreenId, screenName: ScreenName, screenRemarks: ScreenRemarks): MasterScreen {
        return transaction {
            TbMsScreen.insert {
                it[this.screenId] = screenId.value
                it[this.screenName] = screenName.value
                it[this.remarks] = screenRemarks.value
            }

            MasterScreen(screenId, screenName, screenRemarks)
        }
    }
}