package com.example.home.api.group.delete.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GroupDeleteResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(

        @JsonProperty("group_list_delete_rows")
        val groupListDeletedRows: Int? = 0,

        @JsonProperty("group_setting_delete_rows")
        val groupSettingDeletedRows: Int? = 0,

        @JsonProperty("group_info_delete_rows")
        val groupInfoDelRows: Int? = 0,

        @JsonProperty("category_delete_rows")
        val categoryDelRows: Int? = 0,

        @JsonProperty("member_delete_rows")
        val memberDelRows: Int? = 0,

        @JsonProperty("shopping_input_template_delete_rows")
        val shoppingInputTemplateDelRows: Int? = 0,

        @JsonProperty("shopping_search_template_delete_rows")
        val shoppingSearchTemplateDelRows: Int? = 0,

        @JsonProperty("shopping_entry_template_delete_rows")
        val shoppingEntryTemplateDelRows: Int? = 0,

        @JsonProperty("budgets_delete_rows")
        val budgetsDelRows: Int? = 0,

        @JsonProperty("shopping_delete_rows")
        val shoppingDelRows: Int? = 0,

        @JsonProperty("fixed_delete_rows")
        val fixedDelRows: Int? = 0,

        @JsonProperty("comment_delete_rows")
        val commentDelRows: Int? = 0,

        @JsonProperty("communication_delete_rows")
        val communicationDelRows: Int? = 0,

        )

}