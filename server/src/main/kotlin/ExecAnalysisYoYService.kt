import com.example.home.datasource.category.CategoryRepositoryImpl
import com.example.home.datasource.comment.CommentRepositoryImpl
import com.example.home.datasource.fixed.FixedRepositoryImpl
import com.example.home.datasource.shopping.ShoppingRepositoryImpl
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.comment.CommentRepository
import com.example.home.domain.repository.fixed.FixedRepository
import com.example.home.domain.repository.shopping.ShoppingRepository
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.analysis.AnalysisYoYService
import confirmation.DbConnectConfirmation

fun main() {

    // 実行フラグ
    val execFlg = 0

    if (execFlg == 1) {
        println("\n＊＊＊＊＊＊＊＊＊ 処理開始 ＊＊＊＊＊＊＊＊＊\n")

        //  リポジトリ
        val dbConnectConfirmation: DbConnectConfirmation = DbConnectConfirmation()

        val categoryRepository: CategoryRepository = CategoryRepositoryImpl()
        val shoppingRepository: ShoppingRepository = ShoppingRepositoryImpl()


        val yoyService: AnalysisYoYService =
            AnalysisYoYService(
                categoryRepository,
                shoppingRepository
            )

        val fixedRepository: FixedRepository = FixedRepositoryImpl()
        val commentRepository: CommentRepository = CommentRepositoryImpl()

        // DB接続
        dbConnectConfirmation.dbConnect()

        // 確認対象のサービスを呼び出す
        val res = yoyService.analysisYoY(
            GroupsId("home"),
            YYYY(2024),
        )

        println("---------- 処理結果 ----------")
        println(res)

    }

}