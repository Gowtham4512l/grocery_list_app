package com.example.shoppinglist.ui.shoppingList.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
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
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        private val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
        private val ivPlusRounded: ImageView = itemView.findViewById(R.id.ivPlusRounded)
        private val ivMinusRounded: ImageView = itemView.findViewById(R.id.ivMinusRounded)

        fun bind(item: ShoppingItem) {
            tvName.text = item.name
            tvQuantity.text = item.quantity.toString()

            ivDelete.setOnClickListener {
                viewModel.delete(item)
            }

            ivPlusRounded.setOnClickListener {
                val updatedItem = item.copy(quantity = item.quantity + 1)
                viewModel.upsert(updatedItem)
            }

            ivMinusRounded.setOnClickListener {
                if (item.quantity > 0) {
                    val updatedItem = item.copy(quantity = item.quantity - 1)
                    if (updatedItem.quantity == 0) {
                        viewModel.delete(item)
                        return@setOnClickListener
                    }
                    viewModel.upsert(updatedItem)
                }
            }
        }
    }

    fun updateList(newItems: List<ShoppingItem>) {
        val diffUtil = ShoppingItemDiffCallback(items, newItems)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        items = newItems
        diffResults.dispatchUpdatesTo(this)
    }

    class ShoppingItemDiffCallback(
        private val oldList: List<ShoppingItem>,
        private val newList: List<ShoppingItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}