package com.example.conduit.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.conduit.AuthViewModel
import com.example.conduit.databinding.FragmentLoginSignupBinding

class SignupFragment : Fragment() {

    private var _fragmentLoginSignupBinding: FragmentLoginSignupBinding? = null
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _fragmentLoginSignupBinding = FragmentLoginSignupBinding.inflate(inflater, container, false)
        _fragmentLoginSignupBinding?.usernameInput?.visibility = View.VISIBLE
        return _fragmentLoginSignupBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _fragmentLoginSignupBinding!!.submitButton.setOnClickListener {
            authViewModel.signupUser(
                _fragmentLoginSignupBinding!!.usernameInput.editText?.text.toString().trim(),
                _fragmentLoginSignupBinding!!.emailInput.editText?.text.toString().trim(),
                _fragmentLoginSignupBinding!!.passInput.editText?.text.toString().trim()
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentLoginSignupBinding = null
    }
}