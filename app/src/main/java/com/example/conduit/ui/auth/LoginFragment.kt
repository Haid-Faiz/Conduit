package com.example.conduit.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
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
import com.example.conduit.extensions.showSnackBar

class LoginFragment : BaseFragment<FragmentLoginSignupBinding, AuthViewModel, UserRepo>() {

    private lateinit var navController: NavController

    private val sharedAuthViewModel by activityViewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.usernameInput?.visibility = View.INVISIBLE
        _binding?.authenticationTextview?.text = "Sign in"
        // It's a main navController (It's NavHost is on activity_main.xml)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        signIn()
        sharedAuthViewModel.user.observe(viewLifecycleOwner) {

            (it is Resource.Loading).let {
                _binding!!.progressBarAuth.isVisible = it
                _binding!!.submitButton.text = if (it) "" else "Submit"
                _binding!!.submitButton.isEnabled = !it
                _binding!!.emailInput.isEnabled = !it
                _binding!!.passInput.isEnabled = !it
            }

            when (it) {
                is Resource.Success -> updateUI(it.value.user)
                is Resource.Failure -> handleApiError(it) { signIn() }
            }
        }
    }

    private fun updateUI(user: User?) {
        user?.let {
            sharedAuthViewModel.saveAuthToken(it.token)
            navController.navigateUp()
        }
    }

    private fun signIn() {
        _binding?.submitButton?.setOnClickListener {
            sharedAuthViewModel.loginUser(
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