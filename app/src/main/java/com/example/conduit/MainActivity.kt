package com.example.conduit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.api.models.entities.User
import com.example.api.services.ConduitClient
import com.example.conduit.base.Resource
import com.example.conduit.base.ViewModelFactory
import com.example.conduit.data.UserPreference
import com.example.conduit.data.repos.UserRepo
import com.example.conduit.databinding.ActivityMainBinding
import com.example.conduit.utils.loadImage
import com.example.conduit.utils.showSnackBar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var _activityMainBinding: ActivityMainBinding? = null
    private lateinit var authViewModel: AuthViewModel
    private lateinit var navController: NavController
    private lateinit var userPreferences: UserPreference
//    private val sdf : AuthViewModel by viewModels<AuthViewModel> { ViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(_activityMainBinding?.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setUpAppBar()
        userPreferences = UserPreference(this)

        val authToken = runBlocking { userPreferences.authToken.first() }

        val factory = ViewModelFactory(
            UserRepo(
                authApi = authToken?.let { ConduitClient.getAuthApiService(it) },
                publicApi = ConduitClient.getApiService(),
                userPreference = userPreferences
            )
        )
        authViewModel = ViewModelProvider(this@MainActivity, factory).get(AuthViewModel::class.java)

        authToken?.let {
            Log.d("token1", "onCreate: $it")
            authViewModel.getCurrentUser(it)
        }

        authViewModel.user.observe(this) {
            // it will not observe in this case -> when we will open up the app in logout condition
            when (it) {
                is Resource.Error -> {
                    updateMenu(null)
                    lifecycleScope.launch { userPreferences.clearAuthToken() }
                    _activityMainBinding?.root?.showSnackBar("Something went wrong")
                }
//                Resource.Loading -> TODO()
                is Resource.Success -> it.value.user.let {
                    authViewModel.saveAuthToken(it.token)
                    Log.d("Token", "onCreate: ${it.token}")
                    updateMenu(it)
                    _activityMainBinding?.root?.showSnackBar("Welcome ${it.username}")
                } ?: run {
                    lifecycleScope.launch { userPreferences.clearAuthToken() }
                    updateMenu(it.value.user)
                    _activityMainBinding?.root?.showSnackBar("Something went wrong")
                }
            }
        }
    }

    private fun setUpAppBar() {
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_feed,
                R.id.nav_my_feed,
            ),
            _activityMainBinding?.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        _activityMainBinding?.navView?.setupWithNavController(navController)
    }

    private fun updateMenu(user: User?) {
        _activityMainBinding?.navView?.setCheckedItem(R.id.nav_feed)
        when (user) {
            is User -> {
                _activityMainBinding?.navView?.menu?.clear()
                navController.navigateUp()
                _activityMainBinding?.navView?.inflateMenu(R.menu.activity_main_user)
                val navHeaderView = _activityMainBinding?.navView?.getHeaderView(0)
                user.image?.let { navHeaderView?.findViewById<ImageView>(R.id.user_profile_image)?.loadImage(it) }
                navHeaderView?.findViewById<TextView>(R.id.user_profile_name)?.text = user.username
                navHeaderView?.findViewById<TextView>(R.id.user_profile_email)?.text = user.email
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
                lifecycleScope.launch { userPreferences.clearAuthToken() }
                navController.navigateUp()
                updateMenu(null)
                _activityMainBinding?.root?.showSnackBar("Successfully logged out")
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