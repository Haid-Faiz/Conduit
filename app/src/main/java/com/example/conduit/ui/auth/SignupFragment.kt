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

class SignupFragment : BaseFragment<FragmentLoginSignupBinding, AuthViewModel, UserRepo>() {

    //    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.usernameInput?.visibility = View.VISIBLE
        _binding?.authenticationTextview?.text = "Sign up"
        // It's a main navController (It's NavHost is on activity_main.xml)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        signUp()
        viewModel.user.observe(viewLifecycleOwner) {
            // This line will automatically take care of visibility of progress bar
            _binding!!.progressBarAuth.isVisible = it is Resource.Loading
            when (it) {
                is Resource.Failure -> handleApiError(it) { signUp() }
                is Resource.Success -> updateUI(it.value.body()?.user)
            }
        }
    }

    private fun signUp() {
        _binding!!.submitButton.setOnClickListener {
            viewModel.signupUser(
                _binding!!.usernameInput.editText?.text.toString().trim(),
                _binding!!.emailInput.editText?.text.toString().trim(),
                _binding!!.passInput.editText?.text.toString().trim()
            )
        }
    }

    private fun updateUI(user: User?) {
        // Save Auth Token & Goto Home Screen
        user?.let {
            viewModel.saveAuthToken(it.token)
            navController.navigateUp()
        }
    }

    override fun getViewModal(): Class<AuthViewModel> = AuthViewModel::class.java

    override fun getRepo(): UserRepo =
        UserRepo(publicApi = ConduitClient.getApiService(), userPreference = userPreference)

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginSignupBinding =
        FragmentLoginSignupBinding.inflate(inflater, container, false)
}