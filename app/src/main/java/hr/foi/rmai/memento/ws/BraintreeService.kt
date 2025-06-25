package hr.foi.rmai.memento.ws

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface BraintreeService {
    @FormUrlEncoded
    @POST("create_transaction.php")
    fun createTransaction(@Field("amount") amount: Double, @Field("nonce") nonce: String) : Call<BraintreeReponse>
}