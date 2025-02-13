import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsNotice
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {

    transaction {
        SchemaUtils.create(TbTsNotice)
    }

    transaction {
        println("-- SELECT * すべて")
        val results_all = TbTsNotice.selectAll().map {
            mapOf(
                "id" to it[TbTsNotice.noticeId],
                "title" to it[TbTsNotice.title],
                "content" to it[TbTsNotice.content],
                "createDate" to it[TbTsNotice.createDate],
                "updateDate" to it[TbTsNotice.updateDate]
            )
        }
        println(results_all)
    }

//    transaction {
//        println("-- SELECT WHEREあり")
//        val results_cond = TbMsSetting.select(TbMsSetting.id eq 1).map {
//            mapOf(
//                "id" to it[TbMsSetting.id],
//                "title" to it[TbTsNotice.title],
//                "content" to it[TbTsNotice.content]
//            )
//        }
//    }
//
//    transaction {
//        println("-- COUNT ALL")
//        val count = TbTsNotice.selectAll().count()
//    }
//    
//    transaction {
//        println("-- INSERT * ")
//        TbTsNotice.insert {
//            it[TbTsNotice.title] = "お知らせタイトル"
//            it[TbTsNotice.content] = "お知らせ内容"
//            it[TbTsNotice.createDate] = LocalDateTime.now()
//            it[TbTsNotice.updateDate] = LocalDateTime.now()
//        }
//    }
//
//    transaction {
//        println("-- UPDATE * ")
//        TbTsNotice.update({ TbTsNotice.id eq 1 }) {
//            it[title] = "更新されたタイトル"
//            it[content] = "更新された内容"
//            it[updateDate] = LocalDateTime.now()
//        }
//    }
//    transaction {
//        println("-- DELETE * ")
//        TbTsNotice.deleteWhere { id eq 1 }
//    }
}

