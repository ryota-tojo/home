package confirmation.group

import com.example.home.service.group.GroupControlService
import confirmation.DbConnectConfirmation

fun groupReferAllConfirmation() {
    // 実行フラグ
    val execFlg = 1

    if (execFlg == 1) {
        println("\n＊＊＊＊＊＊＊＊＊ 処理開始：グループ全参照 ＊＊＊＊＊＊＊＊＊\n")

        //  リポジトリ
        val dbConnectConfirmation: DbConnectConfirmation = DbConnectConfirmation()
        val groupControlService: GroupControlService = GroupControlService()

        // DB接続
        dbConnectConfirmation.dbConnect()

        // 確認用のデータ定義

        // 確認対象のサービスを呼び出す
        val res = groupControlService.referAll()
        println("---------- 処理結果 ----------")
        println(res.result)

        for (group in res.groupListAndSettingListList) {

            println("---------- 所属グループ一覧 ----------")
            println("id = ${group.groupList?.groupsId}, groupName = ${group.groupList?.groupName?.value}, groupPassword = ${group.groupList?.groupPassword?.value}")

            println("---------- 所属グループ設定 ----------")
            for (setting in group.groupSetting) {
                println("id = ${setting.id}, groupName = ${setting.settingKey.value}, groupPassword = ${setting.settingValue.value}")
            }
            println("\n")
        }
    }
}