package com.example.kamalsweets.Activity

import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import com.example.kamalsweets.Adapter.SeeAllProductAdapter
import com.example.kamalsweets.Model.AddProductModel

import com.example.kamalsweets.databinding.ActivityProductSeeAllBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

class ProductSeeAllActivity : AppCompatActivity() {


    private lateinit var binding: ActivityProductSeeAllBinding
    var list:ArrayList<AddProductModel> = ArrayList()
    private  var adapter:SeeAllProductAdapter= SeeAllProductAdapter(this,list)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductSeeAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProducts()
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
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

    private fun getProducts() {
        Firebase.firestore.collection("products")
            .get().addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val data=doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
                list.shuffle()
                binding.productRecycleView.adapter= adapter
            }.addOnFailureListener { error->
                Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
            }
    }


}