package confirmation.category

fun main() {
    val execFlg = 1

    if (execFlg == 1) {
        val category = categorySaveConfirmation()
        if (category != null) {
            categoryReferConfirmation(category.groupsId)
        }


    }
}