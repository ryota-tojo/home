package confirmation.group

fun main() {
    val execFlg = 1

    if (execFlg == 1) {
        groupSaveConfirmation()
        groupListUpdateConfirmation()
        groupSettingUpdateConfirmation()
        groupReferConfirmation()
        groupDeleteConfirmation()
    }
}