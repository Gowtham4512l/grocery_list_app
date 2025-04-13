package com.example.shoppinglist.ui.shoppingList.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.data.db.entities.ShoppingItem
import com.example.shoppinglist.ui.shoppingList.viewModel.ShoppingViewModel

class ShoppingItemAdapter(
    private var items: List<ShoppingItem>,
    private var viewModel: ShoppingViewModel
) : RecyclerView.Adapter<ShoppingItemAdapter.ShoppingViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShoppingViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.shopping_list_item, parent, false)
        return ShoppingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ShoppingItem) {
            itemView.findViewById<TextView>(R.id.tvName).text = item.name
            itemView.findViewById<TextView>(R.id.tvQuantity).text = "${item.quantity}"
            itemView.findViewById<ImageView>(R.id.ivDelete).setOnClickListener {
                viewModel.delete(item)
            }
            itemView.findViewById<ImageView>(R.id.ivPlusRounded).setOnClickListener {
                item.quantity++
                viewModel.upsert(item)
            }
            itemView.findViewById<ImageView>(R.id.ivMinusRounded).setOnClickListener {
                if (item.quantity > 0) {
                    item.quantity--
                    viewModel.upsert(item)
                }
            }
        }
    }

    fun updateList(newItems: List<ShoppingItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}