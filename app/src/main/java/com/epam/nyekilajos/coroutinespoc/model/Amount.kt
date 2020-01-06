package com.epam.nyekilajos.coroutinespoc.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Amount(

    @SerializedName("value")
    val value: BigDecimal?,

    @SerializedName("unit")
    val unit: String?
)
