package hr.foi.rmai.memento.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.entities.ShopItem

class ShopAdapter(
    private val itemList: MutableList<ShopItem>,
    private val onBuyClick: ((itemId: Int) -> Unit)? = null
) : RecyclerView.Adapter<ShopAdapter.ShopItemViewHolder>() {
    inner class ShopItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvItemName: TextView
        private val tvItemDescription: TextView
        private val btnBuy: Button

        init {
            tvItemName = view.findViewById(R.id.tv_item_name)
            tvItemDescription = view.findViewById(R.id.tv_description)
            btnBuy = view.findViewById(R.id.btn_buy)

            btnBuy.setOnClickListener {
                onBuyClick?.invoke(itemList[bindingAdapterPosition].id)
            }
        }

        fun bind(item: ShopItem) {
            tvItemName.text = item.name
            tvItemDescription.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val shopView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.shop_list_item, parent, false)

        return ShopItemViewHolder(shopView)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}