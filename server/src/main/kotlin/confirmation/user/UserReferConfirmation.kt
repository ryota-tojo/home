package confirmation.user

import com.example.home.domain.entity.user.result.UserSaveResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.user.UserId
import com.example.home.service.user.UserControlService
import confirmation.DbConnectConfirmation

fun userReferConfirmation(userId: UserId) {
    // 実行フラグ
    val execFlg = 1

    if (execFlg == 1) {
        println("\n＊＊＊＊＊＊＊＊＊ 処理開始：ユーザー参照 ＊＊＊＊＊＊＊＊＊\n")

        //  リポジトリ
        val dbConnectConfirmation: DbConnectConfirmation = DbConnectConfirmation()
        val userControlService: UserControlService = UserControlService()

        // DB接続
        dbConnectConfirmation.dbConnect()

        // 確認用のデータ定義

        // 確認対象のサービスを呼び出す
        val res = userControlService.refer(userId)
        println("---------- 処理結果 ----------")
        println(res.result)

        if (res.result == ResponseCode.成功.code) {
            println("---------- ユーザー情報 ----------")
            println("ユーザーID：" + (res.userInfo?.userId ?: ""))
            println("ユーザー名：" + (res.userInfo?.userName?.value ?: ""))
            println("パスワード：" + (res.userInfo?.password?.value ?: ""))
            println("権限　　　：" + (res.userInfo?.permission?.value ?: ""))
            println("承認フラグ：" + (res.userInfo?.approvalFlg?.value ?: ""))
            println("削除フラグ：" + (res.userInfo?.deleteFlg?.value ?: ""))
            println("作成日　　：" + (res.userInfo?.createDate ?: ""))
            println("更新日　　：" + (res.userInfo?.updateDate ?: ""))
            println("承認日　　：" + (res.userInfo?.approvalDate ?: ""))
            println("削除日　　：" + (res.userInfo?.deleteDate ?: ""))

            println("---------- ユーザー設定 ----------")
            for (us in res.userSetting!!) {
                println(
                    "ID：${us.id}, " +
                            "設定キー：${us.userSettingKey}, " +
                            "設定値：${us.userSettingvalue}"
                )
            }

            println("---------- 所属グループ ----------")
            for (gi in res.groupInfo!!) {
                println(
                    "所属グループID：${gi.groupsId.value}, " +
                            "ユーザーID：${gi.userId.value}, " +
                            "リーダーフラグ：${gi.userLeaderFlg.value}, " +
                            "作成日：${gi.createDate}, " +
                            "更新日：${gi.updateDate}"
                )
            }
        }
    }
}