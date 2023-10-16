package com.example.kamalsweets.Fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.kamalsweets.Activity.CategorySeeAllActivity
import com.example.kamalsweets.Activity.ProductSeeAllActivity
import com.example.kamalsweets.Adapter.CategoryAdapter
import com.example.kamalsweets.Adapter.ProductAdapter
import com.example.kamalsweets.Model.AddProductModel
import com.example.kamalsweets.Model.CategoryModel
import com.example.kamalsweets.R
import com.example.kamalsweets.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(layoutInflater)

        val preference =requireContext().getSharedPreferences("info", MODE_PRIVATE)

        if(preference.getBoolean("isCart",false))
            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)

        getCategories()
        getProducts()
        binding.productSeeAll.setOnClickListener {
            val intent=Intent(requireContext(),ProductSeeAllActivity::class.java)
            startActivity(intent)
        }
        binding.categorySeeAll.setOnClickListener {
            val intent=Intent(requireContext(),CategorySeeAllActivity::class.java)
            startActivity(intent)

        }

        return binding.root
    }

    private fun getProducts() {
        val list =ArrayList<AddProductModel>()
        Firebase.firestore.collection("products")
            .get().addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val data=doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
                list.shuffle()
                binding.productRecyclerView.adapter= ProductAdapter(requireContext(),list)
            }.addOnFailureListener { error->
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getCategories() {
        val list =ArrayList<CategoryModel>()
        Firebase.firestore.collection("categories")
            .get().addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val data=doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                list.shuffle()
                binding.categoryRecyclerView.adapter= CategoryAdapter(list,requireContext())
            }.addOnFailureListener {error->
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_SHORT).show()
            }
    }


}