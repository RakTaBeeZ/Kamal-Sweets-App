package com.example.kamalsweets.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kamalsweets.Adapter.AllOrderAdapter
import com.example.kamalsweets.Model.AllOrderModel
import com.example.kamalsweets.databinding.FragmentAboutBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MoreFragment : Fragment() {
private lateinit var binding: FragmentAboutBinding
    private lateinit var list:ArrayList<AllOrderModel>
    private lateinit var adapter:AllOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAboutBinding.inflate(layoutInflater)

        list= ArrayList()
        val preference= requireContext().getSharedPreferences("otpActivityUser", AppCompatActivity.MODE_PRIVATE)

        Firebase.firestore.collection("allOrders").whereEqualTo("userId",preference.getString("number","")!!)
            .get().addOnSuccessListener {
                if(it.size()>0){
                    binding.noOrder.visibility=View.GONE
                    list.clear()

                    for (doc in it){
                        val data=doc.toObject(AllOrderModel::class.java)
                        list.add(data)
                    }
                    adapter = AllOrderAdapter(list,requireContext())
                    binding.recyclerView.adapter=adapter
                    adapter.notifyDataSetChanged()
                }


        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Fetching Order Details Went Wrong", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }


}