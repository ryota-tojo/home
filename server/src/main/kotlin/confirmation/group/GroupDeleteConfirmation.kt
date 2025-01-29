package confirmation.group

import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.group.GroupControlService
import confirmation.DbConnectConfirmation

fun groupDeleteConfirmation() {
    // 実行フラグ
    val execFlg = 1

    if (execFlg == 1) {
        println("\n＊＊＊＊＊＊＊＊＊ 処理開始：グループ削除 ＊＊＊＊＊＊＊＊＊\n")

        //  リポジトリ
        val dbConnectConfirmation: DbConnectConfirmation = DbConnectConfirmation()
        val groupControlService: GroupControlService = GroupControlService()

        // DB接続
        dbConnectConfirmation.dbConnect()

        // 確認用のデータ定義
        val groupsId = GroupsId("test_groups_id")

        // 確認対象のサービスを呼び出す
        val res = groupControlService.delete(groupsId)
        println("---------- 処理結果 ----------")
        println(res.result)

        println("---------- 処理行 ----------")
        println("所属グループ一覧：" + res.groupListDeletedResult)
        println("所属グループ設定：" + res.groupSettingDeletedResult)

    }
}