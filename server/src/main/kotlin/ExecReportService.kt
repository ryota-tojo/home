import com.example.home.datasource.budgets.BudgetsRepositoryImpl
import com.example.home.datasource.category.CategoryRepositoryImpl
import com.example.home.datasource.comment.CommentRepositoryImpl
import com.example.home.datasource.fixed.FixedRepositoryImpl
import com.example.home.datasource.group.GroupListRepositoryImpl
import com.example.home.datasource.member.MemberRepositoryImpl
import com.example.home.datasource.shopping.ShoppingRepositoryImpl
import com.example.home.domain.repository.budgets.BudgetsRepository
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.comment.CommentRepository
import com.example.home.domain.repository.fixed.FixedRepository
import com.example.home.domain.repository.group.GroupListRepository
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.repository.shopping.ShoppingRepository
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupPassword
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.fixed.FixedService
import com.example.home.service.report.ReportService
import confirmation.DbConnectConfirmation

fun main() {

    // 実行フラグ
    val execFlg = 1

    if (execFlg == 1) {
        println("\n＊＊＊＊＊＊＊＊＊ 処理開始 ＊＊＊＊＊＊＊＊＊\n")

        //  リポジトリ
        val dbConnectConfirmation: DbConnectConfirmation = DbConnectConfirmation()

        val groupListRepository: GroupListRepository = GroupListRepositoryImpl()
        val memberRepository: MemberRepository = MemberRepositoryImpl()
        val categoryRepository: CategoryRepository = CategoryRepositoryImpl()
        val budgetsRepository: BudgetsRepository = BudgetsRepositoryImpl()
        val shoppingRepository: ShoppingRepository = ShoppingRepositoryImpl()


        val reportService: ReportService =
            ReportService(
                groupListRepository,
                memberRepository,
                categoryRepository,
                budgetsRepository,
                shoppingRepository
            )

        val fixedRepository: FixedRepository = FixedRepositoryImpl()
        val commentRepository: CommentRepository = CommentRepositoryImpl()

        val fixedService: FixedService =
            FixedService(fixedRepository, budgetsRepository, shoppingRepository, commentRepository)

        // DB接続
        dbConnectConfirmation.dbConnect()

        // 確認対象のサービスを呼び出す
        val res = reportService.createReport(
            GroupsId("group004"),
            GroupPassword("1234567890"),
            YYYY(2030),
            MM(1)
        )

        println("---------- 処理結果 ----------")
        println(res)

    }

}