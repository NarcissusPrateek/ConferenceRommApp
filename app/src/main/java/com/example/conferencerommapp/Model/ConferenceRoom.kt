package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName

data class ConferenceRoom(

    @SerializedName("CId")
    var conf_id: Int? = null,

   @SerializedName("CName")
    var conf_name: String? = null,

    @SerializedName("Capacity")
    var conf_capacity: String? = null,

    @SerializedName("BId")
    var building_id: String? = null,

    var Status: String? = null

)