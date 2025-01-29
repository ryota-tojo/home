package com.example.home.service.group

import com.example.home.data.GroupTestData
import com.example.home.domain.entity.group.GroupListAndSetting
import com.example.home.domain.entity.group.GroupListAndSettingList
import com.example.home.domain.entity.group.result.*
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.group.GroupListRepository
import com.example.home.domain.repository.group.GroupSettingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals

class GroupControlServiceTest {

    private val mockGroupListRepository: GroupListRepository = mockk()
    private val mockGroupSettingRepository: GroupSettingRepository = mockk()
    private val groupControlService = GroupControlService(mockGroupListRepository, mockGroupSettingRepository)

    @Test
    fun `refer_データあり`() {
        every { mockGroupListRepository.refer(GroupTestData.グループsID_正常値()) } returns GroupTestData.所属グループ一覧リスト_正常値()
        every {
            mockGroupSettingRepository.refer(
                GroupTestData.グループsID_正常値()
            )
        } returns GroupTestData.所属グループ設定リスト_正常値()

        val expected = GroupReferResult(
            result = ResponseCode.成功.code,
            groupListAndSetting = GroupListAndSetting(
                GroupTestData.所属グループ一覧リスト_正常値(),
                GroupTestData.所属グループ設定リスト_正常値()
            )
        )

        val result = groupControlService.refer(GroupTestData.グループsID_正常値())
        assertEquals(expected, result)

        verify { mockGroupListRepository.refer(GroupTestData.グループsID_正常値()) }
        verify { mockGroupSettingRepository.refer(GroupTestData.グループsID_正常値()) }
    }

    @Test
    fun `refer_データなし`() {
        every {
            mockGroupListRepository.refer(
                GroupTestData.グループsID_正常値()
            )
        } returns emptyList()
        every {
            mockGroupSettingRepository.refer(
                GroupTestData.グループsID_正常値()
            )
        } returns emptyList()

        val expected = GroupReferResult(
            result = ResponseCode.成功.code,
            groupListAndSetting = GroupListAndSetting(emptyList(), emptyList())
        )

        val result = groupControlService.refer(GroupTestData.グループsID_正常値())
        assertEquals(expected, result)

        verify { mockGroupListRepository.refer(GroupTestData.グループsID_正常値()) }
        verify { mockGroupSettingRepository.refer(GroupTestData.グループsID_正常値()) }
    }

    @Test
    fun `referAll_データあり`() {
        every {
            mockGroupListRepository.referAll()
        } returns GroupTestData.所属グループ一覧リスト_正常値()
        every {
            mockGroupSettingRepository.referAll()
        } returns GroupTestData.所属グループ設定リスト_正常値()

        val expected = GroupReferAllResult(
            result = ResponseCode.成功.code,
            groupListAndSettingListList = listOf(
                GroupListAndSettingList(
                    GroupTestData.グループ一覧_正常値(),
                    GroupTestData.所属グループ設定リスト_正常値()
                )
            )
        )

        val result = groupControlService.referAll()
        assertEquals(expected, result)

        verify { mockGroupListRepository.referAll() }
        verify { mockGroupSettingRepository.referAll() }
    }

    @Test
    fun `referAll_データなし`() {
        every {
            mockGroupListRepository.referAll()
        } returns emptyList()
        every {
            mockGroupSettingRepository.referAll()
        } returns emptyList()

        val expected = GroupReferAllResult(
            result = ResponseCode.成功.code,
            groupListAndSettingListList = listOf(
                GroupListAndSettingList(
                    null,
                    emptyList()
                )
            )
        )

        val result = groupControlService.referAll()
        assertEquals(expected, result)

        verify { mockGroupListRepository.referAll() }
        verify { mockGroupSettingRepository.referAll() }
    }

    @Test
    fun `save_成功`() {
        every { mockGroupListRepository.refer(GroupTestData.グループsID_正常値()) } returns emptyList()
        every {
            mockGroupListRepository.save(
                GroupTestData.グループsID_正常値(),
                GroupTestData.グループ名_正常値(),
                GroupTestData.グループパスワード_正常値()
            )
        } returns GroupTestData.グループ一覧_正常値()
        GroupTestData.所属グループ設定リスト_正常値().forEach { groupSetting ->
            every {
                mockGroupSettingRepository.save(
                    groupSetting.groupsId,
                    groupSetting.settingKey,
                    groupSetting.settingValue
                )
            } returns groupSetting
        }

        val expected = GroupSaveResult(
            result = ResponseCode.成功.code,
            groupList = GroupTestData.グループ一覧_正常値(),
            groupSettingList = GroupTestData.所属グループ設定リスト_正常値()
        )
        val result = groupControlService.save(
            GroupTestData.グループsID_正常値(),
            GroupTestData.グループ名_正常値(),
            GroupTestData.グループパスワード_正常値()
        )
        assertEquals(expected, result)

        verify(exactly = 1) { mockGroupListRepository.refer(GroupTestData.グループsID_正常値()) }
        verify(exactly = 1) {
            mockGroupListRepository.save(
                GroupTestData.グループsID_正常値(),
                GroupTestData.グループ名_正常値(),
                GroupTestData.グループパスワード_正常値()
            )
        }
        GroupTestData.所属グループ設定リスト_正常値().forEach { groupSetting ->
            verify(exactly = 1) {
                mockGroupSettingRepository.save(
                    groupSetting.groupsId,
                    groupSetting.settingKey,
                    groupSetting.settingValue
                )
            }
        }
    }

    @Test
    fun `save_失敗_所属グループIDバリデーションエラー_SYMBOL`() {
        val expected = GroupSaveResult(
            result = ResponseCode.バリデーションエラー.code,
            groupList = null,
            groupSettingList = null
        )
        val result = groupControlService.save(
            GroupTestData.グループsID_バリデーションエラー値_SYMBOL(),
            GroupTestData.グループ名_正常値(),
            GroupTestData.グループパスワード_正常値()
        )
        assertEquals(expected, result)
    }

    @Test
    fun `save_失敗_所属グループIDバリデーションエラー_WORD`() {
        val expected = GroupSaveResult(
            result = ResponseCode.バリデーションエラー.code,
            groupList = null,
            groupSettingList = null
        )
        val result = groupControlService.save(
            GroupTestData.グループsID_バリデーションエラー値_WORD(),
            GroupTestData.グループ名_正常値(),
            GroupTestData.グループパスワード_正常値()
        )
        assertEquals(expected, result)
    }

    @Test
    fun `save_失敗_所属グループ名バリデーションエラー_SYMBOL`() {
        val expected = GroupSaveResult(
            result = ResponseCode.バリデーションエラー.code,
            groupList = null,
            groupSettingList = null
        )
        val result = groupControlService.save(
            GroupTestData.グループsID_正常値(),
            GroupTestData.グループ名_バリデーションエラー値_SYMBOL(),
            GroupTestData.グループパスワード_正常値()
        )
        assertEquals(expected, result)
    }

    @Test
    fun `save_失敗_所属グループ名バリデーションエラー_WORD`() {
        val expected = GroupSaveResult(
            result = ResponseCode.バリデーションエラー.code,
            groupList = null,
            groupSettingList = null
        )
        val result = groupControlService.save(
            GroupTestData.グループsID_正常値(),
            GroupTestData.グループ名_バリデーションエラー値_WORD(),
            GroupTestData.グループパスワード_正常値()
        )
        assertEquals(expected, result)
    }

    @Test
    fun `save_失敗_既存データ`() {
        every { mockGroupListRepository.refer(GroupTestData.グループsID_正常値()) } returns GroupTestData.所属グループ一覧リスト_正常値()
        val expected = GroupSaveResult(
            result = ResponseCode.重複エラー.code,
            groupList = null,
            groupSettingList = null
        )
        val result = groupControlService.save(
            GroupTestData.グループsID_正常値(),
            GroupTestData.グループ名_正常値(),
            GroupTestData.グループパスワード_正常値()
        )
        assertEquals(expected, result)

        verify(exactly = 1) { mockGroupListRepository.refer(GroupTestData.グループsID_正常値()) }
        verify(exactly = 0) { mockGroupListRepository.save(any(), any(), any()) }
        verify(exactly = 0) { mockGroupSettingRepository.save(any(), any(), any()) }
    }

    @Test
    fun `listUpdate_成功`() {
        every { mockGroupListRepository.refer(GroupTestData.グループsID_正常値()) } returns GroupTestData.所属グループ一覧リスト_正常値()
        every {
            mockGroupListRepository.update(
                GroupTestData.グループsID_正常値(),
                GroupTestData.グループ名_正常値(),
                GroupTestData.グループパスワード_正常値()
            )
        } returns 1

        val expected = GroupListUpdateResult(
            result = ResponseCode.成功.code,
            1
        )
        val result = groupControlService.listUpdate(
            GroupTestData.グループsID_正常値(),
            GroupTestData.グループ名_正常値(),
            GroupTestData.グループパスワード_正常値()
        )
        assertEquals(expected, result)

        verify(exactly = 1) { mockGroupListRepository.refer(GroupTestData.グループsID_正常値()) }
        verify(exactly = 1) {
            mockGroupListRepository.update(
                GroupTestData.グループsID_正常値(),
                GroupTestData.グループ名_正常値(),
                GroupTestData.グループパスワード_正常値()
            )
        }
    }

    @Test
    fun `listUpdate_失敗_所属グループIDバリデーションエラー_SYMBOL`() {
        val expected = GroupListUpdateResult(
            result = ResponseCode.バリデーションエラー.code,
            0
        )
        val result = groupControlService.listUpdate(
            GroupTestData.グループsID_バリデーションエラー値_SYMBOL(),
            GroupTestData.グループ名_正常値(),
            GroupTestData.グループパスワード_正常値()
        )
        assertEquals(expected, result)
    }

    @Test
    fun `listUpdate_失敗_所属グループIDバリデーションエラー_WORD`() {
        val expected = GroupListUpdateResult(
            result = ResponseCode.バリデーションエラー.code,
            0
        )
        val result = groupControlService.listUpdate(
            GroupTestData.グループsID_バリデーションエラー値_WORD(),
            GroupTestData.グループ名_正常値(),
            GroupTestData.グループパスワード_正常値()
        )
        assertEquals(expected, result)
    }

    @Test
    fun `listUpdate_失敗_所属グループ名バリデーションエラー_SYMBOL`() {
        val expected = GroupListUpdateResult(
            result = ResponseCode.バリデーションエラー.code,
            0
        )
        val result = groupControlService.listUpdate(
            GroupTestData.グループsID_正常値(),
            GroupTestData.グループ名_バリデーションエラー値_SYMBOL(),
            GroupTestData.グループパスワード_正常値()
        )
        assertEquals(expected, result)
    }

    @Test
    fun `listUpdate_失敗_所属グループ名バリデーションエラー_WORD`() {
        val expected = GroupListUpdateResult(
            result = ResponseCode.バリデーションエラー.code,
            0
        )
        val result = groupControlService.listUpdate(
            GroupTestData.グループsID_正常値(),
            GroupTestData.グループ名_バリデーションエラー値_WORD(),
            GroupTestData.グループパスワード_正常値()
        )
        assertEquals(expected, result)
    }

    @Test
    fun `listUpdate_失敗_データ不在エラー`() {
        every { mockGroupListRepository.refer(GroupTestData.グループsID_正常値()) } returns emptyList()

        val expected = GroupListUpdateResult(
            result = ResponseCode.データ不在エラー.code,
            0
        )
        val result = groupControlService.listUpdate(
            GroupTestData.グループsID_正常値(),
            GroupTestData.グループ名_正常値(),
            GroupTestData.グループパスワード_正常値()
        )
        assertEquals(expected, result)

        verify(exactly = 1) { mockGroupListRepository.refer(GroupTestData.グループsID_正常値()) }
    }

    @Test
    fun `settingUpdate_成功`() {
        every { mockGroupSettingRepository.refer(GroupTestData.グループsID_正常値()) } returns GroupTestData.所属グループ設定リスト_正常値()
        every {
            mockGroupSettingRepository.update(
                GroupTestData.グループsID_正常値(),
                GroupTestData.グループ設定キー_正常値(),
                GroupTestData.グループ設定値_正常値()
            )
        } returns 1

        val expected = GroupSettingUpdateResult(
            result = ResponseCode.成功.code,
            1
        )
        val result = groupControlService.settingUpdate(
            GroupTestData.グループsID_正常値(),
            GroupTestData.グループ設定キー_正常値(),
            GroupTestData.グループ設定値_正常値()
        )
        assertEquals(expected, result)

        verify(exactly = 1) { mockGroupSettingRepository.refer(GroupTestData.グループsID_正常値()) }
        verify(exactly = 1) {
            mockGroupSettingRepository.update(
                GroupTestData.グループsID_正常値(),
                GroupTestData.グループ設定キー_正常値(),
                GroupTestData.グループ設定値_正常値()
            )
        }
    }

    @Test
    fun `settingUpdate_失敗_所属グループIDバリデーションエラー_SYMBOL`() {
        val expected = GroupSettingUpdateResult(
            result = ResponseCode.バリデーションエラー.code,
            0
        )
        val result = groupControlService.settingUpdate(
            GroupTestData.グループsID_バリデーションエラー値_SYMBOL(),
            GroupTestData.グループ設定キー_正常値(),
            GroupTestData.グループ設定値_正常値()
        )
        assertEquals(expected, result)
    }

    @Test
    fun `settingUpdate_失敗_所属グループIDバリデーションエラー_WORD`() {
        val expected = GroupSettingUpdateResult(
            result = ResponseCode.バリデーションエラー.code,
            0
        )
        val result = groupControlService.settingUpdate(
            GroupTestData.グループsID_バリデーションエラー値_WORD(),
            GroupTestData.グループ設定キー_正常値(),
            GroupTestData.グループ設定値_正常値()
        )
        assertEquals(expected, result)
    }

    @Test
    fun `settingUpdate_失敗_所属グループ設定キーバリデーションエラー_SYMBOL`() {
        val expected = GroupSettingUpdateResult(
            result = ResponseCode.バリデーションエラー.code,
            0
        )
        val result = groupControlService.settingUpdate(
            GroupTestData.グループsID_正常値(),
            GroupTestData.グループ設定キー_バリデーションエラー値_SYMBOL(),
            GroupTestData.グループ設定値_正常値()
        )
        assertEquals(expected, result)
    }

    @Test
    fun `settingUpdate_失敗_所属グループ設定値バリデーションエラー_SYMBOL`() {
        val expected = GroupSettingUpdateResult(
            result = ResponseCode.バリデーションエラー.code,
            0
        )
        val result = groupControlService.settingUpdate(
            GroupTestData.グループsID_正常値(),
            GroupTestData.グループ設定キー_正常値(),
            GroupTestData.グループ設定値_バリデーションエラー値_SYMBOL()
        )
        assertEquals(expected, result)
    }

    @Test
    fun `settingUpdate_失敗_データ不在エラー`() {
        every { mockGroupSettingRepository.refer(GroupTestData.グループsID_正常値()) } returns emptyList()

        val expected = GroupSettingUpdateResult(
            result = ResponseCode.データ不在エラー.code,
            0
        )
        val result = groupControlService.settingUpdate(
            GroupTestData.グループsID_正常値(),
            GroupTestData.グループ設定キー_正常値(),
            GroupTestData.グループ設定値_正常値()
        )
        assertEquals(expected, result)

        verify(exactly = 1) { mockGroupSettingRepository.refer(GroupTestData.グループsID_正常値()) }
    }

    @Test
    fun `deleteUpdate_成功`() {
        every { mockGroupListRepository.refer(GroupTestData.グループsID_正常値()) } returns GroupTestData.所属グループ一覧リスト_正常値()
        every { mockGroupSettingRepository.refer(GroupTestData.グループsID_正常値()) } returns GroupTestData.所属グループ設定リスト_正常値()
        every { mockGroupListRepository.delete(GroupTestData.グループsID_正常値()) } returns 1
        every { mockGroupSettingRepository.delete(GroupTestData.グループsID_正常値()) } returns 1

        val expected = GroupDeleteResult(
            result = ResponseCode.成功.code,
            1,
            1
        )

        val result = groupControlService.delete(
            GroupTestData.グループsID_正常値()
        )
        assertEquals(expected, result)

        verify(exactly = 1) { mockGroupListRepository.refer(GroupTestData.グループsID_正常値()) }
        verify(exactly = 1) { mockGroupSettingRepository.refer(GroupTestData.グループsID_正常値()) }
        verify(exactly = 1) { mockGroupListRepository.delete(GroupTestData.グループsID_正常値()) }
        verify(exactly = 1) { mockGroupSettingRepository.delete(GroupTestData.グループsID_正常値()) }
    }

    @Test
    fun `delete_失敗_所属グループIDバリデーションエラー_SYMBOL`() {
        val expected = GroupDeleteResult(
            result = ResponseCode.バリデーションエラー.code,
            0,
        )
        val result = groupControlService.delete(
            GroupTestData.グループsID_バリデーションエラー値_SYMBOL(),
        )
        assertEquals(expected, result)
    }

    @Test
    fun `delete_失敗_所属グループIDバリデーションエラー_WORD`() {
        val expected = GroupDeleteResult(
            result = ResponseCode.バリデーションエラー.code,
            0,
        )
        val result = groupControlService.delete(
            GroupTestData.グループsID_バリデーションエラー値_WORD(),
        )
        assertEquals(expected, result)
    }

    @Test
    fun `deleteUpdate_失敗_所属グループ一覧_データ不在エラー`() {
        every { mockGroupListRepository.refer(GroupTestData.グループsID_正常値()) } returns emptyList()

        val expected = GroupDeleteResult(
            result = ResponseCode.データ不在エラー.code,
            0,
            0
        )

        val result = groupControlService.delete(
            GroupTestData.グループsID_正常値()
        )
        assertEquals(expected, result)

        verify(exactly = 1) { mockGroupListRepository.refer(GroupTestData.グループsID_正常値()) }
    }

    @Test
    fun `deleteUpdate_失敗_所属グループ設定_データ不在エラー`() {
        every { mockGroupListRepository.refer(GroupTestData.グループsID_正常値()) } returns GroupTestData.所属グループ一覧リスト_正常値()
        every { mockGroupSettingRepository.refer(GroupTestData.グループsID_正常値()) } returns emptyList()

        val expected = GroupDeleteResult(
            result = ResponseCode.データ不在エラー.code,
            0,
            0
        )

        val result = groupControlService.delete(
            GroupTestData.グループsID_正常値()
        )
        assertEquals(expected, result)

        verify(exactly = 1) { mockGroupListRepository.refer(GroupTestData.グループsID_正常値()) }
        verify(exactly = 1) { mockGroupSettingRepository.refer(GroupTestData.グループsID_正常値()) }
    }

}
