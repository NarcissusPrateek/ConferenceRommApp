package com.example.conferencerommapp.services

import androidx.core.content.res.FontResourcesParserCompat
import com.example.conferencerommapp.Blocked
import com.example.conferencerommapp.BuildingConference
import com.example.conferencerommapp.BuildingT
import com.example.conferencerommapp.Helper.Unblock
import com.example.conferencerommapp.Model.*
import com.example.conferencerommapp.addConferenceRoom
import com.example.myapplication.Models.ConferenceList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ConferenceService  {

    @GET("api/Building")
    fun getBuildingList() : Call<List<Building>>

    //@POST("api/availablerooms")
    //fun getConferenceRoomList(@Path("id")id: Int) : Call<List<ConferenceRoom>>
    //fun getConferenceRoomList(@Body availableRoom: FetchConferenceRoom) : Call<List<ConferenceRoom>>

    @POST("api/AllRoomsWithStatus")
   // fun getConferenceRoomList(@Path("id")id: Int) : Call<List<ConferenceRoom>>
    fun getConferenceRoomList(@Body availableRoom: FetchConferenceRoom) : Call<List<ConferenceRoom>>


    @GET("api/UserLogin")
    fun getRequestCode(@Query( "email") email : String?) : Call<Int>

    @GET("api/UserDashboard")
    fun getDashboard(@Query( "email") email : String?) : Call<List<Dashboard>>

    //@POST("api/UserDashboard")
    //fun getDashboard(@Body email : String) : Call<List<Dashboard>>

    @POST("api/UserLogin")
    fun addEmployee(@Body newEmoployee: Employee) : Call<Int>

    @POST("api/BookMeeting")
    fun addBookingDetails(@Body booking: Booking) : Call<Int>

    @POST("api/CancelBooking")
    fun cancelBooking(@Body cancel: CancelBooking) : Call<Int>

    @GET("api/AllEmployeeList")
    fun getEmployees() : Call<List<EmployeeList>>
    // Pratheek's.....

    @POST("api/AddBuilding")
    fun addBuilding(@Body newBuilding:addBuilding):Call<ResponseBody>

    @POST("api/addconferenceroom")
    fun addConference(@Body newConferenceRoom: addConferenceRoom):Call<ResponseBody>

    @POST("api/BlockConfirmation")
    fun blockConfirmation(@Body room: BlockRoom) :Call<BlockingConfirmation>

    @POST("api/blocking")
    fun blockconference(@Body room: BlockRoom) :Call<ResponseBody>

    @GET("api/Building")
    fun getBuildings() :Call<List<BuildingT>>

    @GET("api/Building/{id}")
    fun getBuildingsConference(@Path("id")id: Int) : Call<List<BuildingConference>>

    @GET("api/blocking")
    fun getBlockedConference() : Call<List<Blocked>>

    @POST("api/unblocking")
    fun unBlockingConferenceRoom(@Body room: Unblock) : Call<ResponseBody>

    @GET("api/BuildingConferenceRooms/{id}")
    fun conferencelist(@Path("id")id : Int) : Call<List<ConferenceList>>


    //@GET("destination")
    //fun getDestinationList(@Query( "country") country : String?, @Query("counnt") count: Int) : Call<List<Destination>>

    /*@GET("destination")
    fun getDestinationList(@QueryMap filter: HashMap<String, String>) : Call<List<Destination>>

    @GET("destination/{id}")
    fun getDestination(@Path("id") id: Int) : Call<Destination>

    @POST("destination")
    fun addDestination(@Body newDestination: Destination) : Call<Destination>

    @FormUrlEncoded
    @PUT("destination/{id}")
    fun updateDestination(
        @Path("id") id: Int,
        @Field("city") city: String,
        @Field("description") desc: String,
        @Field("country") country: String
    ) : Call<Destination>

    @DELETE("destination/{id}")
    fun deleteDestination(@Path("id") id: Int) : Call<Unit>
*/
}