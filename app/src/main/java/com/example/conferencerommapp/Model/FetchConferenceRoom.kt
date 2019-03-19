package com.example.conferencerommapp.Model

import java.time.chrono.ChronoLocalDateTime

data class FetchConferenceRoom (
    var FromTime : String? = null,
    var ToTime : String? = null,
    var BId : Int? = 0,
    var Capacity : Int? = 0

)