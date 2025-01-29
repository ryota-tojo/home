package confirmation.user

import com.example.home.domain.value_object.user.*
import com.example.home.service.user.UserControlService
import confirmation.DbConnectConfirmation

fun userDeleteConfirmation(userId: UserId) {
    // 実行フラグ
    val execFlg = 1

    if (execFlg == 1) {
        println("\n＊＊＊＊＊＊＊＊＊ 処理開始：ユーザー削除 ＊＊＊＊＊＊＊＊＊\n")

        //  リポジトリ
        val dbConnectConfirmation: DbConnectConfirmation = DbConnectConfirmation()
        val userControlService: UserControlService = UserControlService()

        // DB接続
        dbConnectConfirmation.dbConnect()

        // 確認用のデータ定義

        // 確認対象のサービスを呼び出す
        val res = userControlService.delete(
            userId
        )
        println("---------- 処理結果 ----------")
        println(res.result)

        println("---------- 処理行 ----------")
        println(res.updateRows)
    }
}