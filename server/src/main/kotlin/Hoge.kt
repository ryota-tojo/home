import com.example.home.domain.value_object.category.CategoryNo
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.budgets.BudgetsService
import confirmation.DbConnectConfirmation

fun main() {

    //  リポジトリ
    val dbConnectConfirmation: DbConnectConfirmation = DbConnectConfirmation()
    val budgetsService: BudgetsService = BudgetsService()

    // DB接続
    dbConnectConfirmation.dbConnect()

    // 確認用のデータ定義
    val groupsId = GroupsId("group_001")
    val yyyy = YYYY(2025)
    val mm = MM(2)
    val categoryNo = CategoryNo(1)

    // 確認対象のサービスを呼び出す
    val res = budgetsService.refer(groupsId, yyyy, mm, null)
    for (b in res.budgets!!) {
        println("-------------------")
        println("ID　　　　：${b.id}")
        println("グループID：${b.groupsId.value}")
        println("年　　　　：${b.YYYY.value}")
        println("月　　　　：${b.MM.value}")
        println("分類No　　：${b.categoryNo.value}")
        println("金額　　　：${b.amount.value}")
        println("確定フラグ：${b.fixedFlg.value}")
    }
}


