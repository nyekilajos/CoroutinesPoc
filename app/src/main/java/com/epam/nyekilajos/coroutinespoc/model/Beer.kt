package com.epam.nyekilajos.coroutinespoc.model

import com.google.gson.annotations.SerializedName

data class Beer(

    @SerializedName("id")
    val id: Int?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("tagline")
    val tagline: String? = null,

    @SerializedName("first_brewed")
    val firstBrewed: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("image_url")
    val imageUrl: String? = null,

    @SerializedName("abv")
    val abv: Double? = null,

    @SerializedName("ibu")
    val ibu: Double? = null,

    @SerializedName("target_fg")
    val targetFg: Double? = null,

    @SerializedName("target_og")
    val targetOg: Double? = null,

    @SerializedName("ebc")
    val ebc: Double? = null,

    @SerializedName("srm")
    val srm: Double? = null,

    @SerializedName("ph")
    val ph: Double? = null,

    @SerializedName("attenuation_level")
    val attenuationLevel: Double? = null,

    @SerializedName("volume")
    val volume: Amount? = null,

    @SerializedName("boil_volume")
    val boilVolume: Amount? = null,

    @SerializedName("method")
    val method: Method? = null,

    @SerializedName("ingredients")
    val ingredients: Ingredients? = null,

    @SerializedName("food_pairing")
    val foodPairing: List<String?>? = null,

    @SerializedName("brewers_tips")
    val brewersTips: String? = null,

    @SerializedName("contributed_by")
    val contributedBy: String? = null
)
