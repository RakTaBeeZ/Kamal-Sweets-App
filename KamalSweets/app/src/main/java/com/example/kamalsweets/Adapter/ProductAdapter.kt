package com.example.kamalsweets.Adapter

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kamalsweets.Activity.ProductDetailActivity
import com.example.kamalsweets.Model.AddProductModel
import com.example.kamalsweets.R

import com.example.kamalsweets.databinding.LayoutProductItemBinding

class ProductAdapter(val context:Context,val list:ArrayList<AddProductModel>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){

    inner class ProductViewHolder(val binding: LayoutProductItemBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding=LayoutProductItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val data=list[position]
        var spannableString=SpannableString("₹"+data.productMRP+data.productUnit)
        val strikethroughSpan=StrikethroughSpan()
        val length=1+data.productMRP!!.length+data.productUnit!!.length
        spannableString.setSpan(strikethroughSpan,0,length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val productMrp= holder.binding.textView1
        val productSp= holder.binding.button4
        Glide.with(context).load(data.productCoverImage).into(holder.binding.productImage)
        holder.binding.textView3.text=data.productName
        holder.binding.textView2.text=data.productCategory
        productMrp.text=spannableString
        productSp.text="₹"+data.productSp+data.productUnit
        if(list[position].stockStatus != "In Stock"){
            holder.binding.addToCartButton.text="Out Of Stock"
            holder.binding.addToCartButton.textSize=9f
        }
        holder.binding.addToCartButton.setOnClickListener {
            if (list[position].stockStatus=="In Stock"){
                val intent = Intent(context, ProductDetailActivity::class.java)
                intent.putExtra("id",list[position].produductID)
                context.startActivity(intent)
            }else{
                Toast.makeText(context, "Product Out Of Stock", Toast.LENGTH_SHORT).show()
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("id",list[position].produductID)
            context.startActivity(intent)
        }
    }
}