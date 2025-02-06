package confirmation.group

import com.example.home.domain.value_object.group.GroupSettingKey
import com.example.home.domain.value_object.group.GroupSettingValue
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.group.GroupControlService
import confirmation.DbConnectConfirmation

fun groupSettingUpdateConfirmation() {
    // 実行フラグ
    val execFlg = 1

    if (execFlg == 1) {
        println("\n＊＊＊＊＊＊＊＊＊ 処理開始：ユーザー設定更新 ＊＊＊＊＊＊＊＊＊\n")

        //  リポジトリ
        val dbConnectConfirmation: DbConnectConfirmation = DbConnectConfirmation()
        val groupControlService: GroupControlService = GroupControlService()

        // DB接続
        dbConnectConfirmation.dbConnect()

        // 確認用のデータ定義
        val groupsId = GroupsId("test_groups_id")
        val groupSettingKey = GroupSettingKey("display_yyyy")
        val groupSettingValue = GroupSettingValue("2026")

        // 確認対象のサービスを呼び出す
        val res = groupControlService.settingUpdate(groupsId, groupSettingKey, groupSettingValue)
        println("---------- 処理結果 ----------")
        println(res.result)

        println("---------- 処理行 ----------")
        println(res.updateRows)

    }
}