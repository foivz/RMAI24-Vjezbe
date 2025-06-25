package hr.foi.rmai.memento.ws

import hr.foi.rmai.memento.entities.Perk
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UnlocksService {
    @GET("unlocks.php")
    fun getUnlocksForUser(@Query("username") username: String): Call<List<Perk>>
}