package com.example.kamalsweets.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.example.kamalsweets.Adapter.CategoryAdapter
import com.example.kamalsweets.Adapter.CategoryProductAdapter
import com.example.kamalsweets.Adapter.ProductAdapter
import com.example.kamalsweets.Model.AddProductModel
import com.example.kamalsweets.Model.CategoryModel
import com.example.kamalsweets.R
import com.example.kamalsweets.databinding.ActivityCategoryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

class CategoryActivity : AppCompatActivity() {

    //For this activity CategoryProductAdapter.kt is used
    //item_category_product_layout is used to show in recyclerview of activity_category.xml

    private lateinit var binding:ActivityCategoryBinding
    private var list=ArrayList<AddProductModel>()
    private var adapter=CategoryProductAdapter(this, list)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getProducts(intent.getStringExtra("cat"))
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
            var filteredList = ArrayList<AddProductModel>()
            for (i in list){
                if(i.productName?.lowercase(Locale.ROOT)?.contains(query)==true){
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

    private fun getProducts(category: String?) {

        Firebase.firestore.collection("products").whereEqualTo("productCategory",category)
            .get().addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val data=doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }

                binding.recyclerView.adapter= adapter
            }.addOnFailureListener { error->
                Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
            }
    }
}