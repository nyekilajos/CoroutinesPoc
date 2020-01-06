package com.epam.nyekilajos.coroutinespoc.model

import com.google.gson.annotations.SerializedName

data class Fermentation(

    @SerializedName("temp")
    val temp: Amount?
)
