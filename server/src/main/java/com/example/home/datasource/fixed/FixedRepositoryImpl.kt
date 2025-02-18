package com.example.home.datasource.fixed

import com.example.home.domain.entity.fixed.Fixed
import com.example.home.domain.repository.fixed.FixedRepository
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.fixed.FixedId
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsFixed
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class FixedRepositoryImpl : FixedRepository {
    override fun refer(groupsId: GroupsId, yyyy: YYYY, mm: MM?): List<Fixed> {
        return transaction {
            var condition: Op<Boolean> = (TbTsFixed.groupsId eq groupsId.value) and (TbTsFixed.yyyy eq yyyy.value)
            if (mm != null) {
                mm.let { condition = condition and (TbTsFixed.mm eq mm.value) }
            }
            TbTsFixed
                .select {
                    condition
                }
                .orderBy(TbTsFixed.yyyy to SortOrder.ASC)
                .orderBy(TbTsFixed.mm to SortOrder.ASC)
                .map {
                    Fixed(
                        FixedId(it[TbTsFixed.fixedId]),
                        GroupsId(it[TbTsFixed.groupsId]),
                        YYYY(it[TbTsFixed.yyyy]),
                        MM(it[TbTsFixed.mm])
                    )
                }
        }
    }

    override fun fixed(
        groupsId: GroupsId,
        yyyy: YYYY,
        mm: MM
    ): Boolean {
        return transaction {
            TbTsFixed.insert {
                it[TbTsFixed.groupsId] = groupsId.value
                it[TbTsFixed.yyyy] = yyyy.value
                it[TbTsFixed.mm] = mm.value
            }
            refer(groupsId, yyyy, mm).singleOrNull() != null
        }
    }

    override fun unFixed(
        groupsId: GroupsId,
        yyyy: YYYY,
        mm: MM
    ): Boolean {
        return transaction {
            val condition: Op<Boolean> = (TbTsFixed.groupsId eq groupsId.value) and
                    (TbTsFixed.yyyy eq yyyy.value) and
                    (TbTsFixed.mm eq mm.value)

            val deleteRows = TbTsFixed.deleteWhere { condition }
            deleteRows > 0
        }
    }

}