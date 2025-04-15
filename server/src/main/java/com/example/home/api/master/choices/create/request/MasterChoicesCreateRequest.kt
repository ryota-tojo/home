package com.example.home.api.master.choices.create.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class MasterChoicesCreateRequest(

    @JsonProperty("item_type")
    @field:NotNull(message = "キー「item_type」が存在しません")
    @field:NotBlank(message = "キー「item_type」が未入力です")
    val itemType: String,

    @JsonProperty("item_no")
    @field:NotNull(message = "キー「item_no」が存在しません")
    val itemNo: Int,

    @JsonProperty("item_name_pc")
    @field:NotNull(message = "キー「item_name_pc」が存在しません")
    @field:NotBlank(message = "キー「item_name_pc」が未入力です")
    val itemNamePC: String,

    @JsonProperty("item_name_sp")
    @field:NotNull(message = "キー「item_name_sp」が存在しません")
    @field:NotBlank(message = "キー「item_name_sp」が未入力です")
    val itemNameSP: String,

    )
