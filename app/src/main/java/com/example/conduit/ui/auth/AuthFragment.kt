package com.example.conduit.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.conduit.R
import com.example.conduit.databinding.FragmentAuthBinding

class AuthFragment : Fragment(){

    private var _fragmentAuthBinding: FragmentAuthBinding? = null
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _fragmentAuthBinding = FragmentAuthBinding.inflate(inflater, container, false)
        return _fragmentAuthBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.auth_nav_host_fragment)
        _fragmentAuthBinding!!.authBottomNav.setupWithNavController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentAuthBinding = null
    }
}