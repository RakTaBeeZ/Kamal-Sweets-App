package com.example.kamalsweets.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kamalsweets.Adapter.CategoryAdapter
import com.example.kamalsweets.Model.AddProductModel
import com.example.kamalsweets.Model.CategoryModel
import com.example.kamalsweets.R
import com.example.kamalsweets.databinding.ActivityCategorySeeAllBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

class CategorySeeAllActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityCategorySeeAllBinding.inflate(layoutInflater)
    }
    private var list=ArrayList<CategoryModel>()
    private var adapter=CategoryAdapter(list,this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getCategories()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
    }
    private fun filterList(query: String?) {
        if(query != null){
            var filteredList = ArrayList<CategoryModel>()
            for (i in list){
                if(i.cat?.lowercase(Locale.ROOT)?.contains(query)==true){
                    filteredList.add((i))
                }
            }
            if(filteredList.isEmpty()){
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
            }else{
                adapter.setFilteredList(filteredList)
            }
        }
    }



    private fun getCategories() {
        Firebase.firestore.collection("categories")
            .get().addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val data=doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                binding.categoryRecyclerView.adapter= adapter
                binding.categoryRecyclerView.layoutManager= GridLayoutManager(this,2)
            }.addOnFailureListener {error->
                Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
            }
    }
}