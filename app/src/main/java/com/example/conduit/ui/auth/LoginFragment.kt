package com.example.conduit.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.api.models.entities.User
import com.example.api.services.ConduitClient
import com.example.conduit.AuthViewModel
import com.example.conduit.R
import com.example.conduit.base.BaseFragment
import com.example.conduit.base.Resource
import com.example.conduit.data.repos.UserRepo
import com.example.conduit.databinding.FragmentLoginSignupBinding
import com.example.conduit.extensions.handleApiError

class LoginFragment : BaseFragment<FragmentLoginSignupBinding, AuthViewModel, UserRepo>() {

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.usernameInput?.visibility = View.INVISIBLE
        _binding?.authenticationTextview?.text = "Sign in"
        // It's a main navController (It's NavHost is on activity_main.xml)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        signIn()
        viewModel.user.observe(viewLifecycleOwner) {
            _binding!!.progressBarAuth.isVisible = it is Resource.Loading
            when (it) {
                is Resource.Failure -> handleApiError(it) { signIn() }
                is Resource.Success -> updateUI(it.value.body()?.user)
            }
        }
    }

    private fun updateUI(user: User?) {
        user?.let {
            viewModel.saveAuthToken(it.token)
            navController.navigateUp()
        }
    }

    private fun signIn() {
        _binding?.submitButton?.setOnClickListener {
            viewModel.loginUser(
                _binding!!.emailInput.editText?.text.toString().trim(),
                _binding!!.passInput.editText?.text.toString().trim()
            )
        }
    }


    override fun getViewModal(): Class<AuthViewModel> = AuthViewModel::class.java

    override fun getRepo(): UserRepo = UserRepo(
        publicApi = ConduitClient.getApiService(),
        userPreference = userPreference
    )

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginSignupBinding =
        FragmentLoginSignupBinding.inflate(inflater, container, false)
}