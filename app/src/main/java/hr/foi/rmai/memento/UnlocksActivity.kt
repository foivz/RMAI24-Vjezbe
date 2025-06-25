package hr.foi.rmai.memento

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.braintreepayments.api.DropInClient
import com.braintreepayments.api.DropInListener
import com.braintreepayments.api.DropInRequest
import com.braintreepayments.api.DropInResult
import hr.foi.rmai.memento.adapters.ShopAdapter
import hr.foi.rmai.memento.entities.ShopItem
import hr.foi.rmai.memento.ws.BraintreeReponse
import hr.foi.rmai.memento.ws.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UnlocksActivity : AppCompatActivity(),  DropInListener {
    private lateinit var dropInClient: DropInClient
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unlocks)

        dropInClient = DropInClient(this, "sandbox_ktvbjfdk_yfbkxdhy43f43ht5")
        dropInClient.setListener(this)
        recyclerView = findViewById(R.id.rv_shop_items)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val items = listOf<ShopItem>(
            ShopItem(1, "Faster shooting", "Machine gun upgrade that lets the player shoot faster."),
            ShopItem(2, "Double jump", "Allows player to jump one more when he is already in the air."),
        )
        recyclerView.adapter = ShopAdapter(items.toMutableList()) {
            val request = DropInRequest(false)
            request.maskCardNumber = true
            dropInClient.launchDropIn(request)
        }
    }

    override fun onDropInSuccess(dropInResult: DropInResult) {
        val nonce: String = dropInResult.paymentMethodNonce!!.string
        NetworkService.braintreeService.
        createTransaction(1.0,nonce).enqueue( object : Callback<BraintreeReponse> {
            override fun onResponse(
                call: Call<BraintreeReponse>,
                resp: Response<BraintreeReponse>) {
                Toast.makeText(this@UnlocksActivity, resp.body()?.status, Toast.LENGTH_LONG).show()
            }
            override fun onFailure(response: Call<BraintreeReponse>, error: Throwable) {}
        })
    }

    override fun onDropInFailure(error: Exception) {
        Toast.makeText(this, error.message ?: error.toString(), Toast.LENGTH_LONG).show()
    }
}