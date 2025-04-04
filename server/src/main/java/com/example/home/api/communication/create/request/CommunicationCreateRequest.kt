package com.example.home.api.communication.create.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CommunicationCreateRequest(
    @JsonProperty("groups_id")
    @field:NotNull(message = "「groups_id」は必須項目です")
    @field:NotBlank(message = "「groups_id」を入力してください")
    @field:Size(max = 64, message = "「groups_id」は64文字以内で入力してください")
    val groupsId: String,

    @JsonProperty("date")
    @field:NotNull(message = "「date」は必須項目です")
    @field:NotBlank(message = "「date」を入力してください")
    val date: String,

    @JsonProperty("from")
    @field:NotNull(message = "「from」は必須項目です")
    @field:NotBlank(message = "「from」を入力してください")
    @field:Size(max = 64, message = "「from」は64文字以内で入力してください")
    val from: String,

    @JsonProperty("to")
    @field:NotNull(message = "「to」は必須項目です")
    @field:Size(max = 64, message = "「to」は64文字以内で入力してください")
    val to: String,

    @JsonProperty("item")
    @field:NotNull(message = "「item」は必須項目です")
    @field:Size(max = 64, message = "「item」は64文字以内で入力してください")
    val item: String,

    @JsonProperty("amount")
    @field:NotNull(message = "「amount」は必須項目です")
    val amount: Int,

    @JsonProperty("remarks")
    @field:NotNull(message = "「remarks」は必須項目です")
    val remarks: String,

    @JsonProperty("rtn_date")
    @field:NotNull(message = "「rtn_date」は必須項目です")
    val rtnDate: String,

    @JsonProperty("rtn_item")
    @field:NotNull(message = "「rtn_item」は必須項目です")
    @field:Size(max = 64, message = "「rtn_item」は64文字以内で入力してください")
    val rtnItem: String,

    @JsonProperty("rtn_amount")
    @field:NotNull(message = "「rtn_amount」は必須項目です")
    val rtnAmount: Int,

    @JsonProperty("rtn_remarks")
    @field:NotNull(message = "「rtn_remarks」は必須項目です")
    val rtnRemarks: String,

    @JsonProperty("return_flag")
    @field:NotNull(message = "「return_flag」は必須項目です")
    val returnFlg: Int,
)