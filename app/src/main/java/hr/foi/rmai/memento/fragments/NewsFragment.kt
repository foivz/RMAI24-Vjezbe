package hr.foi.rmai.memento.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.ws.NewsResponse
import hr.foi.rmai.memento.ws.WsNews
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment() {
    private val ws = WsNews.newsService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    private fun loadNews() {
        ws.getNews().enqueue(
            object : Callback<NewsResponse> {
                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                    if (response?.isSuccessful == true) {
                        val responseBody = response.body()
                        Log.i("RMAI-WS", responseBody?.count.toString())
                    }
                }

                override fun onFailure(call: Call<NewsResponse>?, t: Throwable?) {

                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadNews()
    }
}