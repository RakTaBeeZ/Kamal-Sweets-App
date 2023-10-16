package com.example.kamalsweets.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kamalsweets.Activity.CategoryActivity
import com.example.kamalsweets.Model.AddProductModel
import com.example.kamalsweets.Model.CategoryModel
import com.example.kamalsweets.R
import com.example.kamalsweets.databinding.LayoutCategoryItemBinding

class CategoryAdapter(var list: ArrayList<CategoryModel>, val context:Context): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(View: View):RecyclerView.ViewHolder(View){
       // val binding=ItemCategoryLayoutBinding.bind(View)
        val binding =LayoutCategoryItemBinding.bind(View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        return CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_item,parent,false))
    }

    override fun getItemCount(): Int {

        return list.size
    }
    fun setFilteredList(list: ArrayList<CategoryModel>){
        this.list=list
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
    holder.binding.textView.text=list[position].cat
    Glide.with(context).load(list[position].img).into(holder.binding.imageView2)
        holder.itemView.setOnClickListener {
            val intent = Intent(context,CategoryActivity::class.java)
            intent.putExtra("cat",list[position].cat)
            context.startActivity(intent)

        }
    }
}



