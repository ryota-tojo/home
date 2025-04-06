package com.example.home.api.communication.update.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class CommunicationUpdateRequest(
    @JsonProperty("gift_id")
    @field:NotNull(message = "「gift_id」は必須項目です")
    val giftId: Int,

    @JsonProperty("date")
    @field:Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "「date」の形式は「yyyy-MM-dd」で入力してください")
    val date: String?,

    @JsonProperty("from")
    @field:Size(max = 64, message = "「from」は64文字以内で入力してください")
    val from: String?,

    @JsonProperty("to")
    @field:Size(max = 64, message = "「to」は64文字以内で入力してください")
    val to: String?,

    @JsonProperty("item")
    @field:Size(max = 64, message = "「item」は64文字以内で入力してください")
    val item: String?,

    @JsonProperty("amount")
    val amount: Int?,

    @JsonProperty("remarks")
    val remarks: String?,

    @JsonProperty("rtn_date")
    @field:Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "「date」の形式は「yyyy-MM-dd」で入力してください")
    val rtnDate: String?,

    @JsonProperty("rtn_item")
    @field:Size(max = 64, message = "「rtn_item」は64文字以内で入力してください")
    val rtnItem: String?,

    @JsonProperty("rtn_amount")
    val rtnAmount: Int?,

    @JsonProperty("rtn_remarks")
    val rtnRemarks: String?,

    @JsonProperty("return_flag")
    val returnFlg: Int?,
)