package com.cs4520.assignment5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cs4520.assignment5.databinding.LoginFragmentBinding

class LoginFragment : Fragment(R.layout.login_fragment) {
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = LoginFragmentBinding.inflate(inflater, container, false)

        _binding!!.loginButton.setOnClickListener(View.OnClickListener {
            val userNameView = _binding!!.loginUser
            val passwordView = _binding!!.loginPass
            val userName = userNameView.text.toString()
            val password = passwordView.text.toString()

            if (userName == "admin" && password == "admin") {
                // Reset auth fields after sign in
                userNameView.setText("")
                passwordView.setText("")
                // Navigate to product list if credentials are correct
                findNavController().navigate(R.id.action_loginFrag_to_pL)
            }
            // Display error toast if auth is incorrect
            else {
                val toast = Toast.makeText(binding.root.context, getString(R.string.error), Toast.LENGTH_SHORT)
                toast.show()
            }
        })

        return binding.root
    }



}