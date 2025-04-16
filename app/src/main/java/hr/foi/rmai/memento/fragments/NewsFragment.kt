package hr.foi.rmai.memento.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.adapters.NewsAdapter
import hr.foi.rmai.memento.ws.NewsResponse
import hr.foi.rmai.memento.ws.WsNews
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment() {
    private val ws = WsNews.newsService
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    private fun displayWebServiceErrorMessage() {
        Toast.makeText(
            context,
            getString(R.string.news_ws_err_msg),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun loadNews() {
        ws.getNews().enqueue(
            object : Callback<NewsResponse> {
                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        val news = responseBody!!.results
                        recyclerView.adapter = NewsAdapter(news)
                    } else {
                        displayWebServiceErrorMessage()
                    }
                }

                override fun onFailure(call: Call<NewsResponse>?, t: Throwable?) {
                    displayWebServiceErrorMessage()
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.rv_news)
        recyclerView.layoutManager = LinearLayoutManager(context)

        loadNews()
    }
}