package com.example.kamalsweets.Adapter

import android.content.Context
import android.content.Intent
import android.provider.Settings.Global
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kamalsweets.Activity.ProductDetailActivity
import com.example.kamalsweets.Roomdb.AppDatabase
import com.example.kamalsweets.Roomdb.ProductModel
import com.example.kamalsweets.databinding.LayoutCartItemBinding
import com.example.kamalsweets.databinding.LayoutCategoryItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CartAdapter (val context: Context, val list: List<ProductModel>): RecyclerView.Adapter<CartAdapter.CartViewHolder>(){

    inner class CartViewHolder(val binding:LayoutCartItemBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding= LayoutCartItemBinding.inflate(LayoutInflater.from(context),parent,false)

        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        Glide.with(context).load(list[position].productImage).into(holder.binding.imageView4)
        holder.binding.textView6.text="${list[position].productName}  x  ${list[position].productQuantity}"
        holder.binding.textView7.text="₹${list[position].productSp}"

        holder.itemView.setOnClickListener {
            val intent =Intent(context,ProductDetailActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)

        }

        val dao =AppDatabase.getInstance(context).productDao()

        holder.binding.imageView5.setOnClickListener {

            GlobalScope.launch(Dispatchers.IO) {
            dao.deleteProduct(
                ProductModel(
                list[position].productId,
                list[position].productName,
                list[position].productImage,
                list[position].productSp))
            }
        }
    }
}