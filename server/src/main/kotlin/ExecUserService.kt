import com.example.home.datasource.group.GroupInfoRepositoryImpl
import com.example.home.datasource.user.UserInfoRepositoryImpl
import com.example.home.datasource.user.UserSettingRepositoryImpl
import com.example.home.domain.repository.group.GroupInfoRepository
import com.example.home.domain.repository.user.UserInfoRepository
import com.example.home.domain.repository.user.UserSettingRepository
import com.example.home.domain.value_object.user.*
import com.example.home.service.user.UserControlService
import com.example.home.util.ParseLocalDateTime.parseLocalDateTime
import confirmation.DbConnectConfirmation


fun main() {

    // 実行フラグ
    val execFlg = 1

    if (execFlg == 1) {
        println("\n＊＊＊＊＊＊＊＊＊ 処理開始 ＊＊＊＊＊＊＊＊＊\n")

        //  リポジトリ
        val dbConnectConfirmation: DbConnectConfirmation = DbConnectConfirmation()

        val userInfoRepository: UserInfoRepository = UserInfoRepositoryImpl()
        val userSettingRepository: UserSettingRepository = UserSettingRepositoryImpl()
        val groupInfoRepository: GroupInfoRepository = GroupInfoRepositoryImpl()

        val userControlService: UserControlService =
            UserControlService(
                userInfoRepository,
                userSettingRepository,
                groupInfoRepository
            )

        // DB接続
        dbConnectConfirmation.dbConnect()

        // 設定
        val requestUserName = UserName("user_007")
        val requestPassword = UserPassword("1234567890")
        val requestPermission = UserPermission(0)
        val requestApproval = UserApprovalFlg(0)
        val requestDelete = UserDeleteFlg(0)

//        // 確認対象のサービスを呼び出す
//        val res = userControlService.save(
//            requestUserName,
//            requestPassword,
//            requestPermission,
//            requestApproval,
//            requestDelete
//        )
//
//        println("---------- 処理結果 ----------")
//        println(res)

        println(parseLocalDateTime("2025-03-29T13:25:36.978694"))

    }

}