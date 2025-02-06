package confirmation.user

import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.user.UserId
import com.example.home.service.user.UserControlService
import confirmation.DbConnectConfirmation

fun userReferAllConfirmation() {
    // 実行フラグ
    val execFlg = 1

    if (execFlg == 1) {
        println("\n＊＊＊＊＊＊＊＊＊ 処理開始：ユーザー全参照 ＊＊＊＊＊＊＊＊＊\n")

        //  リポジトリ
        val dbConnectConfirmation: DbConnectConfirmation = DbConnectConfirmation()
        val userControlService: UserControlService = UserControlService()

        // DB接続
        dbConnectConfirmation.dbConnect()

        // 確認用のデータ定義

        // 確認対象のサービスを呼び出す
        val res = userControlService.referAll()
        println("---------- 処理結果 ----------")
        println(res.result)

        if (res.result == ResponseCode.成功.code) {
            for (user in res.userInfo!!) {
                println("---------- ユーザー情報 ----------")
                println("ユーザーID：" + (user.userId))
                println("ユーザー名：" + (user.userName.value))
                println("パスワード：" + (user.password.value))
                println("権限　　　：" + (user.permission.value))
                println("承認フラグ：" + (user.approvalFlg.value))
                println("削除フラグ：" + (user.deleteFlg.value))
                println("作成日　　：" + (user.createDate))
                println("更新日　　：" + (user.updateDate))
                println("承認日　　：" + (user.approvalDate))
                println("削除日　　：" + (user.deleteDate))
            }
        }
    }
}