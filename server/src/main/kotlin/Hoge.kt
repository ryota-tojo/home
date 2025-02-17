import com.example.home.datasource.category.CategoryRepositoryImpl
import com.example.home.datasource.master.ChoicesRepositoryImpl
import com.example.home.datasource.member.MemberRepositoryImpl
import com.example.home.datasource.shopping.ShoppingRepositoryImpl
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.master.ChoicesRepository
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.repository.shopping.ShoppingRepository
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.shopping.ShoppingPayment
import com.example.home.domain.value_object.shopping.ShoppingRemarks
import com.example.home.domain.value_object.shopping.ShoppingSettlement
import com.example.home.domain.value_object.shopping.ShoppingType
import com.example.home.domain.value_object.user.UserId
import com.example.home.service.shopping.ShoppingService
import confirmation.DbConnectConfirmation
import java.time.LocalDate

fun main() {

    // 実行フラグ
    val execFlg = 0

    if (execFlg == 1) {
        println("\n＊＊＊＊＊＊＊＊＊ 処理開始 ＊＊＊＊＊＊＊＊＊\n")

        //  リポジトリ
        val dbConnectConfirmation: DbConnectConfirmation = DbConnectConfirmation()
        val shoppingRepository: ShoppingRepository = ShoppingRepositoryImpl()
        val memberRepository: MemberRepository = MemberRepositoryImpl()
        val categoryRepository: CategoryRepository = CategoryRepositoryImpl()
        val choicesRepository: ChoicesRepository = ChoicesRepositoryImpl()
        val shoppingService: ShoppingService =
            ShoppingService(shoppingRepository, memberRepository, categoryRepository, choicesRepository)

        // DB接続
        dbConnectConfirmation.dbConnect()

        // 確認用のデータ定義
        val groupsId = GroupsId("group001")
        val userId = UserId(1)
        val shoppingDate = LocalDate.now()
        val memberId = MemberId(3)
        val categoryId = CategoryId(1)
        val type = ShoppingType(1)
        val payment = ShoppingPayment(1)
        val settlement = ShoppingSettlement(1)
        val amount = Amount(1000)
        val remarks = ShoppingRemarks("aaaa")

        // 確認対象のサービスを呼び出す
        val res = shoppingService.getOldCategories(
            GroupsId("group002")
        )

        println("---------- 処理結果 ----------")
        println(res)

    }

}