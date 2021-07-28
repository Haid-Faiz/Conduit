package com.example.conduit.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.api.models.entities.User
import com.example.api.services.ConduitClient
import com.example.conduit.AuthViewModel
import com.example.conduit.base.BaseFragment
import com.example.conduit.base.Resource
import com.example.conduit.data.repos.UserRepo
import com.example.conduit.databinding.FragmentSettingsBinding
import com.example.conduit.utils.handleApiError
import com.example.conduit.utils.showSnackBar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SettingsFragment : BaseFragment<FragmentSettingsBinding, AuthViewModel, UserRepo>() {

//    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.user.observe(viewLifecycleOwner) {

            (it is Resource.Loading).let { check:Boolean->
                _binding!!.progressBarSettings.isVisible = check
                _binding!!.updateButton.text = if (check) "" else "Update profile"
                _binding!!.updateButton.isEnabled = !check
            }
            when(it) {
                is Resource.Error -> handleApiError(it) {updateUserData()}
                is Resource.Success -> updateUI(it.data?.user)
            }
        }
    }

    private fun updateUI(user: User?) {
        user?.let {
            _binding?.apply {
                usernameInput.editText?.setText(it.username)
                imageUrlInput.editText?.setText(it.image)
                bioInput.editText?.setText(it.bio)
                emailInput.editText?.setText(it.email)
            }
            requireView().showSnackBar("Your profile updated successfully.")
        }
    }

    private fun updateUserData() {
        _binding?.apply {
            updateButton.setOnClickListener {
                viewModel.updateUser(
                    // Actually takeIf { it.isNotBlank() } will return null when the editText would be blank
                    // & if we update blank string on api then api will accept it & will replace the value with
                    // blank & we don't want that But if we send null to api instead of blank string
                    // then api won't update anything.
                    username = usernameInput.editText?.text.toString().takeIf { it.isNotBlank() },
                    email = emailInput.editText?.text.toString().takeIf { it.isNotBlank() },
                    bio = bioInput.editText?.text.toString(),
                    imageUrl = imageUrlInput.editText?.text.toString()
                )
            }
        }
    }

    override fun getViewModal(): Class<AuthViewModel> = AuthViewModel::class.java

    override fun getRepo(): UserRepo {
        val authToken = runBlocking { userPreference.authToken.first() }
        return UserRepo(authApi = authToken?.let { ConduitClient.getAuthApiService(it)}, userPreference = userPreference)
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(inflater, container, false)
}