package hr.foi.rmai.memento.ws

data class NewsResponse(
    var count: Int,
    var results: ArrayList<NewsItem>
)