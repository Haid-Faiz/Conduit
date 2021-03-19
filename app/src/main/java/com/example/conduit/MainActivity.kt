package com.example.conduit

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
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
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.api.models.entities.User
import com.example.conduit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_FILE_AUTH = "prefs_auth"
        private const val PREFS_KEY_TOKEN = "token"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var _activityMainBinding: ActivityMainBinding? = null
    private lateinit var authViewModel: AuthViewModel
    private lateinit var navController: NavController
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(_activityMainBinding?.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        sharedPreferences = getSharedPreferences(PREFS_FILE_AUTH, MODE_PRIVATE)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        sharedPreferences.getString(PREFS_KEY_TOKEN, null)?.let {
            authViewModel.getCurrentUser(it)
        }
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
            it?.token?.let { token: String ->
                sharedPreferences.edit {
                    this.putString(PREFS_KEY_TOKEN, token).apply()
                    Toast.makeText(this@MainActivity, "Welcome ${it.username}", Toast.LENGTH_LONG).show()
                }
            } ?: run {
                sharedPreferences.edit().remove(PREFS_KEY_TOKEN).apply()
                Toast.makeText(this@MainActivity, "Successfully Logged out", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateMenu(user: User?) {
        _activityMainBinding?.navView?.setCheckedItem(R.id.nav_feed)
        when (user) {
            is User -> {
                _activityMainBinding?.navView?.menu?.clear()
                navController.navigateUp()
                _activityMainBinding?.navView?.inflateMenu(R.menu.activity_main_user)
            }
            else -> {
                _activityMainBinding?.navView?.menu?.clear()
                _activityMainBinding?.navView?.inflateMenu(R.menu.activity_main_guest)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                authViewModel.logout()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityMainBinding = null
    }
}