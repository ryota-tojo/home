package confirmation.user

fun main() {
    val execFlg = 1

    if (execFlg == 1) {

        val userSaveResult = userSaveConfirmation()
        if (userSaveResult != null) {
            loginConfirmation(userSaveResult)
            userUserInfoUpdateConfirmation(userSaveResult.userInfo?.userId!!)
            userSettingUpdateConfirmation(userSaveResult.userInfo.userId)
            userReferConfirmation(userSaveResult.userInfo.userId)
            userReferAllConfirmation()
            userDeleteConfirmation(userSaveResult.userInfo.userId)
        }
    }
}