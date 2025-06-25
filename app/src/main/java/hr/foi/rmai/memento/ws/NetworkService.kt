package hr.foi.rmai.memento.ws

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {
    private const val BASE_URL = "http://158.180.45.98/"

    private var instance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val unlocksService: UnlocksService = instance.create(UnlocksService::class.java)
    val braintreeService: BraintreeService = instance.create(BraintreeService::class.java)
}