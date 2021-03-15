package com.example.conduit.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.conduit.AuthViewModel
import com.example.conduit.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.user.observe(viewLifecycleOwner) {

            _binding?.apply {
                usernameInput.editText?.setText(it?.username)
                imageUrlInput.editText?.setText(it?.image)
                bioInput.editText?.setText(it?.bio)
                emailInput.editText?.setText(it?.email)
                passInput.editText?.setText(it?.email)
            }
        }

        _binding?.apply {
            updateButton.setOnClickListener {
                authViewModel.updateUser(
                    // Actually takeIf { it.isNotBlank() } will return null when the editText would be blank
                    // & if we update blank string on api then api will accept it & will replace the value with
                    // blank & we don't want that But if we send null to api instead of blank string
                    // then api won't update anything.
                    username = usernameInput.editText?.text.toString().takeIf { it.isNotBlank() },
                    email = emailInput.editText?.text.toString().takeIf { it.isNotBlank() },
                    bio = bioInput.editText?.text.toString(),
                    imageUrl = imageUrlInput.editText?.text.toString(),
                    password = passInput.editText?.text.toString().takeIf { it.isNotBlank() }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Making it null in onDestroyView saves a memory leak
        _binding = null
    }

}