package mx.ipn.escom.guzmanr.p3

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/kid")
    fun getKids(): Call<List<Kid>>

    @GET("/record")
    fun getRecords(): Call<List<Record>>


    @POST("/record")
    fun addRecord(@Body record: Record): Call<Record>


    @POST("/login")
    fun login(@Body credentials: Map<String, String>): Call<Map<String, Any>>

    @GET("/announcement/last")
    fun getLastAnnouncement(): Call<Announcement>

    @POST("/announcement")
    fun addAnnouncement(@Body announcement: Announcement): Call<Announcement>


}

