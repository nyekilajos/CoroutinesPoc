package com.epam.nyekilajos.coroutinespoc.model

import com.google.gson.annotations.SerializedName

data class Malt(

    @SerializedName("name")
    val name: String?,

    @SerializedName("amount")
    val amount: Amount?
)
