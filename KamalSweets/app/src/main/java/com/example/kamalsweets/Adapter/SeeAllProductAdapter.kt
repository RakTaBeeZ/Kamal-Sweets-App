package com.example.kamalsweets.Adapter

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kamalsweets.Activity.ProductDetailActivity
import com.example.kamalsweets.Model.AddProductModel
import com.example.kamalsweets.databinding.ItemCategoryProductLayoutBinding
import com.example.kamalsweets.databinding.ItemSeeAllProductLayoutBinding

import com.example.kamalsweets.databinding.LayoutProductItemBinding

class SeeAllProductAdapter(val context:Context, var list:ArrayList<AddProductModel>) : RecyclerView.Adapter<SeeAllProductAdapter.ProductViewHolder>(){

    inner class ProductViewHolder(val binding: ItemSeeAllProductLayoutBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding=ItemSeeAllProductLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun setFilteredList(list: ArrayList<AddProductModel>){
        this.list=list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val data=list[position]
        var spannableString= SpannableString("MRP=₹"+data.productMRP+data.productUnit)
        val strikethroughSpan= StrikethroughSpan()
        val length=5+data.productMRP!!.length+data.productUnit!!.length
        spannableString.setSpan(strikethroughSpan,0,length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        Glide.with(context).load(data.productCoverImage).into(holder.binding.imageView3)
        holder.binding.textView4.text=data.productName
        holder.binding.textView5.text=data.productCategory
        holder.binding.MRP.text=spannableString
        holder.binding.sellingPrice.text="₹"+data.productSp+data.productUnit

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("id",list[position].produductID)
            context.startActivity(intent)
        }
    }
}