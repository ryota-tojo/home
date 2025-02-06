package confirmation.category

import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.category.CategoryService
import confirmation.DbConnectConfirmation

fun categoryReferConfirmation(groupsId: GroupsId) {
    // 実行フラグ
    val execFlg = 1

    if (execFlg == 1) {
        println("\n＊＊＊＊＊＊＊＊＊ 処理開始：カテゴリー参照 ＊＊＊＊＊＊＊＊＊\n")

        //  リポジトリ
        val dbConnectConfirmation: DbConnectConfirmation = DbConnectConfirmation()
        val categoryService: CategoryService = CategoryService()

        // DB接続
        dbConnectConfirmation.dbConnect()

        // 確認用のデータ定義

        // 確認対象のサービスを呼び出す
        val res = categoryService.refer(groupsId)
        println("---------- 処理結果 ----------")
        println(res.result)

        if (res.result == ResponseCode.成功.code) {
            println("---------- カテゴリー一覧 ----------")
            for (categoryList in res.category!!) {
                println("id = ${categoryList.id}, categoryNo = ${categoryList.categoryNo}, categoryName = ${categoryList.categoryName}")
            }
        }
    }
}