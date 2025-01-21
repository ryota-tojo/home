import com.example.home.config.DbInitializer
import com.example.home.domain.user.LoginInfo
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserName
import com.example.home.domain.value_object.user.UserPassword
import com.example.home.service.user.LoginService

fun main() {

    val dbInitializer: DbInitializer = DbInitializer()
    dbInitializer.initialize()

    val loginService: LoginService = LoginService()
    val res = loginService.login(
        LoginInfo(
            GroupsId("maintenance"),
            UserName("admin"),
            UserPassword("adminadmin")
        )
    )

    println(res)

}

