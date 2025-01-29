import com.example.home.data.LoginTestData
import com.example.home.domain.entity.user.result.UserLoginResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.group.GroupInfoRepository
import com.example.home.domain.repository.user.LoginRepository
import com.example.home.domain.value_object.user.UserId
import com.example.home.service.user.LoginService
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class LoginServiceTest {

    private val mockGroupInfoRepository: GroupInfoRepository = mockk()
    private val mockLoginRepository: LoginRepository = mockk()
    private val loginService = LoginService(
        groupInfoRepository = mockGroupInfoRepository,
        loginRepository = mockLoginRepository
    )

    @Test
    fun `ログイン成功`() {
        every {
            mockLoginRepository.refer(
                LoginTestData.ログイン情報_正常値().username,
                LoginTestData.ログイン情報_正常値().password
            )
        } returns LoginTestData.ユーザー情報_正常値()
        every {
            mockGroupInfoRepository.refer(
                LoginTestData.ログイン情報_正常値().groupsId,
                UserId(1)
            )
        } returns listOf(mockk())

        val result = loginService.login(LoginTestData.ログイン情報_正常値())
        assertEquals(UserLoginResult(ResponseCode.成功.code, true), result)
    }

    @Test
    fun `ログイン失敗_ユーザー名_パスワードの照合`() {
        every {
            mockLoginRepository.refer(
                LoginTestData.ログイン情報_正常値().username,
                LoginTestData.ログイン情報_正常値().password
            )
        } returns null
        every {
            mockGroupInfoRepository.refer(
                LoginTestData.ログイン情報_正常値().groupsId,
                UserId(1)
            )
        } returns listOf(mockk())

        val result = loginService.login(LoginTestData.ログイン情報_正常値())
        assertEquals(UserLoginResult(ResponseCode.ログインエラー_データ照合.code, false), result)
    }

    @Test
    fun `ログイン失敗_削除ユーザー`() {
        every {
            mockLoginRepository.refer(
                LoginTestData.ログイン情報_正常値().username,
                LoginTestData.ログイン情報_正常値().password
            )
        } returns LoginTestData.ユーザー情報_削除済み()
        every {
            mockGroupInfoRepository.refer(
                LoginTestData.ログイン情報_正常値().groupsId,
                UserId(1)
            )
        } returns listOf(mockk())

        val result = loginService.login(LoginTestData.ログイン情報_正常値())
        assertEquals(UserLoginResult(ResponseCode.ログインエラー_削除済ユーザー.code, false), result)
    }

    @Test
    fun `ログイン失敗_未承認ユーザー`() {
        every {
            mockLoginRepository.refer(
                LoginTestData.ログイン情報_正常値().username,
                LoginTestData.ログイン情報_正常値().password
            )
        } returns LoginTestData.ユーザー情報_未承認()
        every {
            mockGroupInfoRepository.refer(
                LoginTestData.ログイン情報_正常値().groupsId,
                UserId(1)
            )
        } returns listOf(mockk())

        val result = loginService.login(LoginTestData.ログイン情報_正常値())
        assertEquals(UserLoginResult(ResponseCode.ログインエラー_未承認ユーザー.code, false), result)
    }

    @Test
    fun `ログイン失敗_グループ未所属`() {
        every {
            mockLoginRepository.refer(
                LoginTestData.ログイン情報_正常値().username,
                LoginTestData.ログイン情報_正常値().password
            )
        } returns LoginTestData.ユーザー情報_正常値()
        every {
            mockGroupInfoRepository.refer(
                LoginTestData.ログイン情報_正常値().groupsId,
                UserId(1)
            )
        } returns listOf()

        val result = loginService.login(LoginTestData.ログイン情報_正常値())
        assertEquals(UserLoginResult(ResponseCode.ログインエラー_所属グループ不正.code, false), result)
    }

}

