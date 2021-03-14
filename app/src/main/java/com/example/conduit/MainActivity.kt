package com.example.conduit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.api.models.entities.User
import com.example.conduit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var _activityMainBinding: ActivityMainBinding? = null
    private lateinit var authViewModel: AuthViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(_activityMainBinding?.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_feed,
                R.id.nav_my_feed,
                R.id.nav_auth
            ),
            _activityMainBinding?.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        _activityMainBinding?.navView?.setupWithNavController(navController)

        authViewModel.user.observe(this) {
            updateMenu(it)
            Toast.makeText(this, "Logged in as: ${it?.username}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateMenu(user: User?) {
        when (user) {
            is User -> {
                _activityMainBinding?.navView?.menu?.clear()
                navController.navigateUp()
                _activityMainBinding?.navView?.inflateMenu(R.menu.activity_main_user)
                _activityMainBinding?.navView?.setCheckedItem(R.id.nav_feed)
            }
            else -> {
                _activityMainBinding?.navView?.inflateMenu(R.menu.activity_main_guest)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}