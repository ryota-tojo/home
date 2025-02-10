package com.example.home.datasource.member

import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.member.MemberName
import com.example.home.domain.value_object.member.MemberNo
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsMembers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl : MemberRepository {
    override fun refer(groupsId: GroupsId?): List<com.example.home.domain.entity.member.Member> {
        return transaction {
            if (groupsId == null) {
                TbTsMembers.selectAll()
                    .map {
                        com.example.home.domain.entity.member.Member(
                            MemberId(it[TbTsMembers.id]),
                            GroupsId(it[TbTsMembers.groupsId]),
                            MemberNo(it[TbTsMembers.memberNo]),
                            MemberName(it[TbTsMembers.memberName])
                        )
                    }
            } else {
                TbTsMembers.select(TbTsMembers.groupsId eq groupsId.value)
                    .map {
                        com.example.home.domain.entity.member.Member(
                            MemberId(it[TbTsMembers.id]),
                            GroupsId(it[TbTsMembers.groupsId]),
                            MemberNo(it[TbTsMembers.memberNo]),
                            MemberName(it[TbTsMembers.memberName])
                        )
                    }
            }
        }
    }

    override fun save(
        groupsId: GroupsId,
        memberNo: MemberNo,
        memberName: MemberName
    ): com.example.home.domain.entity.member.Member {
        return transaction {
            TbTsMembers.insert {
                it[TbTsMembers.groupsId] = groupsId.value
                it[TbTsMembers.memberNo] = memberNo.value
                it[TbTsMembers.memberName] = memberName.value
            }

            val member = TbTsMembers.select {
                (TbTsMembers.groupsId eq groupsId.value) and
                        (TbTsMembers.memberNo eq memberNo.value) and
                        (TbTsMembers.memberName eq memberName.value)
            }.singleOrNull()

            return@transaction member?.let {
                com.example.home.domain.entity.member.Member(
                    MemberId(it[TbTsMembers.id]),
                    GroupsId(it[TbTsMembers.groupsId]),
                    MemberNo(it[TbTsMembers.memberNo]),
                    MemberName(it[TbTsMembers.memberName])
                )
            } ?: throw IllegalStateException("Failed to save the Member")
        }
    }

    override fun update(
        groupsId: GroupsId,
        memberNo: MemberNo,
        memberName: MemberName
    ): Int {
        return transaction {
            val affectedRows = TbTsMembers.update({
                (TbTsMembers.groupsId eq groupsId.value) and
                        (TbTsMembers.memberNo eq memberNo.value)
            }) {
                it[TbTsMembers.groupsId] = groupsId.value
                it[TbTsMembers.memberNo] = memberNo.value
                it[TbTsMembers.memberName] = memberName.value
            }
            if (affectedRows == 0) {
                throw IllegalStateException("No rows updated for groupsId: ${groupsId.value}")
            }
            return@transaction affectedRows
        }
    }

    override fun delete(groupsId: GroupsId, memberNo: MemberNo?, memberName: MemberName?): Int {
        return transaction {
            val condition = TbTsMembers.groupsId eq groupsId.value

            val fullCondition = when {
                memberNo != null && memberName != null ->
                    condition and (TbTsMembers.memberNo eq memberNo.value) and (TbTsMembers.memberName eq memberName.value)

                memberNo != null ->
                    condition and (TbTsMembers.memberNo eq memberNo.value)

                memberName != null ->
                    condition and (TbTsMembers.memberName eq memberName.value)

                else -> condition
            }

            val deleteRows = TbTsMembers.deleteWhere { fullCondition }

            if (deleteRows == 0) {
                throw IllegalStateException(
                    "No rows deleted for groupsId: ${groupsId.value}, memberNo: ${memberNo?.value}, memberName: ${memberName?.value}"
                )
            }
            deleteRows

        }
    }
}