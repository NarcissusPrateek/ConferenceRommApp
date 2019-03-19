package com.example.conferencerommapp.Model

data class Booking (

    var Email: String? = null,

    var CId: Int? = 0,

    var BId: Int? = 0,

    var FromTime: String? = null,

    var CName: String? = null,

    var ToTime: String? = null,

    var Purpose: String? = null,

    var CCMail: String? = null



) {
    override fun toString(): String {
        return "Booking(Email=$Email, CId=$CId, BId=$BId, FromTime=$FromTime, ToTime=$ToTime, Purpose=$Purpose, CName=$CName, CCMail=$CCMail)"
    }
}