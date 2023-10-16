package com.example.kamalsweets.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kamalsweets.Activity.AddressActivity
import com.example.kamalsweets.Activity.CategoryActivity
import com.example.kamalsweets.Adapter.CartAdapter
import com.example.kamalsweets.Roomdb.AppDatabase
import com.example.kamalsweets.Roomdb.ProductModel
import com.example.kamalsweets.databinding.FragmentCartBinding


class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var list:ArrayList<String>
    private lateinit var list1:ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentCartBinding.inflate(layoutInflater)
        val preference =requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor =preference.edit()
        editor.putBoolean("isCart",false)
        editor.apply()

        val dao =AppDatabase.getInstance(requireContext()).productDao()
        list =ArrayList()
        list1 =ArrayList()
        dao.getAllProducts().observe(requireActivity()){
            if(it.size>0){
                binding.noCart.visibility=View.GONE

            }else{
                binding.noCart.visibility=View.VISIBLE
            }
            binding.cartRecycler.adapter=CartAdapter(requireContext(),it)
            list.clear()
            for(data in it){
                list.add(data.productId)
                list1.add(data.productQuantity.toString())
            }
            totalCost(it)

        }

        return binding.root
    }

    private fun totalCost(data: List<ProductModel>?) {
      val paidPreference=requireContext().getSharedPreferences("online_payment",Context.MODE_PRIVATE)
        val paidEditor=paidPreference.edit()
        var total:Double =0.0
        for(item in data!!){
            total += item.productSp!!.toDouble()
        }

        val preference=this.activity?.getSharedPreferences("totalAmount",Context.MODE_PRIVATE)
        val editor=preference?.edit()
        editor?.putFloat("total",total.toFloat())
        editor?.apply()

        binding.textView5.text="Total item in cart : ${data.size}"
        binding.textView6.text="Total Cost : ${total}"

        binding.checkout.setOnClickListener {
            val dao =AppDatabase.getInstance(requireContext()).productDao()
            dao.getAllProducts().observe(requireActivity()){
                if (it.size>0){
                    binding.checkout.visibility=View.GONE
                    binding.paymentButtons.visibility=View.VISIBLE
                }else{
                    binding.checkout.visibility=View.VISIBLE
                    binding.paymentButtons.visibility=View.GONE
                    Toast.makeText(requireContext(), "Please Add Item To Cart", Toast.LENGTH_LONG).show()

                }
            }


        }
        val intent = Intent(context, AddressActivity::class.java)
        val b= Bundle()
        b.putStringArrayList("productIds",list)
        b.putStringArrayList("productQuantity",list1)
        b.putString("totalCost",total.toString())
        intent.putExtras(b)
        binding.onlinePayment.setOnClickListener {

            paidEditor.putBoolean("paid",true)
            paidEditor.apply()
            startActivity(intent)
        }
        binding.codPayment.setOnClickListener {

            paidEditor.putBoolean("paid",false)
            paidEditor.apply()
            startActivity(intent)
        }

    }


}