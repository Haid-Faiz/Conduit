package com.example.conduit.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.conduit.data.UserPreference

abstract class BaseFragment<B : ViewBinding, VM : BaseViewModel, R : BaseRepo> : Fragment() {

    protected var _binding: B? = null
    protected lateinit var viewModel: VM
    protected lateinit var userPreference: UserPreference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = getBinding(inflater, container)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreference = UserPreference(requireContext())
        val repo = getRepo()
        val factory = ViewModelFactory(repo)
        viewModel = ViewModelProvider(requireActivity(), factory).get(getViewModal())
    }

    override fun onDestroy() {
        super.onDestroy()
        // Making it null in onDestroyView saves a memory leak
        _binding = null
    }

    abstract fun getViewModal(): Class<VM>

    abstract fun getRepo(): R

    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): B
}