package com.example.kamalsweets.Adapter

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.kamalsweets.Activity.ProductDetailActivity
import com.example.kamalsweets.Model.AddProductModel
import com.example.kamalsweets.databinding.ItemCategoryProductLayoutBinding


class CategoryProductAdapter(val context: Context, var list :ArrayList<AddProductModel>) :RecyclerView.Adapter<CategoryProductAdapter.CategoryProductViewHolder>(){

    inner class CategoryProductViewHolder(val binding: ItemCategoryProductLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductViewHolder {
        val binding = ItemCategoryProductLayoutBinding.inflate(LayoutInflater.from(context),parent,false)

        return CategoryProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun setFilteredList(list: ArrayList<AddProductModel>){
        this.list=list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CategoryProductViewHolder, position: Int) {
        var spannableString=SpannableString("MRP:- ₹${list[position].productMRP}"+list[position].productUnit)
        var strikethroughSpan=StrikethroughSpan()
        var length=7+list[position].productMRP!!.length+list[position].productUnit!!.length
        spannableString.setSpan(strikethroughSpan,0,length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        Glide.with(context).load(list[position].productCoverImage).into(holder.binding.imageView3)
        holder.binding.textView4.text=list[position].productName
        holder.binding.textView5.text="₹${list[position].productSp}"+list[position].productUnit
        holder.binding.textView6.text=spannableString

        holder.itemView.setOnClickListener {
            val intent = Intent(context,ProductDetailActivity::class.java)
            intent.putExtra("id",list[position].produductID)
            context.startActivity(intent)
        }
    }
}