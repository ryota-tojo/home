package com.example.home.datasource.notice

import com.example.home.domain.entity.notice.Notice
import com.example.home.domain.repository.notice.NoticeRepository
import com.example.home.domain.value_object.notice.NoticeContent
import com.example.home.domain.value_object.notice.NoticeId
import com.example.home.domain.value_object.notice.NoticeTitle
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsNotice
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class NoticeRepositoryImpl : NoticeRepository {

    override fun refer(noticeId: NoticeId?): List<Notice> {
        return transaction {
            TbTsNotice.select {
                Op.build {
                    var condition: Op<Boolean> = Op.TRUE
                    noticeId?.let { condition = TbTsNotice.noticeId eq it.value }
                    condition
                }
            }.map {
                Notice(
                    NoticeId(it[TbTsNotice.noticeId]),
                    NoticeTitle(it[TbTsNotice.title]),
                    NoticeContent(it[TbTsNotice.content]),
                    it[TbTsNotice.createDate],
                    it[TbTsNotice.updateDate]
                )
            }
        }
    }

    override fun save(noticeTitle: NoticeTitle, noticeContent: NoticeContent): Notice {
        return transaction {
            val id = TbTsNotice.insert {
                it[title] = noticeTitle.value
                it[content] = noticeContent.value
                it[createDate] = LocalDateTime.now()
                it[updateDate] = LocalDateTime.now()
            }.resultedValues?.firstOrNull()?.get(TbTsNotice.noticeId)

            if (id == null || id == 0) {
                throw IllegalStateException("データの挿入に失敗しました")
            }
            val noticeList = refer(NoticeId(id))
            noticeList.single()
        }
    }

    override fun update(noticeId: NoticeId, noticeTitle: NoticeTitle, noticeContent: NoticeContent): Int {
        return transaction {
            val updateRows = TbTsNotice.update({
                TbTsNotice.noticeId eq noticeId.value
            }) {
                it[title] = noticeTitle.value
                it[content] = noticeContent.value
                it[updateDate] = LocalDateTime.now()
            }
            return@transaction updateRows
        }
    }

    override fun delete(noticeId: NoticeId): Int {
        return transaction {
            val updateRows = TbTsNotice.deleteWhere { this.noticeId eq noticeId.value }
            return@transaction updateRows
        }
    }

}