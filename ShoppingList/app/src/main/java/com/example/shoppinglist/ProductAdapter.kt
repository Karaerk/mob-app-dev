package com.example.shoppinglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_product.view.*

class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product) {
            itemView.tvItem.text = "${product.quantity} ${product.name}"
        }
    }

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called item_product.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        )
    }

    /**
     * Returns the size of the list.
     */
    override fun getItemCount(): Int {
        return products.size
    }

    /**
     * Called by RecyclerView to display to data at the specific position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])
    }

}
