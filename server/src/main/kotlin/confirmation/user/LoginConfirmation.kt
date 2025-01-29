package confirmation.user

import com.example.home.domain.entity.user.LoginInfo
import com.example.home.domain.entity.user.result.UserSaveResult
import com.example.home.service.user.LoginService
import confirmation.DbConnectConfirmation

fun loginConfirmation(userSaveResult: UserSaveResult) {
    // 実行フラグ
    val execFlg = 1

    if (execFlg == 1) {
        println("\n＊＊＊＊＊＊＊＊＊ 処理開始：ログインチェック ＊＊＊＊＊＊＊＊＊\n")

        //  リポジトリ
        val dbConnectConfirmation: DbConnectConfirmation = DbConnectConfirmation()
        val loginService: LoginService = LoginService()

        // DB接続
        dbConnectConfirmation.dbConnect()

        // 確認用のデータ定義
        val loginInfo = LoginInfo(
            userSaveResult.groupInfo?.groupsId!!,
            userSaveResult.userInfo?.userName!!,
            userSaveResult.userInfo.password
        )
        println(loginInfo)

        // 確認対象のサービスを呼び出す
        val res = loginService.login(loginInfo)
        println("---------- 処理結果 ----------")
        println(res.result)

        println("---------- 処理行 ----------")
        println("ログインフラグ：" + res.loginFlg)

    }
}