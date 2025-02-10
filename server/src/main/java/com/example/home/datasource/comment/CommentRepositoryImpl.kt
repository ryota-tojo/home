package com.example.home.datasource.comment

import com.example.home.domain.entity.comment.Comment
import com.example.home.domain.repository.comment.CommentRepository
import com.example.home.domain.value_object.comment.Content
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsComment
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class CommentRepositoryImpl : CommentRepository {
    override fun refer(
        groupsId: GroupsId?,
        yyyy: YYYY?,
        mm: MM?
    ): List<Comment> {
        return transaction {
            if (groupsId == null) {
                TbTsComment.selectAll()
                    .map {
                        Comment(
                            it[TbTsComment.id],
                            GroupsId(it[TbTsComment.groupsId]),
                            YYYY(it[TbTsComment.yyyy]),
                            MM(it[TbTsComment.mm]),
                            Content(it[TbTsComment.content])
                        )
                    }
            } else {
                TbTsComment.select {
                    Op.build {
                        var condition: Op<Boolean> = TbTsComment.groupsId eq groupsId.value
                        yyyy?.let { condition = condition and (TbTsComment.yyyy eq it.value) }
                        mm?.let { condition = condition and (TbTsComment.mm eq it.value) }
                        condition
                    }
                }.map {
                    Comment(
                        it[TbTsComment.id],
                        GroupsId(it[TbTsComment.groupsId]),
                        YYYY(it[TbTsComment.yyyy]),
                        MM(it[TbTsComment.mm]),
                        Content(it[TbTsComment.content])
                    )
                }
            }
        }
    }

    override fun save(
        groupsId: GroupsId,
        yyyy: YYYY,
        mm: MM,
        content: Content
    ): Comment {
        return transaction {
            TbTsComment.insert {
                it[TbTsComment.groupsId] = groupsId.value
                it[TbTsComment.yyyy] = yyyy.value
                it[TbTsComment.mm] = mm.value
                it[TbTsComment.content] = content.value
            }

            val comment = TbTsComment.select {
                (TbTsComment.groupsId eq groupsId.value) and
                        (TbTsComment.yyyy eq yyyy.value) and
                        (TbTsComment.mm eq mm.value)
            }.singleOrNull()

            return@transaction comment?.let {
                Comment(
                    it[TbTsComment.id],
                    GroupsId(it[TbTsComment.groupsId]),
                    YYYY(it[TbTsComment.yyyy]),
                    MM(it[TbTsComment.mm]),
                    Content(it[TbTsComment.content])
                )
            } ?: throw IllegalStateException("Failed to save the Comment")
        }
    }

    override fun update(
        groupsId: GroupsId,
        yyyy: YYYY,
        mm: MM,
        content: Content
    ): Int {
        return transaction {
            val affectedRows = TbTsComment.update({
                (TbTsComment.groupsId eq groupsId.value) and
                        (TbTsComment.yyyy eq yyyy.value) and
                        (TbTsComment.mm eq mm.value)
            }) {
                it[TbTsComment.groupsId] = groupsId.value
                it[TbTsComment.yyyy] = yyyy.value
                it[TbTsComment.mm] = mm.value
            }
            if (affectedRows == 0) {
                throw IllegalStateException("No rows updated for groupsId: ${groupsId.value}, yyyy: ${yyyy.value}, mm: ${mm.value}")
            }
            return@transaction affectedRows
        }
    }

    override fun delete(
        groupsId: GroupsId,
        yyyy: YYYY?,
        mm: MM?,
    ): Int {
        return transaction {
            val condition = TbTsComment.groupsId eq groupsId.value

            val fullCondition = when {
                yyyy != null && mm != null ->
                    condition and (TbTsComment.yyyy eq yyyy.value) and (TbTsComment.mm eq mm.value)

                yyyy != null ->
                    condition and (TbTsComment.yyyy eq yyyy.value)

                mm != null ->
                    condition and (TbTsComment.mm eq mm.value)

                else -> condition
            }

            val deleteRows = TbTsComment.deleteWhere { fullCondition }

            if (deleteRows == 0) {
                throw IllegalStateException(
                    "No rows deleted for groupsId: ${groupsId.value}, yyyy: ${yyyy?.value}, mm: ${mm?.value}"
                )
            }
            deleteRows

        }
    }
}