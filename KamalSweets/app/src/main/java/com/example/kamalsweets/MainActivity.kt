package com.example.kamalsweets

import android.content.Intent
import android.content.SharedPreferences

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.example.kamalsweets.Activity.LoginActivity
import com.example.kamalsweets.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var i=0
    private  var userName:String?=null
    private var phoneNumber:String?=null
    private lateinit var navController: NavController

    private lateinit var preference: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        if(FirebaseAuth.getInstance().currentUser== null){
//
//            startActivity(Intent(this,LoginActivity::class.java))
//            finish()
//        }
        preference= this.getSharedPreferences("otpActivityUser", MODE_PRIVATE)
        var check :Boolean = preference.getBoolean("flag",false)
        if (check==false){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        userName=preference.getString("name","Default2")
        phoneNumber=preference.getString("number",null)


        val navHostFragment =supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        navController = navHostFragment!!.findNavController()

        val popupMenu= PopupMenu(this,null)
        popupMenu.inflate(R.menu.bottom_nav)

        binding.bottomBar.setupWithNavController(popupMenu.menu,navController)

        binding.bottomBar.onItemSelected={
            when(it){
                0->{
                    i=0;
                    navController.navigate(R.id.homeFragment)
                }
                1->i=1
                2->i=2
            }
        }
        navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                title=when(destination.id){
                    R.id.cartFragment -> "My Cart"
                        R.id.moreFragment->"My Orders"
                    else -> "Kamal Sweets"

                }
            }

        })



    }
    //        Option starts

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu,menu)
        val item1=menu?.findItem(R.id.user_N)
        val item2=menu?.findItem(R.id.user_Number)
        item1?.setTitle("$userName")
        item2?.setTitle("$phoneNumber")
        invalidateOptionsMenu()
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val editor=preference.edit()

        when(item.itemId){
            R.id.user_N->{
                Toast.makeText(this, "Welcome $userName", Toast.LENGTH_LONG).show()
            }
            R.id.logout->{
                editor.putBoolean("flag",false)
                editor.apply()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()

            }
            R.id.about->{

            }

        }
        return super.onOptionsItemSelected(item)
    }


//                Option Menu ends

    override fun onBackPressed() {

        if (i == 0) {
            super.onBackPressed()
            finish()
        }
    }
}