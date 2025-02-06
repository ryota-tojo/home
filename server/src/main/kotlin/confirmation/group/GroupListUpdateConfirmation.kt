package confirmation.group

import com.example.home.domain.value_object.group.GroupName
import com.example.home.domain.value_object.group.GroupPassword
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.group.GroupControlService
import confirmation.DbConnectConfirmation

fun groupListUpdateConfirmation() {
    // 実行フラグ
    val execFlg = 1

    if (execFlg == 1) {
        println("\n＊＊＊＊＊＊＊＊＊ 処理開始：グループ一覧更新 ＊＊＊＊＊＊＊＊＊\n")

        //  リポジトリ
        val dbConnectConfirmation: DbConnectConfirmation = DbConnectConfirmation()
        val groupControlService: GroupControlService = GroupControlService()

        // DB接続
        dbConnectConfirmation.dbConnect()

        // 確認用のデータ定義
        val groupsId = GroupsId("test_groups_id")
        val groupName = GroupName("test_group_name_update")
        val groupPassword = GroupPassword("test_group_password_update")

        // 確認対象のサービスを呼び出す
        val res = groupControlService.listUpdate(groupsId, groupName, groupPassword)
        println("---------- 処理結果 ----------")
        println(res.result)

        println("---------- 処理行 ----------")
        println(res.updateRow)

    }
}