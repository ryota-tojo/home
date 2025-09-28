//package com.example.home.api.template.search.create
//
//import com.example.home.HomeApplication
//import com.example.home.domain.entity.template.ShoppingSearchTemplate
//import com.example.home.domain.entity.template.result.ShoppingSearchTemplateSaveResult
//import com.example.home.domain.value_object.category.CategoryId
//import com.example.home.domain.value_object.etc.Amount
//import com.example.home.domain.value_object.group.GroupsId
//import com.example.home.domain.value_object.member.MemberId
//import com.example.home.domain.value_object.shopping.ShoppingPayment
//import com.example.home.domain.value_object.shopping.ShoppingRemarks
//import com.example.home.domain.value_object.shopping.ShoppingSettlement
//import com.example.home.domain.value_object.shopping.ShoppingType
//import com.example.home.domain.value_object.template.TemplateDeleteFlg
//import com.example.home.domain.value_object.template.TemplateId
//import com.example.home.domain.value_object.template.TemplateName
//import com.example.home.domain.value_object.template.TemplateNo
//import com.example.home.domain.value_object.template.TemplateUseFlg
//import com.example.home.domain.value_object.template.TmpId
//import com.example.home.service.template.ShoppingSearchTemplateService
//import com.fasterxml.jackson.databind.ObjectMapper
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.mock.mockito.MockBean
//import org.springframework.http.MediaType
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
//import spock.lang.Specification
//
//@SpringBootTest(classes = HomeApplication.class)
//@AutoConfigureMockMvc
//class TemplateSearchCreateApiTest extends Specification {
//
//    @Autowired
//    MockMvc mockMvc
//
//    @MockBean
//    ShoppingSearchTemplateService templateSearchService
//
//    def "POST api/template/search/create"() {
//        given:
//        def dummyId = new TmpId(1)
//        def dummyGroupsId = new GroupsId("home")
//        def dummyTemplateNo = new TemplateNo(1)
//        def dummyTemplateId = new TemplateId("template_001")
//        def dummyTemplateName = new TemplateName("日用品テンプレート")
//        def dummyMemberId = new MemberId(5)
//        def dummyCategoryId = new CategoryId(18)
//        def dummyShoppingType = new ShoppingType(1)
//        def dummyShoppingPayment = new ShoppingPayment(1)
//        def dummyShoppingSettlement = new ShoppingSettlement(0)
//        def dummyShoppingMinAmount = new Amount(0)
//        def dummyShoppingMaxAmount = new Amount(3500)
//        def dummyShoppingRemarks = new ShoppingRemarks("毎月の定期購入品")
//        def dummyTemplateUseFlg = new TemplateUseFlg(1)
//        def dummyTemplateDeletedFlg = new TemplateDeleteFlg(0)
//        def shoppingSearchTemplate = new ShoppingSearchTemplate(
//                dummyId,
//                dummyGroupsId,
//                dummyTemplateNo,
//                dummyTemplateId,
//                dummyTemplateName,
//                dummyMemberId,
//                dummyCategoryId,
//                dummyShoppingType,
//                dummyShoppingPayment,
//                dummyShoppingSettlement,
//                dummyShoppingMinAmount,
//                dummyShoppingMaxAmount,
//                dummyShoppingRemarks,
//                dummyTemplateUseFlg,
//                dummyTemplateDeletedFlg
//        )
//        def requestBody = [
//                groups_id      : "home",
//                template_id    : "template_001",
//                "template_name": "日用品テンプレート",
//                "member_id"    : 5,
//                "category_id"  : 18,
//                "type"         : 1,
//                "payment"      : 1,
//                "settlement"   : 0,
//                "min_amount"   : 0,
//                "max_amount"   : 3500,
//                "remarks"      : "毎月の定期購入品",
//                "use"          : 1
//        ]
//        def requestJson = new ObjectMapper().writeValueAsString(requestBody)
//
//        and:
//        1 * templateSearchService.save(_, _, _, _, _, _, _, _, _, _, _, _, _)
//                >> new ShoppingSearchTemplateSaveResult(
//                "success",
//                shoppingSearchTemplate
//        )
//
//        expect:
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/api/template/search/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson)
//        )
//                .andExpect(jsonPath('$.status').value('success'))
//                .andExpect(jsonPath('$.message').value('成功'))
//                .andExpect(jsonPath('$.data.template.templateId').value('template_001'))
//    }
//}
