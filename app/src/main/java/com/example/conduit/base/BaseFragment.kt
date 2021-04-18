package com.example.conduit.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding, VM : ViewModel, R : BaseRepo> : Fragment() {

    protected var _binding: B? = null
    protected lateinit var viewModel: VM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = getBinding(inflater, container)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = getRepo()
        val factory = ViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory).get(getViewModal())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun getViewModal(): Class<VM>

    abstract fun getRepo(): R

    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): B
}