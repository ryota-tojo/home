package confirmation.category

import com.example.home.domain.entity.category.Category
import com.example.home.domain.value_object.category.CategoryName
import com.example.home.domain.value_object.category.CategoryNo
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.category.CategoryService
import confirmation.DbConnectConfirmation

fun categorySaveConfirmation(): Category? {
    // 実行フラグ
    val execFlg = 1

    if (execFlg == 1) {
        println("\n＊＊＊＊＊＊＊＊＊ 処理開始：カテゴリー登録 ＊＊＊＊＊＊＊＊＊\n")

        //  リポジトリ
        val dbConnectConfirmation: DbConnectConfirmation = DbConnectConfirmation()
        val categoryService: CategoryService = CategoryService()

        // DB接続
        dbConnectConfirmation.dbConnect()

        // 確認用のデータ定義
        val groupsId = GroupsId("test_groups_id")
        val categoryNo = CategoryNo(1)
        val categoryName = CategoryName("test_group_password")

        // 確認対象のサービスを呼び出す
        val res = categoryService.save(groupsId, categoryNo, categoryName)
        println("---------- 処理結果 ----------")
        println(res.result)

        if (res.category != null) {
            println("---------- カテゴリー ----------")
            println("id = ${res.category.id}, categoryNo = ${res.category.categoryNo}, categoryName = ${res.category.categoryName}")
        }
        return res.category
    }
    return null
}