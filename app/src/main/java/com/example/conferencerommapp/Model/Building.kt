package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName

data class Building (
    @SerializedName("BId")
    var building_id: String? = null,

    @SerializedName("BName")
    var building_name: String? = null,

    @SerializedName("Place")
    var building_place: String? = null
)

