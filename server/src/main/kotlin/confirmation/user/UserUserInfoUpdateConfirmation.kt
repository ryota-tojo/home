package confirmation.user

import com.example.home.domain.entity.user.result.UserSaveResult
import com.example.home.domain.value_object.user.*
import com.example.home.service.user.UserControlService
import confirmation.DbConnectConfirmation

fun userUserInfoUpdateConfirmation(userId: UserId) {
    // 実行フラグ
    val execFlg = 1

    if (execFlg == 1) {
        println("\n＊＊＊＊＊＊＊＊＊ 処理開始：ユーザー情報更新 ＊＊＊＊＊＊＊＊＊\n")

        //  リポジトリ
        val dbConnectConfirmation: DbConnectConfirmation = DbConnectConfirmation()
        val userControlService: UserControlService = UserControlService()

        // DB接続
        dbConnectConfirmation.dbConnect()

        // 確認用のデータ定義
        val userName = UserName("test_user2")
        val userPassword = UserPassword("1234567890")
        val userPermission = UserPermission(1)
        val userApprovalFlg = UserApprovalFlg(1)
        val userDeleteFlg = UserDeleteFlg(0)

        // 確認対象のサービスを呼び出す
        val res = userControlService.userInfoUpdate(
            userId,
            userName,
            userPassword,
            userPermission,
            userApprovalFlg,
            userDeleteFlg
        )
        println("---------- 処理結果 ----------")
        println(res.result)

        println("---------- 処理行 ----------")
        println(res.updateRows)
    }
}