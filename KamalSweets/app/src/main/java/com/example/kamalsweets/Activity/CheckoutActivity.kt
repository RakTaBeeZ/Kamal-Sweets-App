package com.example.kamalsweets.Activity

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kamalsweets.MainActivity
import com.example.kamalsweets.R
import com.example.kamalsweets.Roomdb.AppDatabase
import com.example.kamalsweets.Roomdb.ProductModel
import com.example.kamalsweets.databinding.ProgresslayoutBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.math.log


class CheckoutActivity : AppCompatActivity(),PaymentResultListener {
    private lateinit var dialog:Dialog
    private lateinit var pricePreference: SharedPreferences
    private lateinit var paymentPreference:SharedPreferences
    var paymentStatus:Boolean?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progresslayout)
        supportActionBar?.hide()
        dialog.setCancelable(false)
        pricePreference=this.getSharedPreferences("totalAmount", MODE_PRIVATE)
        paymentPreference=this.getSharedPreferences("online_payment",MODE_PRIVATE)
        paymentStatus=paymentPreference.getBoolean("paid",false)
        var price= pricePreference.getFloat("total",0.00f)



        if(paymentStatus==true){
            val description="Best Payment Method To Pay"
            val checkout=Checkout()
            checkout.setKeyID("rzp_test_r4nFtFPvQDK1hn")
            try {
                val options = JSONObject()
                options.put("name", "Kamal Sweets")
                options.put("description", "$description") // Corrected the description here
                options.put("image", "https://shorturl.at/louJZ")
                //options.put("order_id", "order_DBJOWzybf0sJbb") //from response of step 3.
                options.put("theme.color", "#E6296A")
                options.put("currency", "INR")
                options.put("amount", (price * 100).toString()) // Corrected the price calculation here
                //options.put("prefill.email", "naresh2011a@gmail.com")
                //options.put("prefill.contact", "9988776655")
                Log.d("Razorpay", "Description: $description")
                Log.d("Razorpay", "Amount: $price")


                checkout.open(this, options)
            } catch (e: Exception) {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        }else{
            uploadData("Cash On Delivery",true)
            dialog.setContentView(R.layout.betterprogresslayout)
            dialog.show()
        }






    }

    override fun onPaymentSuccess(p0: String?) {
        uploadData("Paid",false)


    }

    private fun uploadData(paymentStatus:String,showDialog:Boolean) {
        val productIds = intent.getStringArrayListExtra("productIds")
        val quantities = intent.getStringArrayListExtra("productQuantity")

        if (productIds != null && quantities != null && productIds.size == quantities.size) {
            for (i in productIds.indices) {
                val productId = productIds[i]
                val quantity = quantities[i]

                fetchData(productId, quantity,paymentStatus,showDialog)
            }
        }
    }

    private fun fetchData(productId: String?,quantity:String,paymentStatus: String,showDialog: Boolean) {
        val dao=AppDatabase.getInstance(this).productDao()
        val preference= this.getSharedPreferences("otpActivityUser", MODE_PRIVATE)

        Firebase.firestore.collection("products")
            .document(productId!!).get().addOnSuccessListener {productSnapShot->

                lifecycleScope.launch(Dispatchers.IO) {

                    dao.deleteProduct(ProductModel(productId))
                }
                Firebase.firestore.collection("users").document(preference.getString("number","")!!).get()
                    .addOnSuccessListener {userSnapShot->
                        val userName=userSnapShot.getString("userName").toString()
                        val userNumber=userSnapShot.getString("village").toString()+" "+userSnapShot.getString("city").toString()+" "+userSnapShot.getString("state").toString()+" "+userSnapShot.getString("pincode").toString()
                        saveData(productSnapShot.getString("productName"),productSnapShot.getString("productSp"),productId,productSnapShot.getString("productCoverImage"),userName,userNumber,quantity,paymentStatus,showDialog)
                    }


            }
    }

    private fun saveData(productName: String?, productSp: String?, productId: String, coverImage: String?,userName:String,userNumber:String,quantity: String,paymentStatus: String,showDialog: Boolean) {
        val preference= this.getSharedPreferences("otpActivityUser", MODE_PRIVATE)
        var productSp=productSp?.toDouble()
        var productQuabtity=quantity.toDouble()
        var orderedValue="${productSp?.times(productQuabtity)}"

        val data = hashMapOf<String,Any>()
        data["name"]=productName!!
        data["price"]=orderedValue
        data["productId"]=productId
        data["status"]="Ordered"
        data["userId"]=preference.getString("number","")!!
        data["userName"]=userName
        data["userAddress"]=userNumber
        data["productQuantity"]=quantity
        data["cancelReason"]=""
        data["paymentStatus"]=paymentStatus
        data["deliveryPersonName"]=""
        data["deliveryPersonNumber"]=""

        val firestore=Firebase.firestore.collection("allOrders")
        val key=firestore.document().id

        data["orderId"]=key

        firestore.document(key).set(data).addOnSuccessListener {
            if(showDialog){
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()

                dialog.setContentView(R.layout.progresslayout2)
                dialog.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this,MainActivity::class.java))
                    finish() }, 3000)
            }, 3000)}else{
                dialog.setContentView(R.layout.progresslayout2)
                dialog.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this,MainActivity::class.java))
                    finish() }, 3000)
            }


        }.addOnFailureListener {
            Toast.makeText(this, "Something weng wrong", Toast.LENGTH_SHORT).show()
        }


    }


    override fun onPaymentError(p0: Int, p1: String?) {
        dialog.show()
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish() }, 3000)
    }
}