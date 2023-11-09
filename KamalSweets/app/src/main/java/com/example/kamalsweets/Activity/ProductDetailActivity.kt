package com.example.kamalsweets.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.kamalsweets.MainActivity
import com.example.kamalsweets.R
import com.example.kamalsweets.Room.productDao
import com.example.kamalsweets.Roomdb.AppDatabase
import com.example.kamalsweets.Roomdb.ProductModel
import com.example.kamalsweets.databinding.ActivityProductDetailBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Array

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var stockStatus:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getProductDetail(intent.getStringExtra("id"))
        Log.d("Counts->","1")
    }

    private fun getProductDetail(prodId: String?) {

        Firebase.firestore.collection("products")
            .document(prodId!!).get().addOnSuccessListener {
                var productMrp=it.getString("productMRP")
                var productUnit=it.getString("productUnit")
                var spannableString=SpannableString("MRP=₹${productMrp}"+productUnit)
                var length=5+productMrp!!.length+productUnit!!.length
                var strikethroughSpan=StrikethroughSpan()
                spannableString.setSpan(strikethroughSpan,0,length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                val list =it.get("productImage") as ArrayList<String>
                var productName=it.getString("productName")
                val productSp=it.getString("productSp")
                stockStatus=it.getString("stockStatus").toString()
                //Log.d("user", "getProductDetail: $stockStatus")
                binding.title.text=productName
                binding.prize.text= "₹$productSp"+productUnit
                binding.mrp.text=spannableString
                binding.Discription.text=it.getString("productDiscription")
                val slideList =ArrayList<SlideModel>()
                list.forEach{data->
                    slideList.add(SlideModel(data,"$productName",ScaleTypes.CENTER_CROP))
                }
                cartAction(prodId, productName,productSp,it.getString("productCoverImage"))
                binding.imageSlider.setImageList(slideList)
            }.addOnFailureListener {
                Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cartAction(prodId: String, productName: String?, productSp: String?, coverImg: String?) {

        val productDao=AppDatabase.getInstance(this).productDao()

        if(productDao.isExist(prodId)!=null){
            binding.cartview.text="Go To Cart"
        }else{
            binding.cartview.text="Add To Cart"
        }

        binding.cartview.setOnClickListener {
            if(stockStatus=="In Stock"){
                if(binding.quantity.text.toString().trim().isEmpty()){
                    Toast.makeText(this, "Please Specify Quantity", Toast.LENGTH_SHORT).show()
                    binding.quantity.requestFocus()
                }else
                {
                    val productSpiInt=productSp?.toDouble()
                    val productMultipleText=binding.quantity.text.toString()
                    val productMultiple=productMultipleText.toDouble()
                    val priceAfterMultiple=productSpiInt?.times(productMultiple).toString()


                    if(productDao.isExist(prodId)!=null){
                        openCart()
                    }else{
                        addToCart(productDao,prodId,productName,priceAfterMultiple,coverImg,productMultipleText)
                    }
                }
            }else{
                Toast.makeText(this, "Product Out Of Stock", Toast.LENGTH_SHORT).show()
            }
            

        }

    }

    private fun addToCart(productDao: productDao, prodId: String, productName: String?, productSp: String?, coverImg: String?,productQuantity:String) {

        val data = ProductModel(prodId,productName,coverImg,productSp,productQuantity)
        lifecycleScope.launch(Dispatchers.IO){
            productDao.insertProduct(data)
            binding.cartview.text="Go To Cart"
        }
    }

    private fun openCart() {

        val preference =this.getSharedPreferences("info", MODE_PRIVATE)
        val editor =preference.edit()
        editor.putBoolean("isCart",true)
        editor.apply()

        startActivity(Intent(this,MainActivity::class.java))
        finish()

    }
}