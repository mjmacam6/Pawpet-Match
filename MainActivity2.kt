package com.example.myproject


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference

class MainActivity2 : AppCompatActivity() {


    private lateinit var bottom_Nav: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var drawer : DrawerLayout
    private lateinit var gridView: GridView
    private lateinit var dataList: ArrayList<Data_Upload>
    private lateinit var adapter: MyAdapter
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        toolbar = findViewById(R.id.toolbar)
          //nilagay to ta hindi nag aapppear agad ung home fragment kapag nag run ka. So, Dapat mauna to
        val homeFragment = Home_fragment()
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, homeFragment).commit()


        bottom_Nav = findViewById(R.id.bottomNavigationView )
        drawer = findViewById(R.id.drawer_layout)


        //tool bar
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_nav, R.string.close_nav)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        //bottom Navigation
        bottom_Nav.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.Home -> {
                    replaceFragment(Home_fragment())
                    true
                }
                R.id.Chat -> {
                    replaceFragment(chat())
                    true
                }
                R.id.Plus -> {
                    showMakeAPostDialog()
                    true
                }

                else -> false
            }
        }

        val navigationView = findViewById<NavigationView>(R.id.nav_drawer)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Profile -> {
                    bottom_Nav.visibility = View.GONE
                    toolbar.visibility = View.GONE
                    val intent = Intent(this, drawer_profile_activity::class.java)
                    startActivity(intent)
                    drawer.closeDrawer(GravityCompat.START) // Close the drawer after selecting "Profile"
                    true
                }
                R.id.create_Notes -> {
                    val intent = Intent(this, create_notes::class.java)
                    Toast.makeText(this, "Create Notes", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    true
                }
                R.id.Settings -> {
                    val intent = Intent(this, drawer_settings::class.java)
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    true
                }
                R.id.nav_logout -> {

                    true
                }

                else -> false
            }
        }

    }


    private fun showMakeAPostDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.activity_upload_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT // Set the width
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT // Set the height
        dialog.window?.attributes = layoutParams

        val tv_post : TextView = dialog.findViewById(R.id.make_post)

        tv_post.setOnClickListener {
            val intent = Intent(this, create_post::class.java)
            startActivity(intent)
            //dinagdag tong sa baba kasi kapag pinindot si create post is magsstay ung dialogue
            dialog.dismiss()
        }
        dialog.show()
        dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
                dialog.dismiss()
                true
            } else {
                false
            }
        }

        dialog.show()
        }


    //function of toolbar dapat outside oncreate
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true;
    }


    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()

    }


}

