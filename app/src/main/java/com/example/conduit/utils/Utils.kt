package com.example.conduit.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.example.conduit.base.Resource
import com.google.android.material.snackbar.Snackbar


fun View.showSnackBar(
    message: String,
    action: (() -> Unit)? = null
) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    action?.let {
        snackBar.setAction("Retry") {
            it()
        }
    }
    snackBar.show()
}


fun Fragment.handleApiError(
    error: Resource.Error,
    retryFun: (() -> Unit)? = null
) {
    when {
        error.isNetworkError -> requireView().showSnackBar("Please check your internet connection", retryFun)
        // Code 401 -> UnAuthorized request
        error.errorCode == 401 -> {

        }
    }
}

//
//fun Fragment.handleApiError(
//    failure: Resource.Error,
//    retryFunction: (() -> Unit)? = null
//) {
//    when {
//        failure.isNetworkError -> {
//            requireView().showSnackBar("Please check your network connection", retryFunction)
//        }
//        failure.errorCode == 401 -> {
//            // Error code 401 means UnAuthorized request
//            if (this is SignInFragment) {
//                // If it comes from signInFragment -> Means user has entered wrong email or password
//                requireView().showSnackBar("You have entered wrong email or password")
//            } else {
//                // If it comes from other than SignInFragment then access/auth token has been
//                // expired so we need to logout the user & user will have to sigIn again
//                // TODO Logout the user
//                (this as BaseFragment<*, *, *>).logout()
//            }
//        }
//        else -> {
//            // For any other error we can simply display error body
//            val error: String = failure.errorBody?.string()
//                .toString()  // toString() lgane se null string nhi aaegi
//            requireView().showSnackBar(error)
//        }
//    }
//}
