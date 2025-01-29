package confirmation.user

import com.example.home.domain.value_object.user.UserId

fun main() {
    val execFlg = 1

    if (execFlg == 1) {

        val userSaveResult = userSaveConfirmation()
        if (userSaveResult != null) {
            loginConfirmation(userSaveResult)
            userUserInfoUpdateConfirmation(UserId(userSaveResult.userInfo?.userId!!))
            userSettingUpdateConfirmation(UserId(userSaveResult.userInfo.userId))
            userReferConfirmation(UserId(userSaveResult.userInfo.userId))
            userReferAllConfirmation()
            userDeleteConfirmation(UserId(userSaveResult.userInfo.userId))
        }
    }
}