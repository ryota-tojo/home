package confirmation.user

import com.example.home.domain.entity.user.result.UserSaveResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.GroupPassword
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.*
import com.example.home.service.user.UserControlService
import confirmation.DbConnectConfirmation

fun userSaveConfirmation(): UserSaveResult? {
    // 実行フラグ
    val execFlg = 1

    if (execFlg == 1) {
        println("\n＊＊＊＊＊＊＊＊＊ 処理開始：ユーザー登録 ＊＊＊＊＊＊＊＊＊\n")

        //  リポジトリ
        val dbConnectConfirmation: DbConnectConfirmation = DbConnectConfirmation()
        val userControlService: UserControlService = UserControlService()

        // DB接続
        dbConnectConfirmation.dbConnect()

        // 確認用のデータ定義
        val groupsId = GroupsId("maintenance")
        val groupPassword = GroupPassword("adminadmin")
        val userName = UserName("test_user2")
        val userPassword = UserPassword("1234567890")
        val userPermission = UserPermission(0)
        val userApprovalFlg = UserApprovalFlg(1)
        val userDeleteFlg = UserDeleteFlg(0)

        // 確認対象のサービスを呼び出す
        val res = userControlService.save(
            groupsId,
            groupPassword,
            userName,
            userPassword,
            userPermission,
            userApprovalFlg,
            userDeleteFlg
        )
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
            println(
                "所属グループID：${res.groupInfo?.groupsId?.value}, " +
                        "ユーザーID：${res.groupInfo?.userId?.value}, " +
                        "リーダーフラグ：${res.groupInfo?.userLeaderFlg?.value}, " +
                        "作成日：${res.groupInfo?.createDate}, " +
                        "更新日：${res.groupInfo?.updateDate}"
            )
        }
        return res
    }
    return null
}