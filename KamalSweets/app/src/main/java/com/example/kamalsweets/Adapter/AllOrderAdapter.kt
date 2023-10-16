package com.example.kamalsweets.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kamalsweets.Model.AllOrderModel
import com.example.kamalsweets.databinding.AllOrderItemLayoutBinding

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AllOrderAdapter(val list: ArrayList<AllOrderModel>, val context: Context) :RecyclerView.Adapter<AllOrderAdapter.viewHolder>() {



    inner class viewHolder(val binding: AllOrderItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding=AllOrderItemLayoutBinding.inflate((LayoutInflater.from(context)),parent,false)

        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.binding.productTitle.text="${list[position].name}  x  ${list[position].productQuantity}"
        holder.binding.productPrice.text="₹"+list[position].price
        when(list[position].status){
            "Ordered"->holder.binding.cancelButton.isEnabled = true
            else-> holder.binding.cancelButton.isEnabled = false
        }
        holder.binding.cancelButton.setOnClickListener {
            if(holder.binding.cancelReason.text.isEmpty()){
                holder.binding.cancelReason.visibility=View.VISIBLE
                holder.binding.cancelReason.requestFocus()
                Toast.makeText(context, "Please Provide Reason! ", Toast.LENGTH_SHORT).show()
            }
            else{
                val cancelReason=holder.binding.cancelReason.text.toString()
            updateStatus("Canceled",list[position].orderId!!,cancelReason)
            holder.binding.cancelButton.isEnabled = false}

        }


        when(list[position].status){
            "Ordered"->{
                holder.binding.productStatus.setTextColor(Color.parseColor("#FFA500"))
                holder.binding.productStatus.text="Order Placed Confirmation Pending!"
            }
            "Confirmed"->{
                holder.binding.productStatus.setTextColor(Color.parseColor("green"))
                holder.binding.productStatus.text="Order Confirmed"
            }
            "Dispatched"->{
                holder.binding.productStatus.setTextColor(Color.parseColor("#FFA500"))
                holder.binding.productStatus.text="Order Has Been Dispatched From Store"
                fetchDeliryDetails(holder,list[position].orderId)
            }
            "Delivered"->{
                holder.binding.productStatus.setTextColor(Color.parseColor("green"))
                holder.binding.productStatus.text="Order Delivered"
            }
            "Canceled"->{
                holder.binding.productStatus.setTextColor(Color.parseColor("red"))
                holder.binding.productStatus.text="Order Canceled"
            }
        }


    }

    private fun fetchDeliryDetails(holder: AllOrderAdapter.viewHolder, orderId: String?) {
        Firebase.firestore.collection("allOrders").document(orderId!!).get()
            .addOnSuccessListener {
                holder.binding.deliveryLayout.visibility=View.VISIBLE
                holder.binding.DeliveryBoyName.text="Person Name: "+it.getString("deliveryPersonName")
                holder.binding.DeliveryBoyNumber.text="Person Number: +91 "+it.getString("deliveryPersonNumber")
            }
    }


    private fun updateStatus(str:String,doc:String,cancelReason:String){
        val data= hashMapOf<String,Any>()
        data["status"]=str
        data["cancelReason"]=cancelReason
        Firebase.firestore.collection("allOrders").document(doc).update(data)
            .addOnSuccessListener {

                Toast.makeText(context, "Order Canceled", Toast.LENGTH_LONG).show()

            }.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }

    }

}